package org.calrissian.mango.uri.common.mock;

import com.google.common.net.MediaType;
import org.calrissian.mango.uri.common.domain.ResolvedItem;
import org.calrissian.mango.uri.common.exception.ContextTransformException;
import org.calrissian.mango.uri.common.transform.interceptor.ContextTransformInterceptor;

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
