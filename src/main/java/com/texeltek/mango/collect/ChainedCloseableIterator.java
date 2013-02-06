package com.texeltek.mango.collect;

import com.texeltek.mango.collect.AbstractCloseableIterator;
import com.texeltek.mango.collect.CloseableIterator;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * This iterator will close the underlying CloseableIterators as they are exhausted to keep from holding resources until
 * the end of the chain.
 *
 * @param <T>
 */
public class ChainedCloseableIterator<T> extends AbstractCloseableIterator<T> {

    protected Iterator<? extends CloseableIterator<? extends T>> closeableIterators;
    private CloseableIterator<? extends T> currentCloseableIterator = CloseableIteratorAdapter.emptyIterator();

    public ChainedCloseableIterator(Collection<? extends CloseableIterator<? extends T>> closeableIterators) {
        this.closeableIterators = closeableIterators.iterator();
    }

    public ChainedCloseableIterator(CloseableIterator<T>... iterators) {
        this(Arrays.asList(iterators));
    }

    @Override
    public boolean hasNext() {
        try {
            while (!currentCloseableIterator.hasNext() && closeableIterators.hasNext()) {
                //autoclose will close when the iterator is exhausted
                currentCloseableIterator = CloseableIterators.autoClose(closeableIterators.next());
            }
            return currentCloseableIterator.hasNext();
        } catch (RuntimeException re) {
            closeQuietly();
            throw re;
        }
    }

    @Override
    public T next() {
        try {
            if (hasNext()) {
                return currentCloseableIterator.next();
            }
        } catch (RuntimeException re) {
            closeQuietly();
            throw re;
        }
        throw new NoSuchElementException();
    }

    @Override
    public void remove() {
        try {
            currentCloseableIterator.remove();
        } catch (RuntimeException re) {
            closeQuietly();
            throw re;
        }
    }

    @Override
    public void close() throws IOException {
        currentCloseableIterator.closeQuietly();
        while (closeableIterators.hasNext()) {
            try {
                closeableIterators.next().closeQuietly();
            } catch (RuntimeException re) {
                //Ignore
            }
        }
    }
}
