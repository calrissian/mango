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
        Closeables.closeQuietly(this);
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
