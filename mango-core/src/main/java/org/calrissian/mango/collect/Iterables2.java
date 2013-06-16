package org.calrissian.mango.collect;

import java.util.Iterator;
import java.util.Queue;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.Collections.emptyList;
import static java.util.Collections.singleton;

/**
 * Additional functions for working on Iterables
 */
public class Iterables2 {

    /**
     * Wraps any iterable into a generic iterable object. This is useful for using complex iterables such as FluentIterable
     * with libraries that use reflection to determine bean definitions such as Jackson.
     * @param iterable
     * @param <T>
     * @return
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
     * Simple method to return an empty iterable.
     * @param <T>
     * @return
     */
    public static <T> Iterable<T> emptyIterable() {
        return emptyList();
    }

    /**
     * Creates an iterable with a single value.
     * @param data
     * @param <T>
     * @return
     */
    public static <T> Iterable<T> singletonIterable(T data) {
        return singleton(data);
    }

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

    /**
     * Generates an iterable that will drain a queue by consistently polling the latest item.
     * @param queue
     * @param <T>
     * @return
     */
    public static <T> Iterable<T> drainingIterable(Queue<T> queue) {

        //Only create a single iterator as navigating it will draing the queue anyway.
        final Iterator<T> iterator = Iterators2.drainingIterator(queue);
        return new Iterable<T>() {
            @Override
            public Iterator<T> iterator() {
                return iterator;
            }
        };
    }
}
