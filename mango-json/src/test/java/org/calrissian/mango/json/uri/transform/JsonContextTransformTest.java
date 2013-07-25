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
package org.calrissian.mango.json.uri.transform;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.net.MediaType;
import org.calrissian.mango.uri.domain.ResolvedItem;
import org.calrissian.mango.uri.transform.ContextTransformService;
import org.calrissian.mango.uri.transform.ContextTransformer;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;

public class JsonContextTransformTest {

    ContextTransformService contextTransformService;

    @Before
    public void setUp() {

        Collection<ContextTransformer> transformers = new ArrayList<ContextTransformer>();
        transformers.add(new JsonContextTransform(new ObjectMapper()));

        contextTransformService = new ContextTransformService(transformers, null);

    }

    @Test
    public void testJsonContextTransform() throws Exception {

        ArrayList<String> items = new ArrayList<String>();
        items.add("item1");
        items.add("item2");

        ResolvedItem result = contextTransformService.transform("json", items);

        byte[] json = new ObjectMapper().writeValueAsBytes(items);

        byte[] actualResult = new byte[json.length];
        result.getObject().read(actualResult);


        assertEquals(new String(actualResult), new String(json));
        assertNull(result.getAdditionalHeaders());
        assertEquals(MediaType.JSON_UTF_8, result.getContentType());
    }



}
