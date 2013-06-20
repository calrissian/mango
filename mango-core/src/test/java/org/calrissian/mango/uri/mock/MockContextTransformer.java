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
import org.calrissian.mango.uri.transform.ContextTransformer;

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
