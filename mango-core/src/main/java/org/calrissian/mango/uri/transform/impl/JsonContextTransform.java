package org.calrissian.mango.uri.transform.impl;

import com.google.common.net.MediaType;
import org.calrissian.mango.uri.domain.ResolvedItem;
import org.calrissian.mango.uri.exception.ContextTransformException;
import org.calrissian.mango.uri.transform.ContextTransformer;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.ByteArrayInputStream;
import java.io.IOException;

public class JsonContextTransform implements ContextTransformer<Object> {

    public static final MediaType CONTENT_TYPE = MediaType.JSON_UTF_8;

    protected final ObjectMapper objectMapper;

    public JsonContextTransform(ObjectMapper objectMapper) {

        this.objectMapper = objectMapper;
    }

    @Override
    public ResolvedItem transform(Object obj) throws ContextTransformException {

        try {

            byte[] json = objectMapper.writeValueAsBytes(obj);

            return new ResolvedItem(CONTENT_TYPE, null, new ByteArrayInputStream(json));

        } catch (IOException e) {

            throw new ContextTransformException(e);
        }
    }

    @Override
    public MediaType getMediaType(Object obj) throws ContextTransformException {
        return CONTENT_TYPE;
    }

    @Override
    public String getContextName() {
        return "json";
    }
}
