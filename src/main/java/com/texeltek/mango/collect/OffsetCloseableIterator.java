package com.texeltek.mango.collect;

import com.texeltek.mango.collect.AbstractCloseableIterator;
import com.texeltek.mango.collect.CloseableIterator;

import java.io.IOException;

/**
 * This iterator advances the passed in iterator to the offset, and then
 * delegates to the passed in iterator for all functionality
 * @param <T>
 *            type of object to iterator over
 */
public class OffsetCloseableIterator<T> extends AbstractCloseableIterator<T> {
    
    private CloseableIterator<T> wrappedIterator;
    
    public OffsetCloseableIterator(final CloseableIterator<T> iterator, final long offset) {
        if (offset < 0) {
            throw new IllegalArgumentException("offset must be greater than or equal to zero");
        }
        this.wrappedIterator = iterator;
        
        /* seek to first element after the offset */
        for (int i = 0; i < offset; i++) {
            if (wrappedIterator.hasNext()) {
                wrappedIterator.next();
            } else {
                /* offset is larger than the number of elements left in the iterator */
                break;
            }
        }
    }

    @Override
    public boolean hasNext() {
        return wrappedIterator.hasNext();
    }

    @Override
    public T next() {
        return wrappedIterator.next();
    }

    @Override
    public void remove() {
        wrappedIterator.remove();
    }

    @Override
    public void close() throws IOException {
        wrappedIterator.close();
    }
}
