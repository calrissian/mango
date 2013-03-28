package mango.uri.mock;

import com.google.common.net.MediaType;
import mango.uri.domain.ResolvedItem;
import mango.uri.exception.ContextTransformException;
import mango.uri.transform.interceptor.ContextTransformInterceptor;

import java.io.ByteArrayInputStream;

public class MockContextTransformInterceptor implements ContextTransformInterceptor<Integer> {

    public static final MediaType MEDIA_TYPE = MediaType.ANY_TYPE;

    public static final String OUTPUT_STRING = "INTERCEPTED_OUTPUT";

    @Override
    public Class intercepts() {
        return Integer.class;
    }

    @Override
    public ResolvedItem transform(Integer obj) throws ContextTransformException {

        return new ResolvedItem(MEDIA_TYPE, null, new ByteArrayInputStream(OUTPUT_STRING.getBytes()));
    }

    @Override
    public MediaType getMediaType(Integer obj) throws ContextTransformException {
        return MEDIA_TYPE;
    }
}
