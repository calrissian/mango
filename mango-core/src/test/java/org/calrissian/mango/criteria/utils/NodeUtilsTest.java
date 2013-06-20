package org.calrissian.mango.criteria.utils;


import org.calrissian.mango.criteria.builder.QueryBuilder;
import org.calrissian.mango.criteria.domain.Node;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Date: 11/13/12
 * Time: 3:51 PM
 */
public class NodeUtilsTest {

    @Test
    public void testSerializeNode() throws Exception {
        Node node = new QueryBuilder().and().and().eq("k1", "v1").eq("k2", "v2").endStatement().endStatement().build();
        String json = NodeUtils.serialize(node);

        Node outNode = NodeUtils.deserialize(json);
        assertEquals(node, outNode);
    }
}
