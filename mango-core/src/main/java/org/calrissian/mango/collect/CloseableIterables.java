/*
 * Copyright (C) 2013 The Calrissian Authors
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

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Iterators;
import com.google.common.collect.UnmodifiableIterator;

import java.io.Closeable;
import java.io.IOException;
import java.util.Iterator;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.Arrays.asList;

/**
 * Utility class to develop commonly used closeable iterables functions.
 */
public class CloseableIterables {

    public CloseableIterables() {/* private constructor */}

    /**
     * Returns an iterable that applies {@code function} to each element of {@code
     * fromIterable}.
     */
    public static <F, T> CloseableIterable<T> transform(final CloseableIterable<F> iterable, final Function<? super F, ? extends T> function) {
        return wrap(Iterables.transform(iterable, function), iterable);
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
     * Returns the elements of {@code unfiltered} that satisfy a predicate. The
     * resulting closeable iterable's iterator does not support {@code remove()}.
     */
    public static <T> CloseableIterable<T> filter(final CloseableIterable<T> iterable, final Predicate<? super T> filter) {
        return wrap(Iterables.filter(iterable, filter), iterable);
    }

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
     * Combines multiple closeable iterables into a single closeable iterable.
     * The returned closeable iterable has an iterator that traverses the elements
     * of each iterable in {@code inputs}. The input iterators are not polled until
     * necessary.
     */
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
        checkNotNull(iterables);

        //Don't use CloseableIterators here, as Iterables can reiterate over their data
        //and don't want to close it on them.
        return new FluentCloseableIterable<T>() {
            @Override
            protected void doClose() throws IOException {
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
     * If we can assume the closeable iterable is sorted, return the distinct elements.
     * This only works if the data provided is sorted.
     */
    public static <T> CloseableIterable<T> distinct(final CloseableIterable<T> iterable) {
        return wrap(Iterables2.distinct(iterable), iterable);
    }

    /**
     * Autoclose the iterator when exhausted or if an exception is thrown.
     */
    public static <T> CloseableIterable<T> autoClose(final CloseableIterable<? extends T> iterable) {
        checkNotNull(iterable);
        return new FluentCloseableIterable<T>() {
            @Override
            protected void doClose() throws IOException {
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
     * Creates a {@link CloseableIterable} from a standard iterable
     */
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
     * Creates a {@link CloseableIterable} from a standard iterable, while closing the provided
     * closeable.
     *
     * Intentionally left package private.
     */
    static <T> FluentCloseableIterable<T> wrap(final Iterable<T> iterable, final Closeable closeable) {
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
