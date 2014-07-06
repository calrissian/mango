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
package org.calrissian.mango.json.ser;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.calrissian.mango.criteria.builder.QueryBuilder;
import org.calrissian.mango.criteria.domain.EqualsLeaf;
import org.calrissian.mango.criteria.domain.Node;
import org.calrissian.mango.criteria.domain.RangeLeaf;
import org.calrissian.mango.domain.ip.IPv4;
import org.calrissian.mango.json.MangoModule;
import org.junit.Test;

import static org.calrissian.mango.types.SimpleTypeEncoders.SIMPLE_TYPES;
import static org.junit.Assert.assertEquals;

public class NodeSerializerTest {

    private ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new MangoModule(SIMPLE_TYPES));

    @Test
    public void testEqSerialization() throws Exception {
        EqualsLeaf equalsLeaf = new EqualsLeaf("k1", "v1", null);
        String json = objectMapper.writeValueAsString(equalsLeaf);
        assertEquals("{\"eq\":{\"key\":\"k1\",\"type\":\"string\",\"value\":\"v1\"}}", json);
    }

    @Test
    public void testRangeSerialization() throws Exception {
        RangeLeaf leaf = new RangeLeaf("k1", "v1", "v2", null);
        String json = objectMapper.writeValueAsString(leaf);
        assertEquals("{\"range\":{\"key\":\"k1\",\"type\":\"string\",\"start\":\"v1\",\"end\":\"v2\"}}", json);
    }

    @Test
    public void testAndEqEqSerialization() throws Exception {
        Node node = new QueryBuilder().and().eq("k1", "v1").eq("k2", "v2").end().build();
        String json = objectMapper.writeValueAsString(node);
        assertEquals("{\"and\":{\"children\":[{\"eq\":{\"key\":\"k1\",\"type\":\"string\",\"value\":\"v1\"}},{\"eq\":{\"key\":\"k2\",\"type\":\"string\",\"value\":\"v2\"}}]}}", json);
    }

    @Test
    public void testAndEqEqDiffTypeSerialization() throws Exception {
        Node node = new QueryBuilder().and().eq("k1", "v1").eq("k2", new IPv4("1.2.3.4")).end().build();
        String json = objectMapper.writeValueAsString(node);
        assertEquals("{\"and\":{\"children\":[{\"eq\":{\"key\":\"k1\",\"type\":\"string\",\"value\":\"v1\"}},{\"eq\":{\"key\":\"k2\",\"type\":\"ipv4\",\"value\":\"1.2.3.4\"}}]}}", json);
    }

    @Test
    public void testAndEqNeqDiffTypeSerialization() throws Exception {
        Node node = new QueryBuilder().and().eq("k1", "v1").notEq("k2", new IPv4("1.2.3.4")).end().build();
        String json = objectMapper.writeValueAsString(node);
        assertEquals("{\"and\":{\"children\":[{\"eq\":{\"key\":\"k1\",\"type\":\"string\",\"value\":\"v1\"}},{\"neq\":{\"key\":\"k2\",\"type\":\"ipv4\",\"value\":\"1.2.3.4\"}}]}}", json);
    }

    @Test
    public void testOrEqNeqDiffTypeSerialization() throws Exception {
        Node node = new QueryBuilder().or().eq("k1", "v1").notEq("k2", new IPv4("1.2.3.4")).end().build();
        String json = objectMapper.writeValueAsString(node);
        assertEquals("{\"or\":{\"children\":[{\"eq\":{\"key\":\"k1\",\"type\":\"string\",\"value\":\"v1\"}},{\"neq\":{\"key\":\"k2\",\"type\":\"ipv4\",\"value\":\"1.2.3.4\"}}]}}", json);
    }

    @Test
    public void testAndOrEqNeqDiffTypeSerialization() throws Exception {
        Node node = new QueryBuilder().or().and().eq("k1", "v1").notEq("k2", new IPv4("1.2.3.4")).end().and().eq("k3", 1234).end().end().build();
        String json = objectMapper.writeValueAsString(node);
        assertEquals("{\"or\":{\"children\":[{\"and\":{\"children\":[{\"eq\":{\"key\":\"k1\",\"type\":\"string\",\"value\":\"v1\"}},{\"neq\":{\"key\":\"k2\",\"type\":\"ipv4\",\"value\":\"1.2.3.4\"}}]}},{\"and\":{\"children\":[{\"eq\":{\"key\":\"k3\",\"type\":\"integer\",\"value\":\"1234\"}}]}}]}}", json);
    }
}
