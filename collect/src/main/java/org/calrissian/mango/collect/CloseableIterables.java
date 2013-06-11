package org.calrissian.mango.collect;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.UnmodifiableIterator;

import java.io.Closeable;
import java.io.IOException;
import java.util.Iterator;
import java.util.NoSuchElementException;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Helper class to develop commonly used Iterables
 */
public class CloseableIterables {

    public static <F, T> CloseableIterable<T> transform(final CloseableIterable<F> iterable, final Function<? super F, ? extends T> function) {
        return consumeClose(Iterables.transform(iterable, function), iterable);
    }

    public static <T> CloseableIterable<T> limit(final CloseableIterable<T> iterable, final int limitSize) {
        return consumeClose(Iterables.limit(iterable, limitSize), iterable);
    }

    public static <T> CloseableIterable<T> filter(final CloseableIterable<T> iterable, final Predicate<? super T> filter) {
        return consumeClose(Iterables.filter(iterable, filter), iterable);
    }

    public static <T> CloseableIterable<T> concat(final CloseableIterable<? extends Iterable<? extends T>> inputs) {
        return consumeClose(Iterables.concat(inputs), inputs);
    }

    public static <T> CloseableIterable<T> chain(final Iterable<? extends CloseableIterable<? extends T>> iterables) {
        checkNotNull(iterables);
        return new FluentCloseableIterable<T>() {
            CloseableIterator<T> curr = CloseableIterators.emptyIterator();
            @Override
            protected void doClose() throws IOException {
                curr.close();
            }

            @Override
            protected Iterator<T> retrieveIterator() {
                curr = CloseableIterators.chain(iterators(iterables));
                return curr;
            }
        };
    }

    public static <T> CloseableIterable<T> wrap(final Iterable<T> iterable) {
        checkNotNull(iterable);
        if (iterable instanceof CloseableIterable) return (CloseableIterable<T>) iterable;

        return new FluentCloseableIterable<T>() {
            @Override
            protected void doClose() throws IOException {
                if (iterable instanceof Closeable)
                    ((Closeable)iterable).close();
            }

            @Override
            protected Iterator<T> retrieveIterator() {
                return iterable.iterator();
            }
        };
    }

    /**
     * If we can assume the iterable is sorted, return the distinct elements. This only works
     * if the data comes back sorted.
     */
    public static <T> CloseableIterable<T> distinct(final CloseableIterable<T> iterable) {
        return consumeClose(Iterables2.distinct(iterable), iterable);
    }

    /**
     * Autoclose the iterator when exhausted or if an exception is thrown. It is currently set to protected, so that only
     * classes in this package can use.
     *
     * @param iterable
     * @param <T>
     * @return
     */
    static <T> CloseableIterable<T> autoClose(final CloseableIterable<? extends T> iterable) {
        checkNotNull(iterable);
        return new AbstractCloseableIterable<T>() {

            private boolean closed = false;

            @Override
            protected void doClose() throws IOException {
                if (closed)
                    return;

                closed = true;
                iterable.close();
            }

            @Override
            protected Iterator<T> retrieveIterator() {
                //autoclose when this iterator is consumed or an exception occurs
                final Iterator<? extends T> iterator = iterable.iterator();
                return new Iterator<T>() {
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
                            } finally {
                                //if no more are left, hasNext will close the stream
                                hasNext();
                            }
                        }
                        throw new NoSuchElementException();
                    }

                    @Override
                    public void remove() {
                        if (hasNext()) {
                            try {
                                iterator.remove();
                            } catch (RuntimeException re) {
                                closeQuietly();
                                throw re;
                            }
                        }
                    }
                };
            }
        };
    }

    /**
     * Return a {@link CloseableIterable} that will consume from the first argument Iterator, and close the second Closeable on close
     *
     * @param iterable
     * @param closeable
     * @return
     */
    static <T> FluentCloseableIterable<T> consumeClose(final Iterable<T> iterable, final Closeable closeable) {
        return new FluentCloseableIterable<T>() {

            @Override
            protected void doClose() throws IOException {
                closeable.close();
            }

            @Override
            protected Iterator<T> retrieveIterator() {
                return iterable.iterator();
            }
        };
    }

    private static <T> Iterator<? extends CloseableIterator<? extends T>> iterators(Iterable<? extends CloseableIterable<? extends T>> inputs) {
        final Iterator<? extends CloseableIterable<? extends T>> iterator = inputs.iterator();
        return new UnmodifiableIterator<CloseableIterator<? extends T>>() {
            @Override
            public boolean hasNext() {
                return iterator.hasNext();
            }

            @Override
            public CloseableIterator<? extends T> next() {
                CloseableIterable<? extends T> internal = iterator.next();
                return CloseableIterators.consumeClose(internal.iterator(), internal);
            }
        };
    }
}
