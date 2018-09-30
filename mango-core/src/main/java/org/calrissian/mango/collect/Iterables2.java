/*
 * Copyright (C) 2017 The Calrissian Authors
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

import java.util.List;
import java.util.function.Function;

import static java.util.Objects.requireNonNull;

/**
 * Additional functions for working on Iterables
 */
public class Iterables2 {

    private Iterables2() {/* private constructor */}

    /**
     * Wraps any iterable into a generic iterable object. This is useful for using complex iterables such as FluentIterable
     * with libraries that use reflection to determine bean definitions such as Jackson.
     */
    public static <T> Iterable<T> simpleIterable(final Iterable<T> iterable) {
        requireNonNull(iterable);
        return () -> iterable.iterator();
    }

    /**
     * If we can assume the iterable is sorted, return the distinct elements. This only works
     * if the data provided is sorted.
     */
    public static <T> Iterable<T> distinct(final Iterable<T> iterable) {
        requireNonNull(iterable);
        return () -> Iterators2.distinct(iterable.iterator());
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
    public static <T> Iterable<List<T>> groupBy(final Iterable<? extends T> iterable, final Function<? super T, ?> groupingFunction) {
        requireNonNull(iterable);
        requireNonNull(groupingFunction);
        return () -> Iterators2.groupBy(iterable.iterator(), groupingFunction);
    }
}
