package com.texeltek.mango.collect;

import com.texeltek.mango.collect.CloseableIterator;

/**
 * Date: 1/28/13
 * Time: 2:32 PM
 */
public class ChainedIterableCloseableIterator<T> extends ChainedCloseableIterator<T> {

    public ChainedIterableCloseableIterator(Iterable<? extends CloseableIterator<? extends T>> closeableIterators) {
        this.closeableIterators = closeableIterators.iterator();
    }
}
