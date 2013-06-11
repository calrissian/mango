package org.calrissian.mango.collect;

import java.util.Iterator;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Additional functions for working on Iterables
 */
public class Iterables2 {

    /**
     * If we can assume the iterable is sorted, return the distinct elements. This only works
     * if the data provided is sorted.
     */
    public static <T> Iterable<T> distinct(final Iterable<T> iterable) {
        checkNotNull(iterable);
        return new Iterable<T>(){
            @Override
            public Iterator<T> iterator() {
                return Iterators2.distinct(iterable.iterator());
            }
        };
    }

}
