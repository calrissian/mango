package org.calrissian.mango.collect;


import com.google.common.collect.AbstractIterator;

import java.util.Iterator;

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
}
