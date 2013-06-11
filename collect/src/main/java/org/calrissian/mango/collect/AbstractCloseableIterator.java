package org.calrissian.mango.collect;

import com.google.common.collect.AbstractIterator;
import com.google.common.io.Closeables;

import java.util.Iterator;

/**
 * Provides a basic implementation of an {@link Iterator} that calls close when the end of the iterator is reached.
 */
public abstract class AbstractCloseableIterator<T> extends AbstractIterator<T> implements CloseableIterator<T> {

    @Override
    public void closeQuietly() {
        Closeables.closeQuietly(this);
    }
}
