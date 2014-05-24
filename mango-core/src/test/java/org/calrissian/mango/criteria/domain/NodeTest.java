package org.calrissian.mango.criteria.domain;


import org.calrissian.mango.criteria.builder.QueryBuilder;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class NodeTest {

    @Test
    public void testClone() {

        Node node = new QueryBuilder().or().and().eq("key1", "val1").range("key2", 0, 10).end().eq("key4", "val4").end().build();

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

        Node node = new QueryBuilder().and().and().eq("key1", "val1").end().eq("key2", "val2").end().build();
        Node node2 = new QueryBuilder().and().and().eq("key1", "val1").end().eq("key2", "val2").end().build();
        assertTrue(node.equals(node2));
    }

    @Test
    public void testEquals_orNodesWithEq() {

        Node node = new QueryBuilder().or().or().eq("key1", "val1").end().eq("key2", "val2").end().build();
        Node node2 = new QueryBuilder().or().or().eq("key1", "val1").end().eq("key2", "val2").end().build();
        assertTrue(node.equals(node2));
    }
}
