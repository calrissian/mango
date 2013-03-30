package org.calrissian.criteria.utils;

import org.calrissian.criteria.builder.QueryBuilder;
import org.calrissian.criteria.domain.Node;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.Assert.assertEquals;

/**
 * Date: 11/13/12
 * Time: 3:51 PM
 */
public class NodeUtilsTest {
    private static final Logger logger = LoggerFactory.getLogger(NodeUtilsTest.class);

    @Test
    public void testSerializeNode() throws Exception {
        Node node = new QueryBuilder().and().and().eq("k1", "v1").eq("k2", "v2").endStatement().endStatement().build();
        String json = NodeUtils.serialize(node);

        Node outNode = NodeUtils.deserialize(json);
        assertEquals(node, outNode);
    }
}
