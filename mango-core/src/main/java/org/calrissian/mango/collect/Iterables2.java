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

import com.google.common.collect.Iterables;

import java.util.Collections;
import java.util.Iterator;
import java.util.Queue;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.Iterables.consumingIterable;
import static java.util.Collections.emptyList;

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
        checkNotNull(iterable);
        return new Iterable<T>() {
            @Override
            public Iterator<T> iterator() {
                return iterable.iterator();
            }
        };
    }

    /**
     * Generates an empty iterable.
     *
     * @deprecated use {@link Collections}.emptySet() instead
     */
    @Deprecated
    public static <T> Iterable<T> emptyIterable() {
        return emptyList();
    }

    /**
     * Generates an iterable with a single value.
     *
     * @deprecated use {@link Collections}.singleton() instead
     */
    @Deprecated
    public static <T> Iterable<T> singleton(T data) {
        return Collections.singleton(data);
    }

    /**
     * If we can assume the iterable is sorted, return the distinct elements. This only works
     * if the data provided is sorted.
     */
    public static <T> Iterable<T> distinct(final Iterable<T> iterable) {
        checkNotNull(iterable);
        return new Iterable<T>() {
            @Override
            public Iterator<T> iterator() {
                return Iterators2.distinct(iterable.iterator());
            }
        };
    }

    /**
     * Generates an iterable that will drain a queue by consistently polling the latest item.
     *
     * @deprecated use {@link Iterables}.consumingIterable instead
     */
    @Deprecated
    public static <T> Iterable<T> drainingIterable(Queue<T> queue) {
        checkNotNull(queue);
        return consumingIterable(queue);
    }
}
