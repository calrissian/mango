/*
 * Copyright (C) 2019 The Calrissian Authors
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


import org.junit.Test;

import java.util.List;

import static com.google.common.collect.Iterables.get;
import static com.google.common.collect.Iterables.size;
import static com.google.common.collect.Lists.newArrayList;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.calrissian.mango.collect.Iterables2.distinct;
import static org.calrissian.mango.collect.Iterables2.groupBy;
import static org.junit.Assert.assertEquals;

public class Iterables2Test {

    @Test
    public void distinctTest() throws Exception {
        Iterable<Integer> distinct = distinct(asList(1, 1, 2, 2, 3, 3, 3, 4, 5, 6, 7, 7, 7));

        assertEquals(asList(1, 2, 3, 4, 5, 6, 7), newArrayList(distinct));
    }

    @Test(expected = NullPointerException.class)
    public void distinctNullIteratorTest() {
        distinct(null);
    }

    @Test
    public void groupByTest() {

        List<Integer> testdata = asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9);

        Iterable<List<Integer>> grouped = groupBy(testdata, input -> input / 5);

        assertEquals(2, size(grouped));
        assertEquals(asList(0, 1, 2, 3, 4), get(grouped, 0));
        assertEquals(asList(5, 6, 7, 8, 9), get(grouped, 1));

        //no grouped
        grouped = groupBy(testdata, input -> null);

        assertEquals(1, size(grouped));
        assertEquals(testdata, get(grouped, 0));

        //all grouped
        grouped = groupBy(testdata, input -> input);

        assertEquals(10, size(grouped));
        for (int i = 0; i< testdata.size(); i++) {
            List<Integer> group = get(grouped, i);
            assertEquals(1, group.size());
            assertEquals(new Integer(i), group.get(0));
        }
    }

    @Test(expected = NullPointerException.class)
    public void groupByNullIteratorTest() {
        groupBy(null, input -> null);
    }

    @Test(expected = NullPointerException.class)
    public void groupByNullEquivTest() {
        groupBy(emptyList(), null);
    }


}
