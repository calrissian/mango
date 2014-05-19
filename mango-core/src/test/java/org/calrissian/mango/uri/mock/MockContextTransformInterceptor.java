/*
 * Copyright (C) 2013 The Calrissian Authors
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
package org.calrissian.mango.uri.mock;

import com.google.common.net.MediaType;
import org.calrissian.mango.uri.domain.ResolvedItem;
import org.calrissian.mango.uri.exception.ContextTransformException;
import org.calrissian.mango.uri.transform.interceptor.ContextTransformInterceptor;

import java.io.ByteArrayInputStream;

public class MockContextTransformInterceptor implements ContextTransformInterceptor<Integer> {

    public static final MediaType MEDIA_TYPE = MediaType.ANY_TYPE;

    public static final String OUTPUT_STRING = "INTERCEPTED_OUTPUT";

    @SuppressWarnings("rawtypes")
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
