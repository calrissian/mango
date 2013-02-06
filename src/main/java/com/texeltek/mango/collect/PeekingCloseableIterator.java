package com.texeltek.mango.collect;

import com.google.common.base.Preconditions;
import com.texeltek.mango.collect.AbstractCloseableIterator;
import com.texeltek.mango.collect.CloseableIterator;

import java.io.IOException;

/**
 * Date: 7/24/12
 * Time: 4:40 PM
 */
public class PeekingCloseableIterator<T> extends AbstractCloseableIterator<T> {

    private final CloseableIterator<T> iter;
    private boolean hasPeeked;
    private T peekedElement;

    public PeekingCloseableIterator(CloseableIterator<T> iter) {
        this.iter = Preconditions.checkNotNull(iter);
    }

    @Override
    public void close() throws IOException {
        iter.close();
    }

    public boolean hasNext() {
        return hasPeeked || iter.hasNext();
    }

    public T next() {
        if (!hasPeeked) {
            return iter.next();
        } else {
            T result = peekedElement;
            hasPeeked = false;
            peekedElement = null;
            return result;
        }
    }

    public void remove() {
        Preconditions.checkState(!hasPeeked, "Can't remove after you've peeked at next");
        iter.remove();
    }

    public T peek() {
        if (!hasPeeked) {
            if(iter.hasNext()) {
                peekedElement = iter.next();
            } else {
                peekedElement = null;
            }
            if (peekedElement != null) {
                hasPeeked = true;
            }
        }
        return peekedElement;
    }

}
