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
package org.calrissian.mango.criteria.domain.criteria;

import org.calrissian.mango.domain.entity.EntityBuilder;
import org.junit.Test;

import static java.util.Comparator.naturalOrder;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class NotEqualsCriteriaTest {

    @Test
    public void testNotEquals() {

        Criteria eq = new NotEqualsCriteria<>("key1", "val1", naturalOrder(), null);
        EntityBuilder entity = EntityBuilder.create("type", "id")
                .attr("key1", "val2");

        assertTrue(eq.test(entity.build()));
    }

    @Test
    public void testEquals() {

        Criteria eq = new NotEqualsCriteria<>("key1", "val1", naturalOrder(), null);
        EntityBuilder entity = EntityBuilder.create("type", "id")
                .attr("key1", "val1");

        assertFalse(eq.test(entity.build()));
    }
}
