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
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;
import static java.lang.Thread.interrupted;
import static java.util.concurrent.Executors.newSingleThreadExecutor;
import static java.util.logging.Level.SEVERE;

abstract class AbstractBatcher<T> implements Batcher<T> {

    private static final Logger logger = Logger.getLogger(AbstractBatcher.class.getName());


    private final ExecutorService batchService;
    private final BatchRunnable batchRunnable;
    private Future<?> batchFuture;

    private final BlockingQueue<T> backingQueue;
    private final BatchListener<T> listener;
    private final ExecutorService handler;

    private volatile boolean isClosed = false;

    AbstractBatcher(BlockingQueue<T> backingQueue, BatchListener<T> listener, ExecutorService handler, Collection<T> batch) {
        this.backingQueue = backingQueue;
        this.listener = listener;
        this.handler = handler;

        batchService = newSingleThreadExecutor();
        batchRunnable = new BatchRunnable(batch);
    }

    /**
     * Method responsible for populating the provided {@code batch} with data from the provided {@code backingQueue}
     */
    protected abstract void populateBatch(BlockingQueue<T> backingQueue, Collection<T> batch) throws InterruptedException;

    /**
     * To be called after construction of any subclass to instantiate the batching thread.
     */
    protected AbstractBatcher<T> start() {
        batchFuture = batchService.submit(batchRunnable);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final boolean add(T item) {
        checkNotNull(item);
        checkState(!isClosed, "The batcher has been closed");

        return backingQueue.offer(item);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final boolean add(T item, long timeout, TimeUnit unit) throws InterruptedException {
        checkNotNull(item);
        checkNotNull(unit);
        checkState(!isClosed, "The batcher has been closed");

        return backingQueue.offer(item, timeout, unit);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final boolean addOrWait(T item) throws InterruptedException {
        checkNotNull(item);
        checkState(!isClosed, "The batcher has been closed");

        backingQueue.put(item);
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void close() {
        isClosed = true;
        //Force an interrupt on running thread and shutdown executor cleanly.
        batchFuture.cancel(true);
        batchService.shutdown();

        handler.shutdownNow();
    }

    private class BatchRunnable implements Runnable {

        private final Collection<T> batch;

        private BatchRunnable(Collection<T> batch) {
            this.batch = batch;
        }

        @Override
        public void run() {
            try {
                while (!isClosed && !interrupted() && !handler.isShutdown()) {

                    try {
                        populateBatch(backingQueue, batch);
                    } catch (InterruptedException e) {
                        //When shut down it is expecting an interrupt, so simply exit cleanly.
                        break;
                    }

                    //Good faith handler shutdown check
                    if (!batch.isEmpty() && !handler.isShutdown()) {
                        //copy the batch and clear it.
                        final Collection<T> copy = new ArrayList<T>(batch);
                        batch.clear();

                        try {
                            handler.execute(new Runnable() {
                                @Override
                                public void run() {
                                    listener.onBatch(copy);
                                }
                            });
                        } catch (Exception e) {
                            //Handler threw exception.  Close the batcher and exit cleanly.
                            logger.log(SEVERE, "Encountered exception sending to batch listener.  Closing the batcher", e);
                            close();
                        }
                    }
                }
            } catch (Throwable e) {
                //Unknown exception
                try {
                    logger.log(SEVERE, "Batcher should not have throw exception.  Closing the batcher", e);
                    close();
                } catch (Throwable e2) {
                    //Do nothing, just exit cleanly
                }
            }
        }
    }
}
