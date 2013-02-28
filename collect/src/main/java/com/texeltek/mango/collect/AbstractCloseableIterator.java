package com.texeltek.mango.collect;

import java.util.Iterator;

/**
 * Provides a basic implementation of an {@link Iterator} that calls close when the end of the iterator is reached.
 */
public abstract class AbstractCloseableIterator<T> implements CloseableIterator<T> {

    @Override
    public void closeQuietly() {
        try{
            this.close();
        } catch(Exception e) {
            //ignore
        }
    }

    @Override
    public Iterator<T> iterator() {
        return this;
    }
}
