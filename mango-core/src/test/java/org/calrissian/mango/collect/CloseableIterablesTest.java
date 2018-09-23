/*
 * Copyright (C) 2017 The Calrissian Authors
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
import com.google.common.collect.Iterables;
import com.google.common.collect.Iterators;
import org.calrissian.mango.collect.mock.MockIterable;
import org.junit.Test;

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
    public void testWrap() {
        CloseableIterable<Integer> wrapped = CloseableIterables.fromIterable(asList(1, 2, 3, 4, 5));

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
    public void testTransform() {
        //add one
        CloseableIterable<Integer> addOne = transform(testIterable(), input -> input + 1);
        //multiply by ten
        CloseableIterable<Integer> multTen = transform(addOne, input -> input * 10);

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
    public void testLimit() {

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
    public void testFilter() {
        //filter odd
        CloseableIterable<Integer> odd = filter(testIterable(), input -> input % 2 == 0);

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
    public void testDistinct() {
        CloseableIterable<Integer> distinct = distinct(fromIterable(asList(1, 1, 2, 2, 3, 3, 3, 4, 5, 6, 7, 7, 7)));

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
    public void testAutoClose() {

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
