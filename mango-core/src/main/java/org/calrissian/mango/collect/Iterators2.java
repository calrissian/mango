/*
 * Copyright (C) 2014 The Calrissian Authors
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
import com.google.common.collect.AbstractIterator;
import com.google.common.collect.Iterators;
import com.google.common.collect.PeekingIterator;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Queue;

import static com.google.common.base.Objects.equal;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.Iterators.consumingIterator;
import static com.google.common.collect.Iterators.peekingIterator;
import static java.util.Collections.unmodifiableList;

/**
 * Additional functions for working on Iterators
 */
public class Iterators2 {

    private Iterators2() {/* private constructor */}

    /**
     * If we can assume the iterator is sorted, return the distinct elements. This only works
     * if the data provided is sorted.
     */
    public static <T> Iterator<T> distinct(final Iterator<T> iterator) {
        checkNotNull(iterator);
        return new AbstractIterator<T>() {
            private final PeekingIterator<T> peekingIterator = peekingIterator(iterator);
            private T curr = null;

            @Override
            protected T computeNext() {
                while (peekingIterator.hasNext() && equal(curr, peekingIterator.peek())) {
                    peekingIterator.next();
                }
                if (!peekingIterator.hasNext())
                    return endOfData();

                curr = peekingIterator.next();
                return curr;
            }
        };
    }

    /**
     * Divides an iterator into unmodifiable sublists of equivalent elements. The iterator groups elements
     * in consecutive order, forming a new parition when the value from the provided function changes. For example,
     * grouping the iterator {@code [1, 3, 2, 4, 5]} with a function grouping even and odd numbers
     * yields {@code [[1, 3], [2, 4], [5]} all in the original order.
     *
     * <p/>
     * <p>The returned lists implement {@link java.util.RandomAccess}.
     */
    public static <T> Iterator<List<T>> groupBy(final Iterator<? extends T> iterator, final Function<? super T, ?> groupingFunction) {
        checkNotNull(iterator);
        checkNotNull(groupingFunction);
        return new AbstractIterator<List<T>>() {
            private final PeekingIterator<T> peekingIterator = peekingIterator(iterator);

            @Override
            protected List<T> computeNext() {
                if (!peekingIterator.hasNext())
                    return endOfData();

                Object key = groupingFunction.apply(peekingIterator.peek());
                List<T> group = new ArrayList<>();

                do {
                    group.add(peekingIterator.next());
                } while (peekingIterator.hasNext() && equal(key, groupingFunction.apply(peekingIterator.peek())));

                return unmodifiableList(group);
            }
        };
    }
}
