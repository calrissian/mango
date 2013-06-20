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
