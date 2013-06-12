package org.calrissian.mango.collect;

import com.google.common.collect.AbstractIterator;
import com.google.common.io.Closeables;

import java.util.Iterator;

/**
 * This class is an extension of the {@link AbstractIterator} class which provides additional
 * support for closing the {@link Iterator} quietly.
 */
public abstract class AbstractCloseableIterator<T> extends AbstractIterator<T> implements CloseableIterator<T> {

    /**
     * {@inheritDoc}
     */
    @Override
    public void closeQuietly() {
        Closeables.closeQuietly(this);
    }
}
