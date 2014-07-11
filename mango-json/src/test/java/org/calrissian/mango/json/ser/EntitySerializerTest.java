package org.calrissian.mango.json.ser;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.calrissian.mango.domain.Tuple;
import org.calrissian.mango.domain.entity.BaseEntity;
import org.calrissian.mango.domain.entity.Entity;
import org.calrissian.mango.json.MangoModule;
import org.junit.Test;

import static org.calrissian.mango.types.SimpleTypeEncoders.SIMPLE_TYPES;
import static org.junit.Assert.assertEquals;

public class EntitySerializerTest {

    private ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new MangoModule(SIMPLE_TYPES));

    @Test
    public void testSerializes() throws JsonProcessingException {

        Entity entity = new BaseEntity("type", "id");
        entity.put(new Tuple("key", "value"));
        entity.put(new Tuple("key1", "valu1"));

        String serialized = objectMapper.writeValueAsString(entity);

        assertEquals(serialized, "{\"type\":\"type\",\"id\":\"id\",\"tuples\":{\"key1\":[{\"key\":\"key1\",\"type\":\"string\",\"value\":\"valu1\",\"metadata\":[]}],\"key\":[{\"key\":\"key\",\"type\":\"string\",\"value\":\"value\",\"metadata\":[]}]}}");
    }

}
