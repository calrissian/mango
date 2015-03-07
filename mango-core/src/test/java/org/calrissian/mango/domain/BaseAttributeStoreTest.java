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


import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Iterables;
import com.google.common.collect.Multimap;
import org.junit.Before;
import org.junit.Test;

import java.util.Collection;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;

public class BaseAttributeStoreTest {

    Attribute tuple1 = new Attribute("key1", "val1");
    Attribute tuple2 = new Attribute("key1", "val2");
    Attribute tuple3 = new Attribute("key2", "val2");
    Attribute tuple4 = new Attribute("key3", "val3");

    Multimap<String, Attribute> attributeMultimap = ArrayListMultimap.create();

    BaseAttributeStore tupleCollection;

    @Before
    public void setup() {

        attributeMultimap.put(tuple1.getKey(), tuple1);
        attributeMultimap.put(tuple2.getKey(), tuple2);
        attributeMultimap.put(tuple3.getKey(), tuple3);
        attributeMultimap.put(tuple4.getKey(), tuple4);

        tupleCollection = new BaseAttributeStore(attributeMultimap);
    }

    @Test
    public void testRemoveAll_forManyTuples() {

        Collection<Attribute> removed = tupleCollection.removeAll(asList(new Attribute[]{tuple2, tuple3}));
        assertEquals(2, removed.size());
        assertEquals(tuple2, Iterables.get(removed, 0));
        assertEquals(tuple3, Iterables.get(removed, 1));
        assertEquals(1, tupleCollection.getAll(tuple2.getKey()).size());
        assertEquals(0, tupleCollection.getAll(tuple3.getKey()).size());
    }


    @Test
    public void testRemoveAll_singleTuple() {
        Attribute removed = tupleCollection.remove(tuple1);
        assertEquals(tuple1, removed);
        assertEquals(1, tupleCollection.getAll(tuple1.getKey()).size());
    }

    @Test
    public void testRemove_singleByKey() {
        Attribute removed = tupleCollection.remove(tuple1.getKey());
        assertEquals(removed, tuple1);
        assertEquals(1, tupleCollection.getAll(tuple2.getKey()).size());
    }
}
