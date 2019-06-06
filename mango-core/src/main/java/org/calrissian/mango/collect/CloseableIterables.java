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

import com.google.common.collect.Iterables;
import com.google.common.collect.Iterators;
import com.google.common.collect.UnmodifiableIterator;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyIterator;
import static java.util.Objects.requireNonNull;

/**
 * Utility class to develop commonly used closeable iterables functions.
 */
public class CloseableIterables {

    private static final CloseableIterable EMPTY_ITERABLE = new CloseableIterable() {
        @Override
        public void close() { }

        @Override
        public Iterator iterator() {
            return emptyIterator();
        }
    };

    private CloseableIterables() {/* private constructor */}

    /**
     * Creates a {@link CloseableIterable} from a standard {@link Stream}.
     */
    public static <T> CloseableIterable<T> fromStream(Stream<T> stream) {
        return wrap(stream::iterator, stream);
    }

    /**
     * Creates a {@link CloseableIterable} from a standard {@link Iterable}. If {@code iterable} is already
     * a {@link CloseableIterable} it will simply be returned as is.
     */
    public static <T> CloseableIterable<T> fromIterable(Iterable<T> iterable) {
        requireNonNull(iterable);
        if (iterable instanceof CloseableIterable) return (CloseableIterable<T>) iterable;

        return new FluentCloseableIterable<T>() {
            @Override
            protected void doClose() {
                if (iterable instanceof AutoCloseable) {
                    try {
                        ((AutoCloseable) iterable).close();
                    } catch (RuntimeException re) {
                        throw re;
                    } catch (Exception e) {
                        throw new IllegalStateException(e);
                    }
                }
            }

            @Override
            protected Iterator<T> retrieveIterator() {
                return iterable.iterator();
            }
        };
    }

    /**
     * If we can assume the closeable iterable is sorted, return the distinct elements.
     * This only works if the data provided is sorted.
     */
    public static <T> CloseableIterable<T> distinct(final CloseableIterable<T> iterable) {
        return wrap(Iterables2.distinct(iterable), iterable);
    }

    /**
     * Divides an iterable into unmodifiable sublists of equivalent elements. The iterable groups elements
     * in consecutive order, forming a new partition when the value from the provided function changes. For example,
     * grouping the iterable {@code [1, 3, 2, 4, 5]} with a function grouping even and odd numbers
     * yields {@code [[1, 3], [2, 4], [5]} all in the original order.
     *
     * <p/>
     * <p>The returned lists implement {@link java.util.RandomAccess}.
     */
    public static <T> CloseableIterable<List<T>> groupBy(final CloseableIterable<? extends T> iterable, final Function<? super T, ?> groupingFunction) {
        return wrap(Iterables2.groupBy(iterable, groupingFunction), iterable);
    }

    /**
     * These set of functions simply delegate to Guava's {@link Iterables} and wrap the
     * result in a {@link CloseableIterable} to retain the ability to close the underlying
     * resource.
     */

    /**
     * Combines multiple iterables into a single closeable iterable.
     * The returned closeable iterable has an iterator that traverses the elements
     * of each iterable in {@code inputs}. The input iterators are not polled until
     * necessary.
     */
    public static <T> CloseableIterable<T> concat(final CloseableIterable<? extends Iterable<? extends T>> inputs) {
        return wrap(Iterables.concat(inputs), inputs);
    }

    /**
     * Returns a closeable iterable whose iterators cycle indefinitely over the elements of
     * {@code iterable}.
     * <p/>
     * <p>That iterator supports {@code remove()} if {@code iterable.iterator()}
     * does. After {@code remove()} is called, subsequent cycles omit the removed
     * element, which is no longer in {@code iterable}. The iterator's
     * {@code hasNext()} method returns {@code true} until {@code iterable} is
     * empty.
     * <p/>
     * <p><b>Warning:</b> Typical uses of the resulting iterator may produce an
     * infinite loop. You should use an explicit {@code break} or be certain that
     * you will eventually remove all the elements.  The close method should be expicitly
     * called when done with iteration.  Tools such as {@code CloseableIterables.autoClose()}
     * will not work.
     */
    public static <T> CloseableIterable<T> cycle(final CloseableIterable<T> iterable) {
        return wrap(Iterables.cycle(iterable), iterable);
    }

    /**
     * Returns all instances of class {@code type} in {@code unfiltered}. The
     * returned closeable iterable has elements whose class is {@code type} or a subclass of
     * {@code type}. The returned iterable's iterator does not support
     * {@code remove()}.
     */
    public static <T> CloseableIterable<T> filter(final CloseableIterable<?> iterable, final Class<T> type) {
        return wrap(Iterables.filter(iterable, type), iterable);
    }

    /**
     * Returns the elements of {@code unfiltered} that satisfy a predicate. The
     * resulting closeable iterable's iterator does not support {@code remove()}.
     */
    public static <T> CloseableIterable<T> filter(final CloseableIterable<T> iterable, final Predicate<? super T> filter) {
        return wrap(Iterables.filter(iterable, filter::test), iterable);
    }

    /**
     * Creates a closeable iterable with the first {@code limitSize} elements of the given
     * closeable iterable. If the original closeable iterable does not contain that many
     * elements, the returned iterator will have the same behavior as the original
     * closeable iterable. The returned closeable iterable's iterator supports {@code remove()}
     * if the original iterator does.
     */
    public static <T> CloseableIterable<T> limit(final CloseableIterable<T> iterable, final int limitSize) {
        return wrap(Iterables.limit(iterable, limitSize), iterable);
    }

    /**
     * Divides a closeable iterable into unmodifiable sublists of the given size, padding
     * the final list with null values if necessary. For example, partitioning
     * a closeable iterable containing {@code [a, b, c, d, e]} with a partition size of 3
     * yields {@code [[a, b, c], [d, e, null]]} -- an outer iterable containing
     * two inner lists of three elements each, all in the original order.
     * <p/>
     * <p>Iterators returned by the returned closeableiterable do not support the {@link
     * Iterator#remove()} method.
     */
    public static <T> CloseableIterable<List<T>> paddedParition(final CloseableIterable<T> iterable, final int size) {
        return wrap(Iterables.paddedPartition(iterable, size), iterable);
    }

    /**
     * Divides a closeable iterable into unmodifiable sublists of the given size (the final
     * iterable may be smaller). For example, partitioning a closeable iterable containing
     * {@code [a, b, c, d, e]} with a partition size of 3 yields {@code
     * [[a, b, c], [d, e]]} -- an outer iterable containing two inner lists of
     * three and two elements, all in the original order.
     * <p/>
     * <p>Iterators returned by the returned iterable do not support the {@link
     * Iterator#remove()} method.
     */
    public static <T> CloseableIterable<List<T>> partition(final CloseableIterable<T> iterable, final int size) {
        return wrap(Iterables.partition(iterable, size), iterable);
    }

    /**
     * Returns a view of {@code iterable} that skips its first
     * {@code numberToSkip} elements. If {@code iterable} contains fewer than
     * {@code numberToSkip} elements, the returned closeableiterable skips all of its
     * elements.
     * <p/>
     * <p>Modifications to the underlying {@link CloseableIterable} before a call to
     * {@code iterator()} are reflected in the returned iterator. That is, the
     * iterator skips the first {@code numberToSkip} elements that exist when the
     * {@code Iterator} is created, not when {@code skip()} is called.
     * <p/>
     * <p>The returned closeableiterable's iterator supports {@code remove()} if the
     * iterator of the underlying iterable supports it. Note that it is
     * <i>not</i> possible to delete the last skipped element by immediately
     * calling {@code remove()} on that iterator, as the {@code Iterator}
     * contract states that a call to {@code remove()} before a call to
     * {@code next()} will throw an {@link IllegalStateException}.
     */
    public static <T> CloseableIterable<T> skip(final CloseableIterable<T> iterable, final int numberToSkip) {
        return wrap(Iterables.skip(iterable, numberToSkip), iterable);
    }

    /**
     * Returns an iterable that applies {@code function} to each element of {@code
     * fromIterable}.
     */
    public static <F, T> CloseableIterable<T> transform(final CloseableIterable<F> iterable, final Function<? super F, ? extends T> function) {
        return wrap(Iterables.transform(iterable, function::apply), iterable);
    }

    /**
     * These are variations of the concat method but grouping closeableiterables into a single closebleiterable.
     */

    /**
     * Combines multiple closeable iterables into a single closeable iterable.
     * The returned closeable iterable has an iterator that traverses the elements
     * of each iterable in {@code inputs}. The input iterators are not polled until
     * necessary.
     */
    @SafeVarargs
    public static <T> CloseableIterable<T> chain(CloseableIterable<? extends T>... iterables) {
        return chain(asList(iterables));
    }

    /**
     * Combines multiple closeable iterables into a single closeable iterable.
     * The returned closeable iterable has an iterator that traverses the elements
     * of each iterable in {@code inputs}. The input iterators are not polled until
     * necessary.
     */
    public static <T> CloseableIterable<T> chain(final Iterable<? extends CloseableIterable<? extends T>> iterables) {
        requireNonNull(iterables);

        //Don't use CloseableIterators here, as Iterables can reiterate over their data
        //and don't want to close it on them.
        return new FluentCloseableIterable<T>() {
            @Override
            protected void doClose() {
                for (CloseableIterable<? extends T> curr : iterables)
                    curr.closeQuietly();
            }

            @Override
            protected Iterator<T> retrieveIterator() {
                return Iterators.concat(iterators(iterables));
            }
        };
    }

    /**
     * These are utility methods specific to CloseableIterables.
     */

    /**
     * Autoclose the {@code iterable} when its iterator is exhausted or if an exception is thrown.
     * <p/>
     * Note that when using this method the order of calls matters. {@code limit()} is an example of one method which can
     * prevent the completion of an iterator.  For instance limit(autoClose(iterable), 1) will not close the
     * resource if there is more than 1 element, but autoClose(limit(iterable, 1)) will close the underlying
     * resource.
     */
    public static <T> CloseableIterable<T> autoClose(final CloseableIterable<? extends T> iterable) {
        requireNonNull(iterable);
        return new FluentCloseableIterable<T>() {
            @Override
            protected void doClose() {
                iterable.close();
            }

            @Override
            protected Iterator<T> retrieveIterator() {
                //autoclose iterator when it is exhausted.
                return CloseableIterators.autoClose(
                        CloseableIterators.wrap(iterable.iterator(), iterable)
                );
            }
        };
    }

    /**
     * Returns an empty closeable iterable
     *
     * <p>The equivalent of this method is {@link
     * java.util.Collections#emptySet()}.
     */
    @SuppressWarnings("unchecked")
    public static <T> CloseableIterable<T> emptyIterable() {
        return EMPTY_ITERABLE;
    }

    /**
     * Returns a closeable iterable containing only {@code value}.
     *
     * <p>The equivalent of this method is {@link
     * java.util.Collections#singleton}.
     */
    public static <T> CloseableIterable<T> singleton(T value) {
        return fromIterable(Collections.singleton(value));
    }

    /**
     * Creates a {@link CloseableIterable} from a standard {@link Iterable}. If {@code iterable} is already
     * a {@link CloseableIterable} it will simply be returned as is.
     *
     * @deprecated Use {@link CloseableIterables#fromIterable(Iterable)}
     */
    @Deprecated
    public static <T> CloseableIterable<T> wrap(final Iterable<T> iterable) {
        return fromIterable(iterable);
    }

    /**
     * Creates a {@link CloseableIterable} from a standard iterable, while closing the provided
     * closeable.
     * <p/>
     * Intentionally left package private.
     */
    static <T> FluentCloseableIterable<T> wrap(final Iterable<T> iterable, final AutoCloseable closeable) {
        return new FluentCloseableIterable<T>() {
            @Override
            protected void doClose() {
                try {
                    closeable.close();
                } catch (RuntimeException re) {
                    throw re;
                } catch (Exception e) {
                    throw new IllegalStateException(e);
                }
            }

            @Override
            protected Iterator<T> retrieveIterator() {
                return iterable.iterator();
            }
        };
    }

    /**
     * Returns an iterator over the iterators of the given iterables.
     */
    private static <T> UnmodifiableIterator<Iterator<? extends T>> iterators(Iterable<? extends CloseableIterable<? extends T>> iterables) {
        final Iterator<? extends Iterable<? extends T>> iterableIterator = iterables.iterator();
        return new UnmodifiableIterator<Iterator<? extends T>>() {
            @Override
            public boolean hasNext() {
                return iterableIterator.hasNext();
            }

            @Override
            public Iterator<? extends T> next() {
                return iterableIterator.next().iterator();
            }
        };
    }
}
