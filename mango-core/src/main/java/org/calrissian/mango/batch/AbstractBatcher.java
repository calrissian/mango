/*
 * Copyright (C) 2019 The Calrissian Authors
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
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import static com.google.common.base.Preconditions.checkState;
import static java.lang.Long.MAX_VALUE;
import static java.lang.Thread.interrupted;
import static java.util.Collections.unmodifiableCollection;
import static java.util.Objects.requireNonNull;
import static java.util.concurrent.Executors.newSingleThreadExecutor;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.logging.Level.SEVERE;

/**
 * Handles the core logic for handling batches and the {@link ExecutorService}s responsible for executing the batch
 * processes.
 * </p>
 * Intentionally left package private.
 */
abstract class AbstractBatcher<T> implements Batcher<T> {

    private static final Logger logger = Logger.getLogger(AbstractBatcher.class.getName());

    private final ExecutorService batchService;

    private final BlockingQueue<T> backingQueue;
    private final BatchListener<T> listener;
    private final ExecutorService handler;

    private volatile boolean isClosed = false;

    AbstractBatcher(BlockingQueue<T> backingQueue, BatchListener<T> listener, ExecutorService handler) {
        this.backingQueue = backingQueue;
        this.listener = listener;
        this.handler = handler;

        batchService = newSingleThreadExecutor();
    }

    /**
     * Method responsible for populating the provided {@code batch} with data from the provided {@code backingQueue}
     */
    protected abstract Collection<T> generateBatch(BlockingQueue<T> backingQueue) throws InterruptedException;

    /**
     * To be called after construction of any subclass to instantiate the batching thread.
     */
    protected AbstractBatcher<T> start() {
        batchService.submit(new BatchRunnable());
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final boolean add(T item) {
        requireNonNull(item);
        checkState(!isClosed, "The batcher has been closed");

        return backingQueue.offer(item);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final boolean add(T item, long timeout, TimeUnit unit) throws InterruptedException {
        requireNonNull(item);
        requireNonNull(unit);
        checkState(!isClosed, "The batcher has been closed");

        return backingQueue.offer(item, timeout, unit);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final boolean addOrWait(T item) throws InterruptedException {
        requireNonNull(item);
        checkState(!isClosed, "The batcher has been closed");

        backingQueue.put(item);
        return true;
    }

    /**
     * Used to shutdown the batching thread in case of an error or user requested close.
     */
    private void stopRunnable() {
        isClosed = true;
        //Force an interrupt on running thread and shutdown executor cleanly.
        batchService.shutdownNow();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void close() {
        stopRunnable();
        try {
            batchService.awaitTermination(MAX_VALUE, MILLISECONDS);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        handler.shutdown();
        try {
            handler.awaitTermination(MAX_VALUE, MILLISECONDS);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<T> closeAndFlush() {
        close();
        //Must wait until after close to drain
        ArrayList<T> remaining = new ArrayList<>();
        backingQueue.drainTo(remaining);

        return remaining;
    }

    private class BatchRunnable implements Runnable {

        @Override
        public void run() {
            try {
                while (!isClosed && !handler.isShutdown() && !interrupted()) {
                    //Each batcher should return complete batches or partial batches in the case of interrupts.
                    final Collection<T> batch = generateBatch(backingQueue);

                    //Good faith handler shutdown check
                    if (!batch.isEmpty() && !handler.isShutdown()) {
                        try {
                            handler.execute(() -> listener.onBatch(unmodifiableCollection(batch)));
                        } catch (Exception e) {
                            //Handler threw exception.  Close the batcher and exit cleanly.
                            logger.log(SEVERE, "Encountered exception sending to batch listener.  Stopping the batcher", e);
                            stopRunnable();
                        }
                    }
                }
            } catch (Throwable e) {
                //Unknown exception
                try {
                    logger.log(SEVERE, "Batcher should not have throw exception.  Stopping the batcher", e);
                    stopRunnable();
                } catch (Throwable e2) {
                    //Do nothing, just exit cleanly
                }
            }
        }
    }
}
