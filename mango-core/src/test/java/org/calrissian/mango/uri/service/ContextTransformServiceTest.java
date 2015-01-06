/*
 * Copyright (C) 2014 The Calrissian Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.calrissian.mango.uri.service;

import org.calrissian.mango.uri.domain.ResolvedItem;
import org.calrissian.mango.uri.exception.ContextTransformException;
import org.calrissian.mango.uri.mock.MockContextTransformInterceptor;
import org.calrissian.mango.uri.mock.MockContextTransformer;
import org.calrissian.mango.uri.transform.ContextTransformService;
import org.calrissian.mango.uri.transform.ContextTransformer;
import org.calrissian.mango.uri.transform.interceptor.ContextTransformInterceptor;
import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;

import static org.junit.Assert.assertEquals;

public class ContextTransformServiceTest {

    ContextTransformService transformService;

    @SuppressWarnings("rawtypes")
    @Before
    public void setUp() {

        Collection<ContextTransformer> transformers = new ArrayList<>();
        Collection<ContextTransformInterceptor> interceptors = new ArrayList<>();

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
            } catch (IOException e) {
                throw new RuntimeException(e);
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
