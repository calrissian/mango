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
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.concurrent.Executors.newSingleThreadScheduledExecutor;

/**
 * Intentionally package private
 */
final class TimeBatcher<T> implements Batcher<T> {

    private final ScheduledExecutorService runService;

    private final BlockingQueue<T> backingQueue;
    private final BatchListener<? extends T> listener;
    private final ExecutorService handler;

    TimeBatcher(BlockingQueue<T> backingQueue, BatchListener<? extends T> listener, ExecutorService handler, long maxTime, TimeUnit timeUnit) {
        this.backingQueue = backingQueue;
        this.listener = listener;
        this.handler = handler;

        runService = newSingleThreadScheduledExecutor();
        runService.scheduleAtFixedRate(new BatchRunnable(), maxTime, maxTime, timeUnit);
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
        @Override
        public void run() {
            try {
                final Collection<T> batch = new ArrayList<T>();
                backingQueue.drainTo(batch);

                //Do nothing if empty.  Also good faith shutdown check
                if (!batch.isEmpty() && !handler.isShutdown()) {
                    handler.submit(new Runnable() {
                        @Override
                        public void run() {
                            listener.onBatch(batch);
                        }
                    });
                }
            } catch (Exception e) {
                //Do nothing, just let method end.
            }
        }
    }
}
