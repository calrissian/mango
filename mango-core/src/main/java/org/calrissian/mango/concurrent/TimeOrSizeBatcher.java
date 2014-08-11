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
import java.util.concurrent.TimeUnit;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.lang.System.nanoTime;
import static java.lang.Thread.interrupted;
import static java.util.concurrent.Executors.newSingleThreadExecutor;
import static java.util.concurrent.TimeUnit.NANOSECONDS;

/**
 * Intentionally package private
 */
final class TimeOrSizeBatcher<T> implements Batcher<T> {

    private final ExecutorService runService;

    private final BlockingQueue<T> backingQueue;
    private final BatchListener<? extends T> listener;
    private final ExecutorService handler;
    private final int maxSize;
    private final long interval;

    TimeOrSizeBatcher(BlockingQueue<T> backingQueue, BatchListener<? extends T> listener, ExecutorService handler, int maxSize, long maxTime, TimeUnit timeUnit) {
        this.backingQueue = backingQueue;
        this.listener = listener;
        this.handler = handler;
        this.maxSize = maxSize;
        this.interval = timeUnit.toNanos(maxTime);

        runService = newSingleThreadExecutor();
        runService.execute(new BatchRunnable());
    }

    @Override
    public void add(T item) throws InterruptedException {
        checkNotNull(item);
        backingQueue.put(item);
    }

    @Override
    public void addAll(Iterable<? extends T> items) throws InterruptedException {
        checkNotNull(items);
        for (T item : items)
            add(item);
    }

    @Override
    public void close() {
        runService.shutdownNow();
        handler.shutdownNow();
    }

    private class BatchRunnable implements Runnable {

        private final Collection<T> batch = new ArrayList<T>(maxSize);

        @Override
        public void run() {
            try {
                while (!runService.isShutdown() && !handler.isShutdown() && !interrupted()) {

                    long startTime = nanoTime();
                    long remaining = (startTime + interval) - nanoTime();

                    while (batch.size() != maxSize && remaining >= 0) {
                        //First try to drain the queue into the batch, but if there is no data then fall back to a
                        //blocking call to wait for data to enter the queue.
                        if (backingQueue.drainTo(batch, maxSize - batch.size()) == 0) {
                            T item = backingQueue.poll(remaining, NANOSECONDS);
                            if (item == null)
                                break; //poll timed out, should try and send batch

                            batch.add(item);
                        }

                        remaining = (startTime + interval) - nanoTime();
                    }

                    //Do nothing if empty.  Also good faith shutdown check
                    if (!batch.isEmpty() && !handler.isShutdown()) {

                        //copy the batch and clear it.
                        final Collection<T> copy = new ArrayList<T>(batch);
                        batch.clear();

                        handler.execute(new Runnable() {
                            @Override
                            public void run() {
                                listener.onBatch(copy);
                            }
                        });
                    }
                }
            } catch (Exception e) {
                //Do nothing, just let method end.
            }

        }
    }
}
