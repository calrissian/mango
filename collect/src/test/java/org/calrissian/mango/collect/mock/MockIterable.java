package org.calrissian.mango.collect.mock;

import com.google.common.io.Closeables;
import org.calrissian.mango.collect.CloseableIterable;

import java.io.IOException;
import java.util.Iterator;

public class MockIterable<T> implements CloseableIterable<T> {
    private boolean closed = false;
    public Iterable<T> internal;

    public MockIterable(Iterable<T> internal) {
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
    public Iterator<T> iterator() {
        if (closed) throw new IllegalStateException("Iterable is already closed");
        return internal.iterator();
    }

    public boolean isClosed() {
        return closed;
    }
}
