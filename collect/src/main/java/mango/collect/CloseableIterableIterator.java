package mango.collect;

import java.io.IOException;
import java.util.Iterator;

public class CloseableIterableIterator<T> extends AbstractCloseableIterator<T> {

    private CloseableIterable<T> closeableIterable;
    private Iterator<T> iterator;

    public CloseableIterableIterator(CloseableIterable<T> closeableIterable) {
        this.closeableIterable = closeableIterable;
        this.iterator = closeableIterable.iterator();
    }

    @Override
    public void close() throws IOException {
        closeableIterable.close();
    }

    @Override
    public boolean hasNext() {
        return iterator.hasNext();
    }

    @Override
    public T next() {
        return iterator.next();
    }

    @Override
    public void remove() {
        iterator.remove();
    }

    @Override
    public Iterator<T> iterator() {
        return closeableIterable.iterator();
    }
}
