/*
 * Copyright (C) 2017 The Calrissian Authors
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
package org.calrissian.mango.criteria.domain;


import org.calrissian.mango.criteria.builder.QueryBuilder;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class NodeTest {

    @Test
    public void testClone() {

        Node node = QueryBuilder.create().or().and().eq("key1", "val1").range("key2", 0, 10).end().eq("key4", "val4").end().build();

        Node node2 = node.clone(null);

        assertTrue(node2 instanceof OrNode && node2 != node);
        assertEquals(2, node2.children().size());
        assertTrue(node2.children().get(0) instanceof AndNode && node2.children().get(0) != node.children().get(0));
        assertEquals(2, node2.children().get(0).children().size());
        assertTrue(node2.children().get(1) instanceof EqualsLeaf && node2.children().get(1) != node.children().get(1));
        assertTrue(node2.children().get(0).children().get(0) instanceof EqualsLeaf &&
                node2.children().get(0).children().get(0) != node.children().get(0).children().get(0));
        assertTrue(node2.children().get(0).children().get(1) instanceof RangeLeaf &&
                node2.children().get(0).children().get(1) != node.children().get(0).children().get(1));
    }

    @Test
    public void testEquals_andNodesWithEq() {

        Node node = QueryBuilder.create().and().and().eq("key1", "val1").end().eq("key2", "val2").end().build();
        Node node2 = QueryBuilder.create().and().and().eq("key1", "val1").end().eq("key2", "val2").end().build();
        assertTrue(node.equals(node2));
    }

    @Test
    public void testEquals_orNodesWithEq() {

        Node node = QueryBuilder.create().or().or().eq("key1", "val1").end().eq("key2", "val2").end().build();
        Node node2 = QueryBuilder.create().or().or().eq("key1", "val1").end().eq("key2", "val2").end().build();
        assertTrue(node.equals(node2));
    }
}
