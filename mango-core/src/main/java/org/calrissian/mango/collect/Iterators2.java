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


import com.google.common.collect.AbstractIterator;

import java.util.Iterator;
import java.util.Queue;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Additional functions for working on Iterators
 */

/**
 * Additional functions for working on Iterators
 */
public class Iterators2 {

    /**
     * If we can assume the iterator is sorted, return the distinct elements. This only works
     * if the data provided is sorted.
     */
    public static <T> Iterator<T> distinct(final Iterator<T> iterator) {
        checkNotNull(iterator);
        return new AbstractIterator<T>() {
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
        };
    }

    /**
     * Generates an iterable that will drain a queue by consistently polling the latest item.
     * @param queue
     * @param <T>
     * @return
     */
    public static <T> Iterator<T> drainingIterator(final Queue<T> queue) {
        checkNotNull(queue);
        return new Iterator<T>() {
            @Override
            public boolean hasNext() {
                return queue.size() != 0;
            }

            @Override
            public T next() {
                return queue.poll();
            }

            @Override
            public void remove() {
                queue.remove();
            }
        };
    }
}
