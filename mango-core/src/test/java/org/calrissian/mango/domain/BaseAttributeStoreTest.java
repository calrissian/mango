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

public class BaseAttributeStoreTest {

    Attribute keyValue1 = new Attribute("key1", "val1");
    Attribute keyValue2 = new Attribute("key1", "val2");
    Attribute keyValue3 = new Attribute("key2", "val2");
    Attribute keyValue4 = new Attribute("key3", "val3");

    BaseAttributeStore attributeCollection = new BaseAttributeStore();

    @Before
    public void setup() {
        attributeCollection.putAll(asList(new Attribute[]{keyValue1, keyValue2, keyValue3, keyValue4}));
    }

    @Test
    public void testRemoveAll_forManyTuples() {

        Collection<? extends Attribute> removed = attributeCollection.removeAll(asList(new Attribute[]{keyValue2, keyValue3}));
        assertEquals(2, removed.size());
        assertEquals(keyValue2, Iterables.get(removed, 0));
        assertEquals(keyValue3, Iterables.get(removed, 1));
        assertEquals(1, attributeCollection.getAll(keyValue2.getKey()).size());
        assertEquals(0, attributeCollection.getAll(keyValue3.getKey()).size());
    }


    @Test
    public void testRemoveAll_singleTuple() {
        Attribute removed = attributeCollection.remove(keyValue1);
        assertEquals(keyValue1, removed);
        assertEquals(1, attributeCollection.getAll(keyValue1.getKey()).size());
    }

    @Test
    public void testRemove_singleByKey() {
        Attribute removed = attributeCollection.remove(keyValue1.getKey());
        assertEquals(removed, keyValue1);
        assertEquals(1, attributeCollection.getAll(keyValue2.getKey()).size());
    }
}
