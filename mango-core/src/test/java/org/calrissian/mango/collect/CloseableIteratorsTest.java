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


import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterators;

import org.calrissian.mango.collect.mock.MockIterator;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.util.NoSuchElementException;

import static com.google.common.collect.Iterators.elementsEqual;
import static java.util.Arrays.asList;
import static org.calrissian.mango.collect.CloseableIterators.*;
import static org.junit.Assert.*;

public class CloseableIteratorsTest {

    @SuppressWarnings("unchecked")
    @Test
    public void testWrap() throws Exception {
        CloseableIterator<Object> mockIterator = mockIterator();
        CloseableIterator<Object> iterator = CloseableIterators.wrap(mockIterator);

        //test passthroughs
        try {
            iterator.hasNext();
            iterator.next();
            iterator.remove();
            iterator.close();
        } finally {
            Mockito.verify(mockIterator, Mockito.times(1)).hasNext();
            Mockito.verify(mockIterator, Mockito.times(1)).next();
            Mockito.verify(mockIterator, Mockito.times(1)).remove();
            Mockito.verify(mockIterator, Mockito.times(1)).close();
        }
    }

    @Test
    public void testTransform() throws IOException {
        //add one
        CloseableIterator<Integer> addOne = transform(testIterator(), new Function<Integer, Integer>() {
            @Override
            public Integer apply(Integer input) {
                return input + 1;
            }
        });
        //multiply by ten
        CloseableIterator<Integer> multTen = transform(addOne, new Function<Integer, Integer>() {
            @Override
            public Integer apply(Integer input) {
                return input * 10;
            }
        });

        assertTrue(elementsEqual(asList(20, 30, 40, 50, 60).iterator(), multTen));
        assertFalse(multTen.hasNext());
        multTen.close();
    }

    @Test
    public void testLimit() throws IOException {
        CloseableIterator<Integer> firstThree = limit(testIterator(), 3);

        assertTrue(elementsEqual(asList(1, 2, 3).iterator(), firstThree));
        assertFalse(firstThree.hasNext());
        firstThree.close();
    }

    @Test
    public void testFilter() throws IOException {
        //filter odd
        CloseableIterator<Integer> odd = filter(testIterator(), new Predicate<Integer>() {
            @Override
            public boolean apply(java.lang.Integer input) {
                return input % 2 == 0;
            }
        });

        assertTrue(elementsEqual(asList(2, 4).iterator(), odd));
        assertFalse(odd.hasNext());
        odd.close();
    }


    @Test
    public void testDistinct() throws Exception {
        CloseableIterator<Integer> distinct = distinct(wrap(asList(1, 1, 2, 2, 3, 3, 3, 4, 5, 6, 7, 7, 7).iterator()));

        assertTrue(elementsEqual(asList(1, 2, 3, 4, 5, 6, 7).iterator(), distinct));
        assertFalse(distinct.hasNext());
        distinct.close();
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Test
    public void testChain() throws Exception {
        //test empty
        CloseableIterator iterator = chain(emptyIterator(), emptyIterator(), emptyIterator());
        assertFalse(iterator.hasNext());

        try {
            iterator.next();
            fail();
        } catch (NoSuchElementException ne) {
        }
        iterator.close();

        //Verify 1 element
        iterator = chain(wrap(asList(1).iterator()));
        try {
            assertTrue(iterator.hasNext());
            iterator.next();
        } catch (NoSuchElementException ex) {
            fail("should not throw NoSuchElementException here");
        } finally {
            iterator.closeQuietly();
        }
        iterator.close();

        //Verify multiple and order
        iterator = chain(
                wrap(asList(1, 2).iterator()),
                wrap(asList(3, 4).iterator()),
                wrap(asList(5, 6).iterator()));

        assertTrue(iterator.hasNext());
        assertTrue(Iterators.elementsEqual(asList(1, 2, 3, 4, 5, 6).iterator(), iterator));
        assertFalse(iterator.hasNext());
        iterator.close();
    }

    @Test
    public void testAutoClose() throws Exception {
        MockIterator<Integer> iterator = testIterator();
        CloseableIterator<Integer> closeableIterator = autoClose(iterator);
        closeableIterator.close();
        assertTrue(iterator.isClosed());

        //if consumed close
        iterator = testIterator();
        closeableIterator = autoClose(iterator);
        Iterators.size(closeableIterator);
        assertTrue(iterator.isClosed());

        //if exception thrown
        iterator = testExceptionThrowingIterator();
        closeableIterator = autoClose(iterator);
        try {
            closeableIterator.next();
            fail();
        } catch (RuntimeException re) {
        }
        assertTrue(iterator.isClosed());

        iterator = testExceptionThrowingIterator();
        closeableIterator = autoClose(iterator);
        try {
            closeableIterator.remove();
            fail();
        } catch (RuntimeException re) {
        }
        assertTrue(iterator.isClosed());
    }

    @SuppressWarnings("rawtypes")
    private CloseableIterator mockIterator() {
        return Mockito.mock(CloseableIterator.class);
    }

    private MockIterator<Integer> testIterator() {
        return new MockIterator<Integer>(asList(1, 2, 3, 4, 5).iterator());
    }

    private MockIterator<Integer> testExceptionThrowingIterator() {
        return new MockIterator<Integer>(testIterator()) {
            @Override
            public Integer next() {
                throw new RuntimeException("I throw because I want to");
            }

            @Override
            public void remove() {
                throw new RuntimeException("I throw because I want to");
            }
        };
    }

}
