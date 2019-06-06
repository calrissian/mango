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
package org.calrissian.mango.collect;

import java.io.Closeable;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * Iterable that is closeable to release underlying resources
 */
public interface CloseableIterable<T> extends Iterable<T>, Closeable {

    @Override
    void close();

    /**
     * <p>Unconditionally closes the iterator.</p>
     * <p>Equivalent to {@link CloseableIterable#close()}, except any exceptions will be ignored.</p>
     */
    default void closeQuietly() {
        try {
            close();
        } catch (Exception e) {
            // Ignore exceptions
        }
    }

    /**
     * Returns a sequential {@code Stream} with this iterable as its source. The resulting stream also close the
     * underlying resource if it {@link Stream#close()} method is called.
     *
     * <p>This method should be overridden when the {@link #spliterator()}
     * method cannot return a spliterator that is {@code IMMUTABLE},
     * {@code CONCURRENT}, or <em>late-binding</em>. (See {@link #spliterator()}
     * for details.)
     *
     * @implSpec
     * The default implementation creates a sequential {@code Stream} from the
     * collection's {@code Spliterator}.
     */
    default Stream<T> stream() {
        return StreamSupport.stream(spliterator(), false)
                .onClose(this::close);
    }

}
