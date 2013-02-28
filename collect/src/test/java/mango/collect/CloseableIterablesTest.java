package mango.collect;

import com.google.common.base.Function;
import com.google.common.collect.AbstractIterator;
import com.google.common.collect.Iterables;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import static org.junit.Assert.*;

/**
 */
public class CloseableIterablesTest {

    @Test
    public void testTransform() throws IOException {
        AbstractCloseableIterable<Integer> closeableIterable = mockCloseableIterable();

        //add one
        CloseableIterable<Integer> addOne = CloseableIterables.transform(closeableIterable, new Function<Integer, Integer>() {
            @Override
            public Integer apply(Integer input) {
                return input + 1;
            }
        });

        //multiply by ten
        CloseableIterable<Integer> multTen = CloseableIterables.transform(addOne, new Function<Integer, Integer>() {
            @Override
            public Integer apply(Integer input) {
                return input * 10;
            }
        });

        Iterables.elementsEqual(Lists.newArrayList(20, 30, 40, 50, 60), multTen);

        multTen.close();

        //make sure closed
        try {
            Iterator<Integer> iterator = multTen.iterator();
            fail();
        } catch (IllegalStateException ise) {
        }

    }

    @Test
    public void testLimit() throws IOException {
        AbstractCloseableIterable<Integer> closeableIterable = mockCloseableIterable();

        //add one
        CloseableIterable<Integer> firstThree = CloseableIterables.limit(closeableIterable, 3);
        assertEquals(3, Iterables.size(firstThree));
        firstThree.close();
    }

    @Test
    public void testAutoClose() throws Exception {

        AbstractCloseableIterable<Integer> iterable = mockCloseableIterable();
        CloseableIterable<Integer> closeableIterable = CloseableIterables.autoClose(iterable);
        closeableIterable.close();
        assertTrue(iterable.isClosed());

        //if consumed close
        iterable = mockCloseableIterable();
        closeableIterable = CloseableIterables.autoClose(iterable);
        Iterator<Integer> iterator = closeableIterable.iterator();
        Iterators.size(iterator);
        assertTrue(iterable.isClosed());

        //if exception thrown
        iterable = mockExceptionThrowingCloseableIterable();
        closeableIterable = CloseableIterables.autoClose(iterable);
        iterator = closeableIterable.iterator();
        try {
            iterator.next();
            fail();
        } catch (RuntimeException re) {
        }
        assertTrue(iterable.isClosed());
    }

    private AbstractCloseableIterable<Integer> mockCloseableIterable() {
        final ArrayList<Integer> list = Lists.newArrayList(1, 2, 3, 4, 5);
        return new AbstractCloseableIterable<Integer>() {

            @Override
            public void doClose() throws IOException {
            }

            @Override
            public Iterator<Integer> retrieveIterator() {
                return list.iterator();
            }
        };
    }

    private AbstractCloseableIterable<Integer> mockExceptionThrowingCloseableIterable() {
        return new AbstractCloseableIterable<Integer>() {
            @Override
            protected void doClose() throws IOException {
            }

            @Override
            protected Iterator<Integer> retrieveIterator() {
                return new AbstractIterator<Integer>() {

                    @Override
                    protected Integer computeNext() {
                        throw new RuntimeException("I throw because I want to");
                    }
                };
            }
        };
    }
}
