package org.calrissian.mango.uri.transform.impl;

import com.google.common.net.MediaType;
import org.calrissian.mango.uri.domain.ResolvedItem;
import org.calrissian.mango.uri.exception.ContextTransformException;
import org.calrissian.mango.uri.transform.ContextTransformService;
import org.calrissian.mango.uri.transform.ContextTransformer;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
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
    public void testJsonContextTransform() throws ContextTransformException, JsonGenerationException, IOException {

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
