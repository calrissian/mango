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
package org.calrissian.mango.criteria.serialization;

import org.calrissian.mango.criteria.domain.Node;
import org.calrissian.mango.criteria.visitor.PrintNodeVisitor;
import org.calrissian.mango.types.TypeContext;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Test;

import java.io.StringWriter;

import static org.calrissian.mango.types.TypeContext.DEFAULT_TYPES;
import static org.junit.Assert.assertEquals;

/**
 * Date: 11/15/12
 * Time: 11:50 AM
 */
public class NodeDeserializerTest {

    private ObjectMapper objectMapper = new ObjectMapper()
            .withModule(new CriteriaModule(DEFAULT_TYPES));

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
