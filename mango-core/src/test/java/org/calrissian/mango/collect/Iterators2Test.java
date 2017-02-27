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


import com.google.common.base.Function;
import org.junit.Test;

import java.util.Iterator;
import java.util.List;

import static com.google.common.collect.Iterators.elementsEqual;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyIterator;
import static org.calrissian.mango.collect.Iterators2.distinct;
import static org.calrissian.mango.collect.Iterators2.groupBy;
import static org.junit.Assert.*;

public class Iterators2Test {

    @Test
    public void distinctTest() throws Exception {
        Iterator<Integer> distinct = distinct(asList(1, 1, 2, 2, 3, 3, 3, 4, 5, 6, 7, 7, 7).iterator());

        assertTrue(elementsEqual(asList(1, 2, 3, 4, 5, 6, 7).iterator(), distinct));
        assertFalse(distinct.hasNext());
    }

    @Test(expected = NullPointerException.class)
    public void distinctNullIteratorTest() {
        distinct(null);
    }

    @Test
    public void groupByTest() {
        List<Integer> testdata = asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9);

        Iterator<List<Integer>> grouped = groupBy(testdata.iterator(), new Function<Integer, Object>() {
            @Override
            public Object apply(Integer input) {
                return input / 5;
            }
        });

        assertTrue(grouped.hasNext());
        assertEquals(asList(0, 1, 2, 3, 4), grouped.next());
        assertEquals(asList(5, 6, 7, 8, 9), grouped.next());
        assertFalse(grouped.hasNext());

        //no grouped
        grouped = groupBy(testdata.iterator(), new Function<Integer, Object>() {
            @Override
            public Object apply(Integer input) {
                return null;
            }
        });

        assertTrue(grouped.hasNext());
        assertEquals(testdata, grouped.next());
        assertFalse(grouped.hasNext());

        //all grouped
        grouped = groupBy(testdata.iterator(), new Function<Integer, Object>() {
            @Override
            public Object apply(Integer input) {
                return input;
            }
        });

        assertTrue(grouped.hasNext());
        for (int i = 0; i< testdata.size(); i++) {
            assertTrue(grouped.hasNext());
            List<Integer> group = grouped.next();
            assertEquals(1, group.size());
            assertEquals(new Integer(i), group.get(0));
        }
        assertFalse(grouped.hasNext());
    }

    @Test(expected = NullPointerException.class)
    public void groupByNullIteratorTest() {
        groupBy(null, new Function<Object, Object>() {
            @Override
            public Object apply(Object input) {
                return null;
            }
        });
    }

    @Test(expected = NullPointerException.class)
    public void groupByNullEquivTest() {
        groupBy(emptyIterator(), null);
    }


}
