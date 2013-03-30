package org.calrissian.criteria.serialization;

import org.calrissian.commons.serialization.ObjectMapperContext;
import org.calrissian.criteria.domain.Node;
import org.calrissian.criteria.visitor.PrintNodeVisitor;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.StringWriter;

import static org.junit.Assert.assertEquals;

/**
 * Date: 11/15/12
 * Time: 11:50 AM
 */
public class NodeDeserializerTest {
    private static final Logger logger = LoggerFactory.getLogger(NodeDeserializerTest.class);

    private ObjectMapper objectMapper = ObjectMapperContext.getInstance().getObjectMapper();

    @Before
    public void setUp() throws Exception {
        objectMapper.getDeserializationConfig().addMixInAnnotations(Node.class, NodeMixin.class);
    }

    @Test
    public void testEqDeserialize() throws Exception {
        String json = "{\"eq\":{\"key\":\"k1\",\"type\":\"string\",\"value\":\"v1\"}}";
        Node node = objectMapper.readValue(json, Node.class);
        StringWriter writer = new StringWriter();
        node.accept(new PrintNodeVisitor(writer));
        writer.flush();
        writer.close();
        String eq = writer.toString();
        assertEquals("Equals[k1,v1],", eq);
    }

    @Test
    public void testRangeDeserialize() throws Exception {
        String json = "{\"range\":{\"key\":\"k1\",\"type\":\"string\",\"start\":\"v1\",\"end\":\"v2\"}}";
        Node node = objectMapper.readValue(json, Node.class);
        StringWriter writer = new StringWriter();
        node.accept(new PrintNodeVisitor(writer));
        writer.flush();
        writer.close();
        String eq = writer.toString();
        assertEquals("Range[k1,(v1,v2)],", eq);
    }

    @Test
    public void testAndEqDeserialize() throws Exception {
        String json = "{\"and\":{\"children\":[{\"eq\":{\"key\":\"k1\",\"type\":\"string\",\"value\":\"v1\"}},{\"eq\":{\"key\":\"k2\",\"type\":\"string\",\"value\":\"v2\"}}]}}";
        Node node = objectMapper.readValue(json, Node.class);
        StringWriter writer = new StringWriter();
        node.accept(new PrintNodeVisitor(writer));
        writer.flush();
        writer.close();
        String eq = writer.toString();
        assertEquals("AndNode(Equals[k1,v1],Equals[k2,v2],),", eq);
    }

    @Test
    public void testOrEqDeserialize() throws Exception {
        String json = "{\"or\":{\"children\":[{\"eq\":{\"key\":\"k1\",\"type\":\"string\",\"value\":\"v1\"}},{\"eq\":{\"key\":\"k2\",\"type\":\"string\",\"value\":\"v2\"}}]}}";
        Node node = objectMapper.readValue(json, Node.class);
        StringWriter writer = new StringWriter();
        node.accept(new PrintNodeVisitor(writer));
        writer.flush();
        writer.close();
        String eq = writer.toString();
        assertEquals("OrNode(Equals[k1,v1],Equals[k2,v2],),", eq);
    }

    @Test
    public void testAndOrEqNeqDeserialize() throws Exception {
        String json = "{\"or\":{\"children\":[{\"and\":{\"children\":[{\"eq\":{\"key\":\"k1\",\"type\":\"string\",\"value\":\"v1\"}},{\"neq\":{\"key\":\"k2\",\"type\":\"ipv4\",\"value\":\"1.2.3.4\"}}]}},{\"and\":{\"children\":[{\"eq\":{\"key\":\"k3\",\"type\":\"integer\",\"value\":\"1234\"}}]}}]}}";
        Node node = objectMapper.readValue(json, Node.class);
        StringWriter writer = new StringWriter();
        node.accept(new PrintNodeVisitor(writer));
        writer.flush();
        writer.close();
        String eq = writer.toString();
        assertEquals("OrNode(AndNode(Equals[k1,v1],NotEquals[k2,1.2.3.4],),AndNode(Equals[k3,1234],),),", eq);
    }
}
