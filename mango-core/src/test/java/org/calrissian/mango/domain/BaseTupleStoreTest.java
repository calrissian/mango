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
package org.calrissian.mango.domain;


import com.google.common.collect.Iterables;
import org.junit.Before;
import org.junit.Test;

import java.util.Collection;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;

public class BaseTupleStoreTest {

    Tuple tuple1 = new Tuple("key1", "val1");
    Tuple tuple2 = new Tuple("key1", "val2");
    Tuple tuple3 = new Tuple("key2", "val2");
    Tuple tuple4 = new Tuple("key3", "val3");

    BaseTupleStore tupleCollection = new BaseTupleStore();

    @Before
    public void setup() {
        tupleCollection.putAll(asList(new Tuple[]{tuple1, tuple2, tuple3, tuple4}));
    }

    @Test
    public void testRemoveAll_forManyTuples() {

        Collection<Tuple> removed = tupleCollection.removeAll(asList(new Tuple[]{tuple2, tuple3}));
        assertEquals(2, removed.size());
        assertEquals(tuple2, Iterables.get(removed, 0));
        assertEquals(tuple3, Iterables.get(removed, 1));
        assertEquals(1, tupleCollection.getAll(tuple2.getKey()).size());
        assertEquals(0, tupleCollection.getAll(tuple3.getKey()).size());
    }


    @Test
    public void testRemoveAll_singleTuple() {
        Tuple removed = tupleCollection.remove(tuple1);
        assertEquals(tuple1, removed);
        assertEquals(1, tupleCollection.getAll(tuple1.getKey()).size());
    }

    @Test
    public void testRemove_singleByKey() {
        Tuple removed = tupleCollection.remove(tuple1.getKey());
        assertEquals(removed, tuple2);
        assertEquals(1, tupleCollection.getAll(tuple1.getKey()).size());
    }
}
