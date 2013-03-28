package org.calrissian.mango.uri.common.mock;

import com.google.common.net.MediaType;
import org.calrissian.mango.uri.common.domain.ResolvedItem;
import org.calrissian.mango.uri.common.exception.ContextTransformException;
import org.calrissian.mango.uri.common.transform.ContextTransformer;

import java.io.ByteArrayInputStream;


public class MockContextTransformer implements ContextTransformer<Object> {

    public static final MediaType MEDIA_TYPE = MediaType.PLAIN_TEXT_UTF_8;

    public static final String TRANSFORMED_OUTPUT = "TRANSFORMED_OUTPUT";

    public static final String CONTEXT_NAME = "mockTransformer";

    @Override
    public String getContextName() {
        return CONTEXT_NAME;
    }

    @Override
    public ResolvedItem transform(Object obj) throws ContextTransformException {
        return new ResolvedItem(MEDIA_TYPE, null, new ByteArrayInputStream(TRANSFORMED_OUTPUT.getBytes()));
    }

    @Override
    public MediaType getMediaType(Object obj) throws ContextTransformException {
        return MEDIA_TYPE;
    }
}
