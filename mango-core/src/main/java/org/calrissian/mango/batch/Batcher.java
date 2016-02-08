/*
 * Copyright (C) 2014 The Calrissian Authors
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

import java.io.Closeable;
import java.util.List;
import java.util.concurrent.TimeUnit;

public interface Batcher<T> extends Closeable {

    /**
     * Adds a new item to be batched. If this batcher was constructed with a max buffer size, this
     * method will attempt to add a new item if there is room, otherwise will return false immediately.
     */
    boolean add(T item);

    /**
     * Adds a new item to be batched. If this batcher was constructed with a max queue size, this
     * method will wait until there is room in the buffer to add the data or it has timed out.
     */
    boolean add(T item, long timeout, TimeUnit unit) throws InterruptedException;

    /**
     * Adds a new item to be batched. If this batcher was constructed with a max queue size, this
     * method will wait until there is room in the buffer to add the data.
     *
     * Note: If this batcher is closed and there are producers blocking on this call, they may be stuck until
     * they are interrupted.  If using a max buffer size, it is suggested that one of the other add methods
     * be used.
     */
    boolean addOrWait(T item) throws InterruptedException;

    /**
     * Closes the underlying batcher and flushes the remaining elements in the batch queue to a list.
     */
    List<T> closeAndFlush();

    /**
     * Closes the underlying batcher and any .
     */
    @Override
    void close();
}
