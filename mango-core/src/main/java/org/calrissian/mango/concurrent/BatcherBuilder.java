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

import java.util.concurrent.*;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.concurrent.Executors.newCachedThreadPool;

/**
 * Constructs a {@link Batcher} implementation that creates batches with one or more bounding conditions.  All batchers
 * pass off batches to a {@code listenerService()} to allow for handling of batches asynchronously via a
 * {@link BatchListener}.  If no {@code listenerService()} is provided then a cached thread pool will be used.
 */
public final class BatcherBuilder<T> {

    /**
     * Creates a new builder for creating a {@link Batcher}
     */
    public static <T> BatcherBuilder<T> create() {
        return new BatcherBuilder<T>();
    }

    private Integer maxSize = null;
    private Long maxTime = null;
    private TimeUnit timeUnit = null;

    private ExecutorService listenerService = null;

    private BatcherBuilder() {/**private constructor*/}

    /**
     * Add a max size component for a batcher.  If specified a batcher will call the {@link BatchListener}
     * as soon as the maxSize is reached.
     */
    public BatcherBuilder<T> maxSize(int maxSize) {
        checkArgument(maxSize > 0, "Required to have a max size greater than 0");
        this.maxSize = maxSize;
        return this;
    }

    /**
     * Add a max time component for a batcher.  If specified a batcher will call the {@link BatchListener}
     * at most once for the time specified if there are any elements in the batch.
     */
    public BatcherBuilder<T> maxTime(long maxTime, TimeUnit timeUnit) {
        checkArgument(maxTime > 0, "Required to have a max time greater than 0");
        checkNotNull(timeUnit);
        this.maxTime = maxTime;
        this.timeUnit = timeUnit;
        return this;
    }

    /**
     * Provide a configured {@link ExecutorService} to use for running the {@link BatchListener} on.
     * This {@link ExecutorService} will be shutdown when the created batcher is closed.
     */
    public BatcherBuilder<T> listenerService(ExecutorService listenerService) {
        checkNotNull(listenerService);
        this.listenerService = listenerService;
        return this;
    }

    public Batcher<T> build(BatchListener<? extends T> listener) {
        checkNotNull(listener);
        checkArgument(maxSize != null || maxTime != null, "All batchers are required to have either a time or size bound.");

        ExecutorService handler = (listenerService == null ? newCachedThreadPool() : listenerService);

        if (maxSize != null && maxTime != null) {
            return new TimeOrSizeBatcher<T>(
                    new LinkedBlockingQueue<T>(),
                    listener,
                    handler,
                    maxSize,
                    maxTime,
                    timeUnit
            );
        } else if (maxSize != null) {
            return new SizeBatcher<T>(
                    new LinkedBlockingQueue<T>(),
                    listener,
                    handler,
                    maxSize
            );
        } else {
            return new TimeBatcher<T>(
                    new LinkedBlockingQueue<T>(),
                    listener,
                    handler,
                    maxTime,
                    timeUnit
            );
        }
    }
}
