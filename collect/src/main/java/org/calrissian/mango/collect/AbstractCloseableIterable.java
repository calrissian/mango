package org.calrissian.mango.collect;

import com.google.common.io.Closeables;

import java.io.IOException;
import java.util.Iterator;

/**
 */
public abstract class AbstractCloseableIterable<T> implements CloseableIterable<T> {

    protected boolean closed = false;

    @Override
    public void closeQuietly() {
        Closeables.closeQuietly(this);
    }

    @Override
    public void close() throws IOException {
        if (!closed) {
            doClose();
            closed = true;
        }
    }

    @Override
    public Iterator<T> iterator() {
        if (closed) throw new IllegalStateException("Iterable is already closed");
        return retrieveIterator();
    }

    protected abstract void doClose() throws IOException;

    protected abstract Iterator<T> retrieveIterator();
}
