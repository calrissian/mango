package org.calrissian.mango.collect;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterators;
import com.google.common.collect.PeekingIterator;
import com.google.common.io.Closeables;

import java.io.Closeable;
import java.io.IOException;
import java.util.Iterator;
import java.util.NoSuchElementException;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Date: 8/29/12
 * Time: 10:02 AM
 */
public class CloseableIterators {

    public static <F, T> CloseableIterator<T> transform(CloseableIterator<F> iterator, Function<F, T> function) {
        return consumeClose(Iterators.transform(iterator, function), iterator);
    }


    public static <T> PeekingCloseableIterator<T> peekingIterator(final CloseableIterator<T> iterator) {
        final PeekingIterator<T> peeking = Iterators.peekingIterator(iterator);
        return new PeekingCloseableIterator<T>() {
            @Override
            public void closeQuietly() {
                iterator.closeQuietly();
            }

            @Override
            public void close() throws IOException {
                iterator.close();
            }

            @Override
            public T peek() {
                return peeking.peek();
            }

            @Override
            public T next() {
                return peeking.next();
            }

            @Override
            public void remove() {
                peeking.remove();
            }

            @Override
            public boolean hasNext() {
                return peeking.hasNext();
            }
        };
    }

    public static <T> CloseableIterator<T> limit(CloseableIterator<T> iterator, int limitSize) {
        return consumeClose(Iterators.limit(iterator, limitSize), iterator);
    }

    public static <T> CloseableIterator<T> filter(CloseableIterator<T> iterator, Predicate<T> filter) {
        return consumeClose(Iterators.filter(iterator, filter), iterator);
    }

    public static <T> int advance (CloseableIterator<T> iterator, int numberToAdvance) {
        return Iterators.advance(iterator, numberToAdvance);
    }

    public static <T> CloseableIterator<T> emptyIterator() {
        return wrap(Iterators.<T>emptyIterator());
    }

    public static <T> CloseableIterator<T> concat(CloseableIterator<? extends Iterator<? extends T>> iterators) {
        return consumeClose(Iterators.concat(iterators), iterators);
    }


    public static <T> CloseableIterator<T> chain(CloseableIterator<? extends T>... iterators) {
        return chain(Iterators.forArray(iterators));
    }

    public static <T> CloseableIterator<T> chain(final Iterator<? extends CloseableIterator<? extends T>> iterator) {
        checkNotNull(iterator);
        return new CloseableIterator<T>() {
            CloseableIterator<? extends T> curr = emptyIterator();
            @Override
            public void closeQuietly() {
                Closeables.closeQuietly(this);
            }

            @Override
            public void close() throws IOException {
                //Close the current one then close all the others
                if (curr != null)
                    curr.closeQuietly();

                while (iterator.hasNext())
                    iterator.next().closeQuietly();

            }

            @Override
            public boolean hasNext() {
                //autoclose will close when the iterator is exhausted
                while (!curr.hasNext() && iterator.hasNext())
                    curr = iterator.next();

                return curr.hasNext();
            }

            @Override
            public T next() {
                if (hasNext())
                    return curr.next();

                throw new NoSuchElementException();
            }

            @Override
            public void remove() {
                curr.remove();
            }
        };
    }

    /**
     * Concat an Iterable of CloseableIterators. This will allow us to chain any Collection of CloseableIterators.
     *
     * @param iterable
     * @param <T>
     * @return
     */
    public static <T> CloseableIterator<T> chain(Iterable<? extends CloseableIterator<? extends T>> iterable) {
        return chain(iterable.iterator());
    }

    public static <T> CloseableIterator<T> sortedDistinct(final CloseableIterator<T> iterator) {
        checkNotNull(iterator);
        return new AbstractCloseableIterator<T>() {
            T current = null;

            @Override
            protected T computeNext() {
                if (iterator.hasNext()) {
                    if (current == null) {
                        current = iterator.next();
                        return current;
                    } else {
                        T next = iterator.next();
                        while (current.equals(next)) {
                            if (iterator.hasNext()) {
                                next = iterator.next();
                            } else {
                                return endOfData();
                            }
                        }
                        current = next;
                        return current;
                    }
                } else
                    return endOfData();
            }

            @Override
            public void close() throws IOException {
                iterator.close();
            }
        };
    }

    public static <T> CloseableIterator<T> wrap(final Iterator<T> iterator) {
        checkNotNull(iterator);
        if (iterator instanceof CloseableIterator) return (CloseableIterator<T>) iterator;

        return new CloseableIterator<T>() {

            @Override
            public void closeQuietly() {
                Closeables.closeQuietly(this);
            }

            @Override
            public void close() throws IOException {
                if (iterator instanceof Closeable)
                    ((Closeable) iterator).close();
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
        };
    }

    /**
     * Autoclose the iterator when exhausted or if an exception is thrown. It is currently set to protected, so that only
     * classes in this package can use.
     *
     * @param iterator
     * @param <T>
     * @return
     */
    static <T> CloseableIterator<T> autoClose(final CloseableIterator<? extends T> iterator) {
        checkNotNull(iterator);
        return new CloseableIterator<T>() {
            private boolean closed = false;
            @Override
            public void closeQuietly() {
                Closeables.closeQuietly(this);
            }

            @Override
            public void close() throws IOException {
                if (closed)
                    return;

                closed = true;
                iterator.close();
            }

            @Override
            public boolean hasNext() {
                try {
                    if (closed)
                        return false;
                    if (!iterator.hasNext()) {
                        closeQuietly();
                        return false;
                    }
                    return true;
                } catch (RuntimeException re) {
                    closeQuietly();
                    throw re;
                }
            }

            @Override
            public T next() {
                if (hasNext()) {
                    try {
                        return iterator.next();
                    } catch (RuntimeException re) {
                        closeQuietly();
                        throw re;
                    }
                }
                throw new NoSuchElementException();
            }

            @Override
            public void remove() {
                try {
                    if (hasNext()) {
                        iterator.remove();
                    }
                } catch (RuntimeException re) {
                    closeQuietly();
                    throw re;
                }
            }
        };

    }

    static <T> CloseableIterator<T> consumeClose(final Iterator<T> iterator, final Closeable closeable) {
        return new CloseableIterator<T>() {

            @Override
            public void closeQuietly() {
                Closeables.closeQuietly(this);
            }

            @Override
            public void close() throws IOException {
                closeable.close();
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
        };
    }
}
