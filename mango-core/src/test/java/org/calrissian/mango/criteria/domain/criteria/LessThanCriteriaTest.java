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
package org.calrissian.mango.criteria.domain.criteria;

import org.calrissian.mango.domain.entity.EntityBuilder;
import org.junit.Test;

import static com.google.common.collect.Ordering.natural;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class LessThanCriteriaTest {

    @Test
    public void test() {

        LessThanCriteria criteria = new LessThanCriteria<>("key1", 5, natural(), null);

        EntityBuilder entity = EntityBuilder.create("type", "id");

        // first test without attribute existing
        assertFalse(criteria.apply(entity.build()));

        entity = entity.attr("key1", 10);

        assertFalse(criteria.apply(entity.build()));

        entity = entity.attr("key1", 4);

        assertTrue(criteria.apply(entity.build()));
    }
}
