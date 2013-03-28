package org.calrissian.mango.collect;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 */
public class FluentCloseableIterableTest {

    @Test
    public void testFluent() throws IOException {
        final ArrayList<Integer> list = Lists.newArrayList(1, 2, 3, 4, 5);
        CloseableIterable<Integer> closeableIterable = new AbstractCloseableIterable<Integer>() {

            @Override
            public void doClose() throws IOException {
            }

            @Override
            public Iterator<Integer> retrieveIterator() {
                return list.iterator();
            }
        };

        FluentCloseableIterable<Integer> filter = (FluentCloseableIterable<Integer>) FluentCloseableIterable.
                from(closeableIterable).
                transform(new Function<Integer, Integer>() {
                    @Override
                    public Integer apply(Integer input) {
                        return input + 1;
                    }
                }).
                filter(new Predicate<Integer>() {
                    @Override
                    public boolean apply(Integer input) {
                        return (input % 2) == 0;      //even only
                    }
                });

        Iterables.elementsEqual(Lists.newArrayList(2, 4, 6), filter);

        filter.close();

        //make sure closed
        try {
            Iterator<Integer> iterator = filter.iterator();
            fail();
        } catch (IllegalStateException ise) {
        }
    }

    @Test
    public void testLimit() throws IOException {
        final ArrayList<Integer> list = Lists.newArrayList(1, 2, 3, 4, 5);
        CloseableIterable<Integer> closeableIterable = new AbstractCloseableIterable<Integer>() {

            @Override
            public void doClose() throws IOException {
            }

            @Override
            public Iterator<Integer> retrieveIterator() {
                return list.iterator();
            }
        };

        FluentCloseableIterable<Integer> limit = (FluentCloseableIterable<Integer>) FluentCloseableIterable.
                from(closeableIterable).limit(3);

        assertEquals(3, Iterables.size(limit));
        limit.close();
    }
}
