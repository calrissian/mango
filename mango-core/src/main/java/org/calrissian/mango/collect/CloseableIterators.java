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
import com.google.common.collect.Iterators;
import com.google.common.collect.PeekingIterator;
import com.google.common.io.Closeables;

import java.io.Closeable;
import java.io.IOException;
import java.util.Iterator;
import java.util.NoSuchElementException;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Utility class to develop commonly used closeable iterator functions.
 */
public class CloseableIterators {

    private CloseableIterators() {/* private constructor */}

    private static final CloseableIterator EMPTY_ITERATOR = wrap(Iterators.emptyIterator());

    /**
     * Returns a closeable iterator that applies {@code function} to each element of {@code
     * fromIterator}.
     */
    public static <F, T> CloseableIterator<T> transform(CloseableIterator<F> iterator, Function<F, T> function) {
        return wrap(Iterators.transform(iterator, function), iterator);
    }

    /**
     * /**
     * Returns a {@code PeekingCloseableIterator} backed by the given closeable iterator.
     *
     * Calls to peek do not change the state of the iterator.  The subsequent call to next
     * after peeking will always return the same value.
     */
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

    /**
     * Creates a closeable iterator returning the first {@code limitSize} elements of the
     * given closeable iterator. If the original closeable iterator does not contain that many
     * elements, the returned closeable iterator will have the same behavior as the original.
     */
    public static <T> CloseableIterator<T> limit(CloseableIterator<T> iterator, int limitSize) {
        return wrap(Iterators.limit(iterator, limitSize), iterator);
    }

    /**
     * Returns the elements of {@code unfiltered} that satisfy a predicate.
     */
    public static <T> CloseableIterator<T> filter(CloseableIterator<T> iterator, Predicate<T> filter) {
        return wrap(Iterators.filter(iterator, filter), iterator);
    }

    /**
     * Calls {@code next()} on {@code iterator}, either {@code numberToAdvance} times
     * or until {@code hasNext()} returns {@code false}, whichever comes first.
     */
    public static <T> int advance (CloseableIterator<T> iterator, int numberToAdvance) {
        return Iterators.advance(iterator, numberToAdvance);
    }

    /**
     * Returns an empty closeable iterator.
     */
    @SuppressWarnings("unchecked")
    public static <T> CloseableIterator<T> emptyIterator() {
        return (CloseableIterator<T>)EMPTY_ITERATOR;
    }

    /**
     * Combines multiple iterators into a single closeable iterator. The returned
     * closeable iterator iterates across the elements of each iterator in {@code inputs}.
     * The input iterators are not polled until necessary.
     * @param iterators
     * @param <T>
     * @return
     */
    public static <T> CloseableIterator<T> concat(CloseableIterator<? extends Iterator<? extends T>> iterators) {
        return wrap(Iterators.concat(iterators), iterators);
    }

    /**
     * Combines multiple closeable iterators into a single closeable iterator. The returned
     * closeable iterator iterates across the elements of each closeable iterator in {@code inputs}.
     * The input iterators are not polled until necessary.
     *
     * As each closeable iterator is exhausted, it is closed before moving onto the next closeable
     * iterator.  A call to close on the returned closeable iterator will quietly close all of
     * the closeable iterators in {@code inputs} which have not been exhausted.
     */
    public static <T> CloseableIterator<T> chain(CloseableIterator<? extends T>... iterators) {
        return chain(Iterators.forArray(iterators));
    }

    /**
     * Combines multiple closeable iterators into a single closeable iterator. The returned
     * closeable iterator iterates across the elements of each closeable iterator in {@code inputs}.
     * The input iterators are not polled until necessary.
     *
     * As each closeable iterator is exhausted, it is closed before moving onto the next closeable
     * iterator.  A call to close on the returned closeable iterator will quietly close all of
     * the closeable iterators in {@code inputs} which have not been exhausted.
     */
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
     * If we can assume the closeable iterator is sorted, return the distinct elements.
     * This only works if the data provided is sorted.
     */
    public static <T> CloseableIterator<T> distinct(final CloseableIterator<T> iterator) {
        checkNotNull(iterator);
        return wrap(Iterators2.distinct(iterator), iterator);
    }


    /**
     * Autoclose the iterator when exhausted or if an exception is thrown. It is currently set to protected, so that only
     * classes in this package can use.
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

    /**
     * Creates a {@link CloseableIterator} from a standard iterator.
     */
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
     * Creates a {@link CloseableIterable} from a standard iterable, while closing the provided
     * closeable.
     *
     * Intentionally left package private.
     */
    static <T> CloseableIterator<T> wrap(final Iterator<T> iterator, final Closeable closeable) {
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
