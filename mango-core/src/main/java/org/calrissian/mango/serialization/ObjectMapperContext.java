package org.calrissian.mango.serialization;

import org.codehaus.jackson.map.ObjectMapper;

public class ObjectMapperContext {

    protected static ObjectMapperContext instance = new ObjectMapperContext();

    public static ObjectMapperContext getInstance() {
        return instance;
    }

    protected ObjectMapper objectMapper;

    public ObjectMapperContext() {
        objectMapper = new ObjectMapper();
    }

    public ObjectMapper getObjectMapper() {

        return objectMapper;
    }
}
