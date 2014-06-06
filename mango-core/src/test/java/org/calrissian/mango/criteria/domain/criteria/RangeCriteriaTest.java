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
package org.calrissian.mango.criteria.domain.criteria;

import org.calrissian.mango.criteria.support.ComparableComparator;
import org.calrissian.mango.domain.Tuple;
import org.calrissian.mango.domain.entity.BaseEntity;
import org.calrissian.mango.domain.entity.Entity;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class RangeCriteriaTest {

    @Test
    public void test() {

        RangeCriteria criteria = new RangeCriteria("key1", 5, 10, new ComparableComparator(), null);

        Entity entity = new BaseEntity("type", "id");

        // first test without tuple existing
        assertFalse(criteria.apply(entity));

        entity.put(new Tuple("key1", 11));
        assertFalse(criteria.apply(entity));

        entity.put(new Tuple("key1", 4));
        assertFalse(criteria.apply(entity));

        entity.put(new Tuple("key1", 5));
        assertTrue(criteria.apply(entity));

        entity.put(new Tuple("key1", 6));
        assertTrue(criteria.apply(entity));

    }
}
