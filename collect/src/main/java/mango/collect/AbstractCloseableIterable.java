package mango.collect;

import java.io.IOException;
import java.util.Iterator;

/**
 */
public abstract class AbstractCloseableIterable<T> implements CloseableIterable<T> {

    protected boolean closed = false;

    @Override
    public void closeQuietly() {
        try {
            close();
        } catch (IOException e) {
        }
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

    public boolean isClosed() {
        return closed;
    }

    protected abstract void doClose() throws IOException;

    protected abstract Iterator<T> retrieveIterator();
}
