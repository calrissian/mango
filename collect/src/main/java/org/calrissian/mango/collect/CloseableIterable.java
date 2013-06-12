package org.calrissian.mango.collect;

import java.io.Closeable;

/**
 * Iterable that is closeable to release underlying resources
 */
public interface CloseableIterable<T> extends Iterable<T>, Closeable {

    /**
     * <p>Unconditionally closes the iterator.</p>
     * <p>Equivalent to {@link CloseableIterator#close()}, except any exceptions will be ignored.</p>
     */
    public void closeQuietly();

}
