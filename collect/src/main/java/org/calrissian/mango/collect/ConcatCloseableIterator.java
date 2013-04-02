package org.calrissian.mango.collect;

import java.io.IOException;
import java.util.List;
public class ConcatCloseableIterator<T> extends AbstractCloseableIterator<T> {

    private List<CloseableIterator<T>> iterators;

    public ConcatCloseableIterator(List<CloseableIterator<T>> iterators) {
        this.iterators = iterators;
    }

    @Override
    public void close() throws IOException {
        for (CloseableIterator iter : iterators) {
            iter.close();
        }
    }

    @Override
    public boolean hasNext() {
        if (iterators.isEmpty()) return false;
        CloseableIterator<T> current = iterators.get(0);
        if (!current.hasNext()) {
            iterators.remove(0);
            return hasNext();
        }
        return true;
    }

    @Override
    public T next() {
        if (iterators.isEmpty()) return null;
        CloseableIterator<T> current = iterators.get(0);
        if (!current.hasNext()) {
            iterators.remove(0);
            return next();
        }
        return current.next();
    }

    @Override
    public void remove() {
        next();
    }
}
