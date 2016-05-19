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


import org.calrissian.mango.criteria.builder.QueryBuilder;
import org.calrissian.mango.criteria.domain.Node;
import org.junit.Test;

import static org.calrissian.mango.criteria.support.NodeUtils.criteriaFromNode;
import static org.junit.Assert.assertTrue;

public class CriteriaTest {

    @Test
    public void testEquals_andNodesWithEq() {

        Node node = QueryBuilder.create().and().and().eq("key1", "val1").end().eq("key2", "val2").end().build();
        Node node2 = QueryBuilder.create().and().and().eq("key1", "val1").end().eq("key2", "val2").end().build();

        Criteria criteria = criteriaFromNode(node);
        Criteria criteria2 = criteriaFromNode(node2);

        assertTrue(criteria.equals(criteria2));
    }

    @Test
    public void testEquals_orNodesWithEq() {

        Node node = QueryBuilder.create().or().or().eq("key1", "val1").end().eq("key2", "val2").end().build();
        Node node2 = QueryBuilder.create().or().or().eq("key1", "val1").end().eq("key2", "val2").end().build();
        Criteria criteria = criteriaFromNode(node);
        Criteria criteria2 = criteriaFromNode(node2);

        assertTrue(criteria.equals(criteria2));
    }

    @Test
    public void testClone_andNodesWithEq() {

        Node node = QueryBuilder.create().or().or().eq("key1", "val1").end().eq("key2", "val2").end().build();
        Criteria criteria = criteriaFromNode(node);
        Criteria criteria2 = criteria.clone(null);

        assertTrue(criteria.equals(criteria2));
    }

    @Test
    public void testClone_orNodesWithEq() {

        Node node = QueryBuilder.create().or().or().eq("key1", "val1").end().eq("key2", "val2").end().build();
        Criteria criteria = criteriaFromNode(node);
        Criteria criteria2 = criteria.clone(null);

        assertTrue(criteria.equals(criteria2));
    }
}
