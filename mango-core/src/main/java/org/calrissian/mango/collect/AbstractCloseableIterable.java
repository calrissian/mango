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
package org.calrissian.mango.collect;

import com.google.common.io.Closeables;

import java.io.IOException;
import java.util.Iterator;

/**
 * This class represents a skeletal implementation of a {@link CloseableIterable}.
 * The implementation will manage all operations concerned with checks regarding
 * whether a resource has already been closed.
 *
 * Implementations of this abstract class will have to implement the logic involved
 * in actually closing and retrieving the {@link Iterator} from the {@link Iterable}.
 */
public abstract class AbstractCloseableIterable<T> implements CloseableIterable<T> {

    protected boolean closed = false;

    /**
     * Performes the logic to cleanup any held resources
     * @throws IOException
     */
    protected abstract void doClose() throws IOException;

    /**
     * Returns an iterator over a set of elements of type T.
     *
     * @return an Iterator.
     */
    protected abstract Iterator<T> retrieveIterator();

    /**
     * {@inheritDoc}
     */
    @Override
    public void closeQuietly() {
        try {
            Closeables.close(this, true);
        } catch (IOException e) {
            // IOException should not have been thrown
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void close() throws IOException {
        if (!closed) {
            doClose();
            closed = true;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Iterator<T> iterator() {
        if (closed)
            throw new IllegalStateException("Iterable is already closed");

        return retrieveIterator();
    }

}
