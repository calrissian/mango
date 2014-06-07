package org.calrissian.mango.json.tuple;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.calrissian.mango.domain.Tuple;
import org.junit.Test;

import static org.calrissian.mango.types.SimpleTypeEncoders.SIMPLE_TYPES;
import static org.junit.Assert.assertEquals;

public class TupleSerializerTest {

    private ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new TupleModule(SIMPLE_TYPES));

    @Test
    public void testBasicSerialization() throws Exception {
        Tuple tuple = new Tuple("key", "value");
        String json = objectMapper.writeValueAsString(tuple);
        assertEquals(json, "{\"key\":\"key\",\"type\":\"string\",\"value\":\"value\",\"metadata\":[]}");
    }

    @Test
    public void testSerialization_withMetadata() throws Exception {
        Tuple tuple = new Tuple("key", "value");
        tuple.setMetadataValue("metaKey", "metaVal");
        String json = objectMapper.writeValueAsString(tuple);
        assertEquals(json, "{\"key\":\"key\",\"type\":\"string\",\"value\":\"value\",\"metadata\":[{\"value\":\"metaVal\",\"type\":\"string\",\"key\":\"metaKey\"}]}");
    }

}
