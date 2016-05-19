/*
 * Copyright (C) 2016 The Calrissian Authors
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
import com.google.common.base.Predicate;
import com.google.common.collect.AbstractIterator;
import com.google.common.collect.Iterables;
import com.google.common.collect.Iterators;
import org.calrissian.mango.collect.mock.MockIterable;
import org.junit.Test;

import java.io.IOException;
import java.util.Iterator;

import static com.google.common.collect.Iterables.elementsEqual;
import static java.util.Arrays.asList;
import static org.calrissian.mango.collect.CloseableIterables.*;
import static org.junit.Assert.*;

/**
 */
public class CloseableIterablesTest {

    @Test
    public void testCloseEmptyMultipleTimes() {
        CloseableIterable<Integer> iterable = CloseableIterables.emptyIterable();
        iterable.iterator();
        iterable.closeQuietly();

        iterable = CloseableIterables.emptyIterable();
        iterable.iterator();
        iterable.closeQuietly();
    }

    @Test
    public void testWrap() throws Exception {
        CloseableIterable<Integer> wrapped = CloseableIterables.wrap(asList(1, 2, 3, 4, 5));

        assertTrue(elementsEqual(asList(1, 2, 3, 4, 5), wrapped));

        wrapped.close();

        //make sure closed
        try {
            wrapped.iterator();
            fail();
        } catch (IllegalStateException ignored) {
        }
    }

    @Test
    public void testTransform() throws IOException {
        //add one
        CloseableIterable<Integer> addOne = transform(testIterable(), new Function<Integer, Integer>() {
            @Override
            public Integer apply(Integer input) {
                return input + 1;
            }
        });
        //multiply by ten
        CloseableIterable<Integer> multTen = transform(addOne, new Function<Integer, Integer>() {
            @Override
            public Integer apply(Integer input) {
                return input * 10;
            }
        });

        assertTrue(elementsEqual(asList(20, 30, 40, 50, 60), multTen));
        multTen.close();

        //make sure closed
        try {
            multTen.iterator();
            fail();
        } catch (IllegalStateException ignored) {
        }

    }

    @Test
    public void testLimit() throws IOException {

        CloseableIterable<Integer> firstThree = limit(testIterable(), 3);

        assertEquals(3, Iterables.size(firstThree));
        assertTrue(elementsEqual(asList(1, 2, 3), firstThree));

        firstThree.close();

        //make sure closed
        try {
            firstThree.iterator();
            fail();
        } catch (IllegalStateException ignored) {
        }
    }

    @Test
    public void testFilter() throws IOException {
        //filter odd
        CloseableIterable<Integer> odd = filter(testIterable(), new Predicate<Integer>() {
            @Override
            public boolean apply(java.lang.Integer input) {
                return input % 2 == 0;
            }
        });

        assertTrue(elementsEqual(asList(2, 4), odd));

        odd.close();

        //make sure closed
        try {
            odd.iterator();
            fail();
        } catch (IllegalStateException ignored) {
        }

    }


    @Test
    public void testDistinct() throws Exception {
        CloseableIterable<Integer> distinct = distinct(wrap(asList(1, 1, 2, 2, 3, 3, 3, 4, 5, 6, 7, 7, 7)));

        assertEquals(7, Iterables.size(distinct));
        assertTrue(elementsEqual(asList(1, 2, 3, 4, 5, 6, 7), distinct));

        distinct.close();

        //make sure closed
        try {
            distinct.iterator();
            fail();
        } catch (IllegalStateException ignored) {
        }
    }

    @Test
    public void testAutoClose() throws Exception {

        MockIterable<Integer> iterable = testIterable();
        CloseableIterable<Integer> closeableIterable = autoClose(iterable);
        closeableIterable.close();
        assertTrue(iterable.isClosed());

        //if consumed close
        iterable = testIterable();
        closeableIterable = autoClose(iterable);
        Iterator<Integer> iterator = closeableIterable.iterator();
        Iterators.size(iterator);
        assertTrue(iterable.isClosed());

        //if exception thrown
        iterable = testExceptionThrowingIterable();
        closeableIterable = autoClose(iterable);
        iterator = closeableIterable.iterator();
        try {
            iterator.next();
            fail();
        } catch (RuntimeException ignored) {
        }
        assertTrue(iterable.isClosed());
    }

    private MockIterable<Integer> testIterable() {
        return new MockIterable<>(asList(1, 2, 3, 4, 5));
    }

    private MockIterable<Integer> testExceptionThrowingIterable() {
        return new MockIterable<Integer>(testIterable()) {

            @Override
            public Iterator<Integer> iterator() {
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
