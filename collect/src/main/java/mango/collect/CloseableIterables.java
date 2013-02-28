package mango.collect;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;

import java.io.Closeable;
import java.io.IOException;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Helper class to develop commonly used Iterables
 */
public class CloseableIterables {

    public static <F, T> FluentCloseableIterable<T> transform(final CloseableIterable<F> iterable, final Function<? super F, ? extends T> function) {
        return consumeClose(Iterables.transform(iterable, function), iterable);
    }

    public static <T> FluentCloseableIterable<T> limit(final CloseableIterable<T> iterable, final int limitSize) {
        return consumeClose(Iterables.limit(iterable, limitSize), iterable);
    }

    public static <T> FluentCloseableIterable<T> filter(final CloseableIterable<T> iterable, final Predicate<? super T> filter) {
        return consumeClose(Iterables.filter(iterable, filter), iterable);
    }

    public static <T> FluentCloseableIterable<T> concat(final CloseableIterable<? extends Iterable<? extends T>> inputs) {
        return consumeClose(Iterables.concat(inputs), inputs);
    }

    public static <T> CloseableIterable<T> wrap(final Iterable<T> iterable) {
        if (iterable instanceof CloseableIterable) return (CloseableIterable<T>) iterable;

        return new AbstractCloseableIterable<T>() {
            @Override
            protected void doClose() throws IOException {
            }

            @Override
            protected Iterator<T> retrieveIterator() {
                return iterable.iterator();
            }
        };
    }

//    /**
//     * Concat an Iterable of CloseableIterators. This will allow us to concat any Collection of CloseableIterators.
//     *
//     * @param iterable
//     * @param <T>
//     * @return
//     */
//    public static <T> CloseableIterator<T> chain(final Iterable<? extends CloseableIterator<? extends T>> iterable) {
//        return new ChainedIterableCloseableIterator<T>(iterable);
//    }
//

    /**
     * Autoclose the iterator when exhausted or if an exception is thrown. It is currently set to protected, so that only
     * classes in this package can use.
     *
     * @param iterable
     * @param <T>
     * @return
     */
    static <T> CloseableIterable<T> autoClose(final CloseableIterable<? extends T> iterable) {
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
     *
     * @param consumeFrom
     * @param closeable
     * @return
     */
    static <T> FluentCloseableIterable<T> consumeClose(final Iterable<T> consumeFrom, final Closeable closeable) {
        return new FluentCloseableIterable<T>() {

            @Override
            protected void doClose() throws IOException {
                closeable.close();
            }

            @Override
            protected Iterator<T> retrieveIterator() {
                return consumeFrom.iterator();
            }
        };
    }
}
