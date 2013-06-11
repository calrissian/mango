package org.calrissian.mango.collect;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import org.junit.Test;

import java.io.IOException;

import static java.util.Arrays.asList;
import static org.calrissian.mango.collect.CloseableIterables.wrap;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 */
public class FluentCloseableIterableTest {

    @Test
    public void testFluent() throws IOException {
        CloseableIterable<Integer> closeableIterable = wrap(asList(1, 2, 3, 4, 5));

        FluentCloseableIterable<Integer> filter = FluentCloseableIterable.
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

        Iterables.elementsEqual(asList(2, 4, 6), filter);

        filter.close();

        //make sure closed
        try {
            filter.iterator();
            fail();
        } catch (IllegalStateException ise) {
        }
    }

    @Test
    public void testLimit() throws IOException {
        CloseableIterable<Integer> closeableIterable = wrap(asList(1, 2, 3, 4, 5));

        FluentCloseableIterable<Integer> limit = FluentCloseableIterable.
                from(closeableIterable).limit(3);

        assertEquals(3, Iterables.size(limit));
        limit.close();
    }
}
