package org.calrissian.mango.json.tuple;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.calrissian.mango.domain.Tuple;
import org.junit.Test;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

import static org.calrissian.mango.types.SimpleTypeEncoders.SIMPLE_TYPES;
import static org.junit.Assert.assertEquals;

public class TupleDeserializerTest {

    private ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new TupleModule(SIMPLE_TYPES));


    @Test
    public void testBasicSerialization() throws IOException {

        Tuple tuple = objectMapper.readValue("{\"key\":\"key\",\"visibility\":\"visibility\",\"type\":\"string\",\"value\":\"value\",\"metadata\":[]}", Tuple.class);
        assertEquals("key", tuple.getKey());
        assertEquals("visibility", tuple.getVisibility());
        assertEquals("value", tuple.getValue());
        assertEquals(0, tuple.getMetadata().size());
    }

    @Test
    public void testSerialization_withMetadata() throws IOException {
        Tuple tuple = objectMapper.readValue("{\"key\":\"key\",\"visibility\":\"visibility\",\"type\":\"string\",\"value\":\"value\",\"metadata\":[{\"value\":\"metaVal\",\"type\":\"string\",\"key\":\"metaKey\"}]}", Tuple.class);
        assertEquals("key", tuple.getKey());
        assertEquals("visibility", tuple.getVisibility());
        assertEquals("value", tuple.getValue());
        assertEquals(1, tuple.getMetadata().size());

        Set<Map.Entry<String, Object>> entrySet = tuple.getMetadata().entrySet();
        assertEquals("metaKey", entrySet.iterator().next().getKey());
        assertEquals("metaVal", entrySet.iterator().next().getValue());
    }



}

