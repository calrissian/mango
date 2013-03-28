package org.calrissian.mango.collect;

import java.io.IOException;
import java.util.NoSuchElementException;

/**
 * Custom iterator implementation (decorator) that can be used as a wrapper for an existing
 * iterator. Provides additional functionality from standard iterators so that a
 * limit of returned items can be defined.
 *
 * For example, if your iterator iterates over 1 billion elements, but you create
 * a LimitCloseableIterator like <code>new LimitCloseableIterator(myIterator, 10)</code>
 * a call to hasNext would only be true for the first 10 calls to <code>.next()</code>
 */
public class LimitCloseableIterator<E> extends AbstractCloseableIterator<E> {
    private final CloseableIterator<E> iterator;
    private final int limit;
    private int count = 0;

    /**
     * Constructs a new LimitCloseableIterator that wraps another iterator and specifies the max number of iterations over the iterator.
     *
     * @param iterator Iterator that will be limited.
     * @param limit Max number of iterations allowed.
     *
     * @throws {@link IllegalArgumentException} if limit is less than zero.
     */
    public LimitCloseableIterator(CloseableIterator<E> iterator, int limit) {
        if (limit < 0) {
            throw new IllegalArgumentException("limit cannot be less than zero.");
        }
        this.iterator = iterator;
        this.limit = limit;
    }

    /** @inheritDoc
     */
    @Override
    public boolean hasNext() {
        return count < limit && iterator.hasNext();
    }

    /** @inheritDoc
     */
    @Override
    public E next() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }
        count++;
        return iterator.next();
    }

    /** @inheritDoc
     */
    @Override
    public void remove() {
        iterator.remove();
    }

    @Override
    public void close() throws IOException {
        iterator.close();
    }
}
