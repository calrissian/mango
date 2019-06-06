/*
 * Copyright (C) 2019 The Calrissian Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.calrissian.mango.collect;

import com.google.common.collect.Iterators;
import com.google.common.collect.PeekingIterator;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static java.util.Objects.requireNonNull;

/**
 * Utility class to develop commonly used closeable iterator functions.
 */
public class CloseableIterators {

    @SuppressWarnings("rawtypes")
    private static final CloseableIterator EMPTY_ITERATOR = fromIterator(Collections.emptyIterator());

    private CloseableIterators() {/* private constructor */}

    /**
     * Creates a {@link CloseableIterable} from a standard {@link Stream}.
     */
    public static <T> CloseableIterator<T> fromStream(Stream<T> stream) {
        return wrap(stream.iterator(), stream);
    }

    /**
     * Creates a {@link CloseableIterator} from a standard {@link Iterator}. If {@code iterator} is already
     * a {@link CloseableIterator} it will simply be returned as is.
     */
    public static <T> CloseableIterator<T> fromIterator(Iterator<T> iterator) {
        requireNonNull(iterator);
        if (iterator instanceof CloseableIterator) return (CloseableIterator<T>) iterator;

        return new CloseableIterator<T>() {
            @Override
            public void close() {
                if (iterator instanceof AutoCloseable) {
                    try {
                        ((AutoCloseable) iterator).close();
                    } catch (RuntimeException re) {
                        throw re;
                    } catch (Exception e) {
                        throw new IllegalStateException(e);
                    }
                }
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
     * If we can assume the closeable iterator is sorted, return the distinct elements.
     * This only works if the data provided is sorted.
     */
    public static <T> CloseableIterator<T> distinct(final CloseableIterator<T> iterator) {
        return wrap(Iterators2.distinct(iterator), iterator);
    }

    /**
     * Divides a closeableiterator into unmodifiable sublists of equivalent elements. The iterator groups elements
     * in consecutive order, forming a new partition when the value from the provided function changes. For example,
     * grouping the iterator {@code [1, 3, 2, 4, 5]} with a function grouping even and odd numbers
     * yields {@code [[1, 3], [2, 4], [5]} all in the original order.
     *
     * <p/>
     * <p>The returned lists implement {@link java.util.RandomAccess}.
     */
    public static <T> CloseableIterator<List<T>> groupBy(final CloseableIterator<? extends T> iterator, final Function<? super T, ?> groupingFunction) {
        return wrap(Iterators2.groupBy(iterator, groupingFunction), iterator);
    }

    /**
     * These set of functions simply delegate to Guava's {@link com.google.common.collect.Iterators} and wrap the
     * result in a {@link CloseableIterator} to retain the ability to close the underlying
     * resource.
     */

    /**
     * Combines multiple iterators into a single closeable iterator. The returned
     * closeable iterator iterates across the elements of each iterator in {@code inputs}.
     * The input iterators are not polled until necessary.
     */
    public static <T> CloseableIterator<T> concat(final CloseableIterator<? extends Iterator<? extends T>> iterators) {
        return wrap(Iterators.concat(iterators), iterators);
    }

    /**
     * Returns an empty closeable iterator.
     */
    @SuppressWarnings("unchecked")
    public static <T> CloseableIterator<T> emptyIterator() {
        return (CloseableIterator<T>) EMPTY_ITERATOR;
    }

    /**
     * Returns the elements of {@code unfiltered} that satisfy a predicate.
     */
    public static <T> CloseableIterator<T> filter(final CloseableIterator<T> iterator, final Predicate<T> filter) {
        return wrap(Iterators.filter(iterator, filter::test), iterator);
    }

    /**
     * Returns all instances of class {@code type} in {@code unfiltered}. The
     * returned closeable iterator has elements whose class is {@code type} or a subclass of
     * {@code type}.
     */
    public static <T> CloseableIterator<T> filter(final CloseableIterator<?> iterator, final Class<T> type) {
        return wrap(Iterators.filter(iterator, type), iterator);
    }

    /**
     * Creates a closeable iterator returning the first {@code limitSize} elements of the
     * given closeable iterator. If the original closeable iterator does not contain that many
     * elements, the returned closeable iterator will have the same behavior as the original.
     */
    public static <T> CloseableIterator<T> limit(final CloseableIterator<T> iterator, final int limitSize) {
        return wrap(Iterators.limit(iterator, limitSize), iterator);
    }

    /**
     * Divides a closeableiterator into unmodifiable sublists of the given size, padding
     * the final iterator with null values if necessary. For example, partitioning
     * a closeable iterator containing {@code [a, b, c, d, e]} with a partition size of 3
     * yields {@code [[a, b, c], [d, e, null]]} -- an outer iterator containing
     * two inner lists of three elements each, all in the original order.
     * <p/>
     * <p>The returned lists implement {@link java.util.RandomAccess}.
     */
    public static <T> CloseableIterator<List<T>> paddedParition(final CloseableIterator<T> iterator, final int size) {
        return wrap(Iterators.paddedPartition(iterator, size), iterator);
    }

    /**
     * Divides a closeableiterator into unmodifiable sublists of the given size (the final
     * list may be smaller). For example, partitioning a closeableiterator containing
     * {@code [a, b, c, d, e]} with a partition size of 3 yields {@code
     * [[a, b, c], [d, e]]} -- an outer iterator containing two inner lists of
     * three and two elements, all in the original order.
     * <p/>
     * <p>The returned lists implement {@link java.util.RandomAccess}.
     */
    public static <T> CloseableIterator<List<T>> partition(final CloseableIterator<T> iterator, final int size) {
        return wrap(Iterators.partition(iterator, size), iterator);
    }

    /**
     * Returns a {@code PeekingCloseableIterator} backed by the given closeable iterator.
     * <p/>
     * Calls to peek do not change the state of the iterator.  The subsequent call to next
     * after peeking will always return the same value.
     */
    public static <T> PeekingCloseableIterator<T> peekingIterator(final CloseableIterator<T> iterator) {
        final PeekingIterator<T> peeking = Iterators.peekingIterator(iterator);
        return new PeekingCloseableIterator<T>() {
            @Override
            public void close() {
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

    /**
     * Returns an iterator containing only {@code value}.
     *
     * <p>The {@link Iterable} equivalent of this method is {@link
     * java.util.Collections#singleton}.
     */
    public static <T> CloseableIterator<T> singletonIterator(T value) {
        return fromIterator(Iterators.singletonIterator(value));
    }

    /**
     * Returns a closeable iterator that applies {@code function} to each element of {@code
     * fromIterator}.
     */
    public static <F, T> CloseableIterator<T> transform(CloseableIterator<F> iterator, Function<F, T> function) {
        return wrap(Iterators.transform(iterator, function::apply), iterator);
    }

    /**
     * These are variations of the concat method but grouping closeableiterables into a single closebleiterable.
     */

    /**
     * Combines multiple closeable iterators into a single closeable iterator. The returned
     * closeable iterator iterates across the elements of each closeable iterator in {@code inputs}.
     * The input iterators are not polled until necessary.
     * <p/>
     * As each closeable iterator is exhausted, it is closed before moving onto the next closeable
     * iterator.  A call to close on the returned closeable iterator will quietly close all of
     * the closeable iterators in {@code inputs} which have not been exhausted.
     */
    @SafeVarargs
    public static <T> CloseableIterator<T> chain(CloseableIterator<? extends T>... iterators) {
        return chain(Iterators.forArray(iterators));
    }

    /**
     * Combines multiple closeable iterators into a single closeable iterator. The returned
     * closeable iterator iterates across the elements of each closeable iterator in {@code inputs}.
     * The input iterators are not polled until necessary.
     * <p/>
     * As each closeable iterator is exhausted, it is closed before moving onto the next closeable
     * iterator.  A call to close on the returned closeable iterator will quietly close all of
     * the closeable iterators in {@code inputs} which have not been exhausted.
     */
    public static <T> CloseableIterator<T> chain(final Iterator<? extends CloseableIterator<? extends T>> iterator) {
        requireNonNull(iterator);
        return new CloseableIterator<T>() {
            CloseableIterator<? extends T> curr = emptyIterator();

            @Override
            public void close() {
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
                    curr = autoClose(iterator.next());

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
     * These are utility methods specific to CloseableIterators.
     */

    /**
     * Autoclose the iterator when exhausted or if an exception is thrown.
     * <p/>
     * Note that when using this method the order of calls matters. {@code limit()} is an example of one method which can
     * prevent the completion of an iterator.  For instance limit(autoClose(iterator), 1) will not close the
     * resource if there is more than 1 element, but autoClose(limit(iterator, 1)) will close the underlying
     * resource.
     */
    public static <T> CloseableIterator<T> autoClose(final CloseableIterator<? extends T> iterator) {
        requireNonNull(iterator);
        return new CloseableIterator<T>() {
            private boolean closed = false;

            @Override
            public void close() {
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
                if (!closed) {
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
                if (!closed) {
                    try {
                        iterator.remove();
                    } catch (RuntimeException re) {
                        closeQuietly();
                        throw re;
                    }
                }
                throw new IllegalStateException();
            }
        };
    }

    /**
     * Creates a {@link CloseableIterator} from a standard {@link Iterator}. If {@code iterator} is already
     * a {@link CloseableIterator} it will simply be returned as is.
     *
     * @deprecated Use {@link CloseableIterators#fromIterator(Iterator)}
     */
    @Deprecated
    public static <T> CloseableIterator<T> wrap(final Iterator<T> iterator) {
        return fromIterator(iterator);
    }

    /**
     * Creates a {@link CloseableIterable} from a standard iterable, while closing the provided
     * closeable.
     * <p/>
     * Intentionally left package private.
     */
    static <T> CloseableIterator<T> wrap(final Iterator<T> iterator, final AutoCloseable closeable) {
        return new CloseableIterator<T>() {

            @Override
            public void close() {
                try {
                    closeable.close();
                } catch (RuntimeException re) {
                    throw re;
                } catch (Exception e) {
                    throw new IllegalStateException(e);
                }
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
