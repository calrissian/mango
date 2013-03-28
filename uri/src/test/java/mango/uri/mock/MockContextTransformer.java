package mango.uri.mock;

import com.google.common.net.MediaType;
import mango.uri.domain.ResolvedItem;
import mango.uri.exception.ContextTransformException;
import mango.uri.transform.ContextTransformer;

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
