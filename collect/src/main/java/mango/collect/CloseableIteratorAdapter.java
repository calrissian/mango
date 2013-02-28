package mango.collect;

import com.google.common.collect.Iterators;

import java.io.Closeable;
import java.io.IOException;
import java.util.Iterator;

/**
 * Custom {@link Iterator} (decorator) that converts an iterator into a {@link CloseableIterator}.
 */
public class CloseableIteratorAdapter<T> extends AbstractCloseableIterator<T> {

    private Iterator<T> iterator;

    @SuppressWarnings("unchecked")
    public static <T> CloseableIterator<T> emptyIterator() {
        return new CloseableIteratorAdapter<T>((Iterator<T>) Iterators.emptyIterator());
    }

    public static <T> CloseableIterator<T> wrap(Iterator<T> iterator) {
        if (iterator instanceof CloseableIterator) {
            return (CloseableIterator<T>) iterator;
        } else {
            return new CloseableIteratorAdapter<T>(iterator);
        }
    }

    /**
     * Return a {@link CloseableIterator} that will consume from the first argument Iterator, and close the second Closeable on close
     *
     * @param consumeFrom
     * @param closeable
     * @param <T>
     * @return
     */
    static <T> CloseableIterator<T> wrap(final Iterator<T> consumeFrom, final Closeable closeable) {
        return new AbstractCloseableIterator<T>() {
            @Override
            public void close() throws IOException {
                closeable.close();
            }

            @Override
            public boolean hasNext() {
                return consumeFrom.hasNext();
            }

            @Override
            public T next() {
                return consumeFrom.next();
            }

            @Override
            public void remove() {
                consumeFrom.remove();
            }
        };
    }

    private CloseableIteratorAdapter(Iterator<T> iterator) {
        this.iterator = iterator;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public T next() {
        return iterator.next();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void close() throws IOException {
        if (iterator instanceof Closeable) {
            ((Closeable) iterator).close();
        } // else do nothing
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasNext() {
        return iterator.hasNext();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void remove() {
        iterator.remove();
    }
}
