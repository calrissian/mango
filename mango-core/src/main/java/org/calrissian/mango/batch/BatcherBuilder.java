/*
 * Copyright (C) 2017 The Calrissian Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.calrissian.mango.batch;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.*;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkState;
import static java.lang.System.nanoTime;
import static java.lang.Thread.currentThread;
import static java.util.Objects.requireNonNull;
import static java.util.concurrent.Executors.newCachedThreadPool;
import static java.util.concurrent.TimeUnit.NANOSECONDS;

/**
 * Constructs a {@link Batcher} implementation that creates batches with one or more bounding conditions.  All batchers
 * pass off batches to a {@code listenerService()} to allow for handling of batches asynchronously via a
 * {@link BatchListener}.  If no {@code listenerService()} is provided then a cached thread pool will be used.
 */
public final class BatcherBuilder {

    private static final int UNSET_INT = -1;

    /**
     * Creates a new builder for creating a {@link Batcher}.
     */
    public static BatcherBuilder create() {
        return new BatcherBuilder();
    }

    private int maxSize = UNSET_INT;
    private long interval = UNSET_INT;
    private int maxBufferSize = UNSET_INT;
    private ExecutorService listenerService = null;

    private BatcherBuilder() {/**private constructor*/}

    /**
     * Add a max size component for a batcher.  If specified a batcher will call the {@link BatchListener}
     * as soon as the size bound is reached.
     */
    public BatcherBuilder sizeBound(int size) {
        checkState(this.maxSize == UNSET_INT, "Max size already set to %s", this.maxSize);
        checkArgument(size > 0, "Required to have a size bound greater than 0");
        this.maxSize = size;
        return this;
    }

    /**
     * Add a max time component for a batcher.  If specified a batcher will call the {@link BatchListener}
     * as soon as the time bound has been exceeded.
     *
     * NOTE: This interval is the time since successfully sending the last batch to the batchlistener.  This means
     * that if a blocking {@link BatcherBuilder#listenerService(ExecutorService)} is used then the time interval will not
     * restart until it has been successfully handed off to the {@link BatcherBuilder#listenerService(ExecutorService)}.
     */
    public BatcherBuilder timeBound(long time, TimeUnit timeUnit) {
        checkState(this.interval == UNSET_INT, "Max time already set");
        checkArgument(time > 0, "Required to have a time interval greater than 0");
        requireNonNull(timeUnit);
        this.interval = timeUnit.toNanos(time);
        return this;
    }

    /**
     * Bound the buffer size used by the batchers.  By specifying a max size, the {@link Batcher}
     * will allow producers to start blocking when adding data to the batcher.  Producers can determine how to handle
     * a full buffer using one of the {@link Batcher}'s add methods.  By default the buffer size is unbounded.
     */
    public BatcherBuilder bufferSize(int bufferSize) {
        checkState(this.maxBufferSize == UNSET_INT, "Max buffer size already set to %d", this.maxBufferSize);
        checkArgument(bufferSize > 0, "Required to have a buffer size greater than 0");
        this.maxBufferSize = bufferSize;
        return this;
    }

    /**
     * Provide a configured {@link ExecutorService} to use for running the {@link BatchListener} on.
     * This {@link ExecutorService} will be shutdown when the created batcher is closed.  By default a cached
     * thread pool is used to handle calls to the {@link BatchListener}.
     */
    public BatcherBuilder listenerService(ExecutorService listenerService) {
        checkState(this.listenerService == null, "A listener service has already been set");
        requireNonNull(listenerService);
        this.listenerService = listenerService;
        return this;
    }

    /**
     * Builds a {@link Batcher} which will provide batches to the provided {@code listener}.
     *
     * Note: The builder is required to have either a time or size bound to build a batch.
     */
    public <T> Batcher<T> build(BatchListener<T> listener) {
        requireNonNull(listener);
        checkState(maxSize != UNSET_INT || interval != UNSET_INT, "All batchers are required to have either a time or size bound.");

        ExecutorService handler = (listenerService == null ? newCachedThreadPool() : listenerService);
        BlockingQueue<T> backingQueue = (maxBufferSize == UNSET_INT ? new LinkedBlockingQueue<T>() : new ArrayBlockingQueue<T>(maxBufferSize));

        if (maxSize != UNSET_INT && interval != UNSET_INT) {
            return new TimeOrSizeBatcher<>(backingQueue, listener, handler, maxSize, interval)
                    .start();
        } else if (maxSize != UNSET_INT) {
            return new SizeBatcher<>(backingQueue, listener, handler, maxSize)
                    .start();
        } else {
            return new TimeBatcher<>(backingQueue, listener, handler, interval)
                    .start();
        }
    }

    private static final class SizeBatcher<T> extends AbstractBatcher<T> {

        private final int maxSize;

        SizeBatcher(BlockingQueue<T> backingQueue, BatchListener<T> listener, ExecutorService handler, int maxSize) {
            super(backingQueue, listener, handler);
            this.maxSize = maxSize;
        }

        @Override
        protected Collection<T> generateBatch(BlockingQueue<T> backingQueue) throws InterruptedException {
            Collection<T> batch = new ArrayList<>(maxSize);
            try {
                int remainingSize = maxSize;

                while (remainingSize > 0) {
                    //First try to drain the queue into the batch, but if there is not enough data then fall back to a
                    //blocking call to wait for data to enter the queue.
                    if (backingQueue.drainTo(batch, remainingSize) != remainingSize) {
                        batch.add(backingQueue.take());
                    }

                    remainingSize = maxSize - batch.size();
                }
            } catch (InterruptedException e) {
                currentThread().interrupt();
            }
            return batch;
        }
    }

    private static final class TimeBatcher<T> extends AbstractBatcher<T> {

        private final long interval;

        TimeBatcher(BlockingQueue<T> backingQueue, BatchListener<T> listener, ExecutorService handler, long interval) {
            super(backingQueue, listener, handler);
            this.interval = interval;
        }

        @Override
        protected Collection<T> generateBatch(BlockingQueue<T> backingQueue) throws InterruptedException {
            Collection<T> batch = new ArrayList<>();
            try {
                long startTime = nanoTime();
                long remainingTime = interval;

                while (remainingTime > 0) {
                    //First try to drain the queue into the batch, but if there is no data then fall back to a
                    //blocking call to wait for data to enter the queue.
                    if (backingQueue.drainTo(batch) == 0) {
                        T item = backingQueue.poll(remainingTime, NANOSECONDS);
                        if (item == null)
                            break; //poll timed out, should try and send batch

                        batch.add(item);
                    }

                    //Order of operations matters to minimize overflows
                    remainingTime = interval - (nanoTime() - startTime);
                }
            } catch (InterruptedException e) {
                currentThread().interrupt();
            }
            return batch;
        }
    }

    private static final class TimeOrSizeBatcher<T> extends AbstractBatcher<T> {

        private final int maxSize;
        private final long interval;

        TimeOrSizeBatcher(BlockingQueue<T> backingQueue, BatchListener<T> listener, ExecutorService handler, int maxSize, long interval) {
            super(backingQueue, listener, handler);
            this.maxSize = maxSize;
            this.interval = interval;
        }

        @Override
        protected Collection<T> generateBatch(BlockingQueue<T> backingQueue) throws InterruptedException {
            Collection<T> batch = new ArrayList<>(maxSize);
            try{
                long startTime = nanoTime();
                long remainingTime = interval;
                int remainingSize = maxSize;

                while (remainingSize > 0 && remainingTime > 0) {
                    //First try to drain the queue into the batch, but if there is not enough data then fall back to a
                    //blocking call to wait for data to enter the queue.
                    if (backingQueue.drainTo(batch, remainingSize) != remainingSize) {
                        T item = backingQueue.poll(remainingTime, NANOSECONDS);
                        if (item == null)
                            break; //poll timed out, should try and send batch

                        batch.add(item);
                    }

                    //Order of operations matters to minimize overflows
                    remainingTime = interval - (nanoTime() - startTime);
                    remainingSize = maxSize - batch.size();
                }
            } catch (InterruptedException e) {
                currentThread().interrupt();
            }
            return batch;
        }
    }
}
