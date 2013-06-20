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
package org.calrissian.mango.collect.mock;

import com.google.common.io.Closeables;
import org.calrissian.mango.collect.CloseableIterator;

import java.io.IOException;
import java.util.Iterator;

public class MockIterator<T> implements CloseableIterator<T> {

    private boolean closed = false;
    private Iterator<T> internal;

    public MockIterator(Iterator<T> internal) {
        this.internal = internal;
    }

    @Override
    public void closeQuietly() {
        Closeables.closeQuietly(this);
    }

    @Override
    public void close() throws IOException {
        closed = true;
    }

    @Override
    public boolean hasNext() {
        return internal.hasNext();
    }

    @Override
    public T next() {
        return internal.next();
    }

    @Override
    public void remove() {
        internal.remove();
    }

    public boolean isClosed() {
        return closed;
    }
}
