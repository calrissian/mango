/*
 * Copyright (C) 2013 The Calrissian Authors
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
package org.calrissian.mango.concurrent;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.*;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static java.lang.System.nanoTime;
import static java.util.concurrent.Executors.newCachedThreadPool;
import static java.util.concurrent.TimeUnit.NANOSECONDS;

/**
 * Constructs a {@link Batcher} implementation that creates batches with one or more bounding conditions.  All batchers
 * pass off batches to a {@code listenerService()} to allow for handling of batches asynchronously via a
 * {@link BatchListener}.  If no {@code listenerService()} is provided then a cached thread pool will be used.
 */
public final class BatcherBuilder {

    /**
     * Creates a new builder for creating a {@link Batcher}
     */
    public static BatcherBuilder create() {
        return new BatcherBuilder();
    }

    private int maxSize = -1;
    private long interval = -1;

    private ExecutorService listenerService = null;
    private Integer maxQueueSize;

    private BatcherBuilder() {/**private constructor*/}

    /**
     * Add a max size component for a batcher.  If specified a batcher will call the {@link BatchListener}
     * as soon as the maxSize is reached.
     */
    public BatcherBuilder maxSize(int maxSize) {
        checkArgument(maxSize > 0, "Required to have a max size greater than 0");
        this.maxSize = maxSize;
        return this;
    }

    /**
     * Add a max time component for a batcher.  If specified a batcher will call the {@link BatchListener}
     * at most once for the time specified if there are any elements in the batch.
     */
    public BatcherBuilder maxTime(long maxTime, TimeUnit timeUnit) {
        checkArgument(maxTime > 0, "Required to have a max time greater than 0");
        checkNotNull(timeUnit);
        this.interval = timeUnit.toNanos(maxTime);
        return this;
    }

    /**
     * Bound the queue size used by the batchers.  By specifying a max size, all producers to the {@link Batcher}
     * the batcher will at most keep {@code maxQueueSize} elements.  Producers can determine how to handle
     * a full queue using one of the {@link Batcher}'s add methods.
     */
    public BatcherBuilder maxQueueSize(int maxQueueSize) {
        checkArgument(maxQueueSize > 0, "Required to have a max queue size greater than 0");
        this.maxQueueSize = maxQueueSize;
        return this;
    }

    /**
     * Provide a configured {@link ExecutorService} to use for running the {@link BatchListener} on.
     * This {@link ExecutorService} will be shutdown when the created batcher is closed.
     */
    public BatcherBuilder listenerService(ExecutorService listenerService) {
        checkNotNull(listenerService);
        this.listenerService = listenerService;
        return this;
    }

    public <T> Batcher<T> build(BatchListener<T> listener) {
        checkNotNull(listener);
        checkArgument(maxSize > 0 || interval > 0, "All batchers are required to have either a time or size bound.");

        ExecutorService handler = (listenerService == null ? newCachedThreadPool() : listenerService);
        BlockingQueue<T> backingQueue = (maxQueueSize == null ? new LinkedBlockingQueue<T>() : new ArrayBlockingQueue<T>(maxQueueSize));

        if (maxSize > 0 && interval > 0) {
            return new TimeOrSizeBatcher<T>(backingQueue, listener, handler, maxSize, interval)
                    .start();
        } else if (maxSize > 0) {
            return new SizeBatcher<T>(backingQueue, listener, handler, maxSize)
                    .start();
        } else {
            return new TimeBatcher<T>(backingQueue, listener, handler, interval)
                    .start();
        }
    }

    private static final class SizeBatcher<T> extends AbstractBatcher<T> {

        private final int maxSize;

        SizeBatcher(BlockingQueue<T> backingQueue, BatchListener<T> listener, ExecutorService handler, int maxSize) {
            super(backingQueue, listener, handler, new ArrayList<T>(maxSize));
            this.maxSize = maxSize;
        }

        @Override
        protected void populateBatch(BlockingQueue<T> backingQueue, Collection<T> batch) throws InterruptedException {
            int remainingSize = maxSize;

            while (remainingSize > 0) {
                //First try to drain the queue into the batch, but if there is not enough data then fall back to a
                //blocking call to wait for data to enter the queue.
                if (backingQueue.drainTo(batch, remainingSize) != remainingSize) {
                    batch.add(backingQueue.take());
                }

                remainingSize = maxSize - batch.size();
            }
        }
    }

    private static final class TimeBatcher<T> extends AbstractBatcher<T> {

        private final long interval;

        TimeBatcher(BlockingQueue<T> backingQueue, BatchListener<T> listener, ExecutorService handler, long interval) {
            super(backingQueue, listener, handler, new ArrayList<T>());
            this.interval = interval;
        }

        @Override
        protected void populateBatch(BlockingQueue<T> backingQueue, Collection<T> batch) throws InterruptedException {
            long startTime = nanoTime();
            long remaining = interval;

            while (remaining >= 0) {
                //First try to drain the queue into the batch, but if there is no data then fall back to a
                //blocking call to wait for data to enter the queue.
                if (backingQueue.drainTo(batch) == 0) {
                    T item = backingQueue.poll(remaining, NANOSECONDS);
                    if (item == null)
                        break; //poll timed out, should try and send batch

                    batch.add(item);
                }

                //Order of operations matters to minimize overflows
                remaining = interval - (nanoTime() - startTime);
            }
        }
    }

    private static final class TimeOrSizeBatcher<T> extends AbstractBatcher<T> {

        private final int maxSize;
        private final long interval;

        TimeOrSizeBatcher(BlockingQueue<T> backingQueue, BatchListener<T> listener, ExecutorService handler, int maxSize, long interval) {
            super(backingQueue, listener, handler, new ArrayList<T>(maxSize));
            this.maxSize = maxSize;
            this.interval = interval;
        }

        @Override
        protected void populateBatch(BlockingQueue<T> backingQueue, Collection<T> batch) throws InterruptedException {
            long startTime = nanoTime();
            long remainingTime = interval;
            int remainingSize = maxSize;

            while (remainingSize > 0 && remainingTime >= 0) {
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
        }
    }
}
