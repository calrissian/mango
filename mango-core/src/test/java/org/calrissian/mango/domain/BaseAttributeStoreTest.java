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

    Attribute attribute1 = new Attribute("key1", "val1");
    Attribute attribute2 = new Attribute("key1", "val2");
    Attribute attribute3 = new Attribute("key2", "val2");
    Attribute attribute4 = new Attribute("key3", "val3");

    Multimap<String, Attribute> attributeMultimap = ArrayListMultimap.create();

    BaseAttributeStore attributeCollection;

    @Before
    public void setup() {

        attributeMultimap.put(attribute1.getKey(), attribute1);
        attributeMultimap.put(attribute2.getKey(), attribute2);
        attributeMultimap.put(attribute3.getKey(), attribute3);
        attributeMultimap.put(attribute4.getKey(), attribute4);

        attributeCollection = new BaseAttributeStore(attributeMultimap);
    }

    @Test
    public void testRemoveAll_forManyTuples() {

        Collection<Attribute> removed = attributeCollection.removeAll(asList(new Attribute[]{attribute2, attribute3}));
        assertEquals(2, removed.size());
        assertEquals(attribute2, Iterables.get(removed, 0));
        assertEquals(attribute3, Iterables.get(removed, 1));
        assertEquals(1, attributeCollection.getAll(attribute2.getKey()).size());
        assertEquals(0, attributeCollection.getAll(attribute3.getKey()).size());
    }


    @Test
    public void testRemoveAll_singleTuple() {
        Attribute removed = attributeCollection.remove(attribute1);
        assertEquals(attribute1, removed);
        assertEquals(1, attributeCollection.getAll(attribute1.getKey()).size());
    }

    @Test
    public void testRemove_singleByKey() {
        Attribute removed = attributeCollection.remove(attribute1.getKey());
        assertEquals(removed, attribute1);
        assertEquals(1, attributeCollection.getAll(attribute2.getKey()).size());
    }
}
