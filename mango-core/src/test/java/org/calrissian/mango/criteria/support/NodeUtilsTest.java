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
package org.calrissian.mango.criteria.support;

import org.calrissian.mango.criteria.builder.QueryBuilder;
import org.calrissian.mango.criteria.domain.Node;
import org.calrissian.mango.criteria.domain.criteria.AndCriteria;
import org.calrissian.mango.criteria.domain.criteria.Criteria;
import org.calrissian.mango.criteria.domain.criteria.EqualsCriteria;
import org.calrissian.mango.criteria.domain.criteria.OrCriteria;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class NodeUtilsTest {

    @Test
    public void testCriteriaFromNode_fullTree() {

        Node node = QueryBuilder.create().or().and().eq("key1", "val1").eq("key2", "val2").end().eq("key3", "val3").end().build();

        Criteria criteria = NodeUtils.criteriaFromNode(node);

        assertTrue(criteria instanceof OrCriteria);
        assertTrue(criteria.children().get(0) instanceof AndCriteria);
        assertTrue(criteria.children().get(0).children().get(0) instanceof EqualsCriteria);
        assertTrue(criteria.children().get(0).children().get(1) instanceof EqualsCriteria);
        assertTrue(criteria.children().get(1).children().get(0) instanceof EqualsCriteria);
    }

    @Test
    public void test_isEmpty_notEmpty() {

        Node node = QueryBuilder.create().or().and().or().and().end().end().end().end().build();
    }
}
