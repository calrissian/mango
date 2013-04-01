package org.calrissian.commons.serialization;

import org.codehaus.jackson.map.ObjectMapper;

public class ObjectMapperContext {

    protected static ObjectMapperContext instance;

    public static synchronized ObjectMapperContext getInstance() {

        if(instance == null) {
            instance = new ObjectMapperContext();
        }

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
