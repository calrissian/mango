package com.texeltek.mango.collect;

import java.io.Closeable;
import java.util.Iterator;

/**
 * An Iterator that needs to be closed once it is no longer being used in order to clean up opened resources 
 */
public interface CloseableIterator<T> extends Iterator<T>,Closeable,Iterable<T> {
    /**
     * <p>Unconditionally closes the iterator.</p>
     * <p>Equivalent to {@link CloseableIterator#close()}, except any exceptions will be ignored.</p>
     */
    public void closeQuietly();
}
