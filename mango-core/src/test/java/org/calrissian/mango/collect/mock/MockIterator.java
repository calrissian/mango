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
