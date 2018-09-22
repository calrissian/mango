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
package org.calrissian.mango.collect;

import com.google.common.io.Closeables;

import java.io.Closeable;
import java.io.IOException;
import java.util.Iterator;
import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * An Iterator that needs to be closed once it is no longer being used in order to clean up opened resources
 */
public interface CloseableIterator<T> extends Iterator<T>, Closeable {
    /**
     * <p>Unconditionally closes the iterator.</p>
     * <p>Equivalent to {@link CloseableIterator#close()}, except any exceptions will be ignored.</p>
     */
    default void closeQuietly() {
        try {
            Closeables.close(this, true);
        } catch (IOException e) {
            // IOException should not have been thrown
        }
    }

    default Stream<T> stream() {
        return StreamSupport.stream(Spliterators.spliteratorUnknownSize(this, 0), false)
                .onClose(() -> {
                    try {
                        close();
                    } catch (IOException e) {
                        throw new IllegalStateException("Unable to close stream", e);
                    }
                });
    }
}
