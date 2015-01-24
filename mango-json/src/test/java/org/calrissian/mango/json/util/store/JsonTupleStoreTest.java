/*
 * Copyright (C) 2014 The Calrissian Authors
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
package org.calrissian.mango.json.util.store;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.calrissian.mango.domain.Tuple;
import org.junit.Test;

import java.util.Collection;

import static org.calrissian.mango.json.util.store.JsonTupleStore.fromJson;
import static org.calrissian.mango.json.util.store.JsonTupleStore.toJsonString;
import static org.junit.Assert.assertEquals;

public class JsonTupleStoreTest {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void testRealisticNestedJson() {
        String json = "{ \"name\":\"Corey\", \"nestedObject\":{\"anotherNest\":{\"innerObj\":\"innerVal\"}}, \"nestedArray\":[[[\"1\"],[\"1\"]]], \"ids\":[\"5\",\"2\"], \"locations\":[{\"name\":\"Office\", \"addresses\":[{\"number\":1234,\"street\":\"BlahBlah Lane\"}]}]}";
        assertJson(json);
    }

    @Test
    public void testArrayWithNativeTypes() {
        String json = "{ \"ids\":[\"5\",\"2\"]}";
        assertJson(json);
    }

    @Test
    public void testNestedObject() {
        String json = "{\"nestedObject\":{\"anotherNest\":{\"innerObj\":\"innerVal\"}}}";
        assertJson(json);
    }

    @Test
    public void testArrayNestedWithOtherArrays() {
        String json = "{\"nestedArray\":[[[\"1\"],[\"2\"]]]}";
        assertJson(json);
    }

    @Test
    public void testArrayNestedWithHeterogenousObjects() {
        String json = "{ \"locations\":[{\"name\":\"Office\", \"addresses\":[{\"number\":1234,\"street\":{\"name\":\"BlahBlah Lane\"}}]}]}}";
        assertJson(json);
    }


    private void assertJson(String json)  {

        try {
            ObjectNode jsonNode = (ObjectNode)objectMapper.readTree(json);

            // parse original json
            Collection<Tuple> tuples = fromJson(jsonNode);


            // turn back into json
            ObjectNode actualNode = (ObjectNode)objectMapper.readTree(toJsonString(tuples, objectMapper));

            System.out.println(toJsonString(tuples, objectMapper));


            // verify nodes are the same
            assertEquals(actualNode, jsonNode);

        } catch(Exception e) {
            throw new RuntimeException(e);
        }

    }
}
