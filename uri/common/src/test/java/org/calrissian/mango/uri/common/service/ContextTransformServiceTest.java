package org.calrissian.mango.uri.common.service;

import org.calrissian.mango.uri.common.domain.ResolvedItem;
import org.calrissian.mango.uri.common.exception.ContextTransformException;
import org.calrissian.mango.uri.common.mock.MockContextTransformInterceptor;
import org.calrissian.mango.uri.common.mock.MockContextTransformer;
import org.calrissian.mango.uri.common.transform.ContextTransformService;
import org.calrissian.mango.uri.common.transform.ContextTransformer;
import org.calrissian.mango.uri.common.transform.interceptor.ContextTransformInterceptor;
import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;

import static junit.framework.Assert.assertEquals;

public class ContextTransformServiceTest {

    ContextTransformService transformService;

    @Before
    public void setUp() {

        Collection<ContextTransformer> transformers = new ArrayList<ContextTransformer>();
        Collection<ContextTransformInterceptor> interceptors = new ArrayList<ContextTransformInterceptor>();

        transformers.add(new MockContextTransformer());
        interceptors.add(new MockContextTransformInterceptor());


        transformService = new ContextTransformService(transformers, interceptors);
    }

    @Test
    public void testTransformer() throws ContextTransformException, IOException {

        ResolvedItem actualItem = transformService.transform(MockContextTransformer.CONTEXT_NAME, "TEST_STRING");

        assertEquals(MockContextTransformer.MEDIA_TYPE, actualItem.getContentType());
        assertEquals(MockContextTransformer.TRANSFORMED_OUTPUT, streamToString(actualItem.getObject()));
    }

    @Test
    public void testTransformInterceptor() throws ContextTransformException, IOException {

        ResolvedItem actualItem = transformService.transform(MockContextTransformer.CONTEXT_NAME, 1);

        assertEquals(MockContextTransformInterceptor.MEDIA_TYPE, actualItem.getContentType());
        assertEquals(MockContextTransformInterceptor.OUTPUT_STRING, streamToString(actualItem.getObject()));
    }

    private String streamToString(InputStream is) throws IOException {
        if (is != null) {
            Writer writer = new StringWriter();

            char[] buffer = new char[1024];
            try {
                Reader reader = new BufferedReader(
                        new InputStreamReader(is, "UTF-8"));
                int n;
                while ((n = reader.read(buffer)) != -1) {
                    writer.write(buffer, 0, n);
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            } catch (IOException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            } finally {
                is.close();
            }
            return writer.toString();
        } else {
            return "";
        }
    }

    @Test
    public void uriTest() throws URISyntaxException {
        new URI("ikeai:event://hello");
    }


}
