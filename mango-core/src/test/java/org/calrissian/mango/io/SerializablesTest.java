package org.calrissian.mango.io;


import com.google.common.collect.Lists;
import org.junit.Test;

import java.io.IOException;
import java.io.Serializable;

import static org.calrissian.mango.io.Serializables.*;
import static org.junit.Assert.assertEquals;

public class SerializablesTest {

    private static final Serializable testObject = Lists.newArrayList(1,2,3,4,5);

    @Test
    public void testSimpleSerialization() throws Exception {
        byte [] serialized = serialize(testObject);
        assertEquals(testObject, deserialize(serialized));
    }

    @Test
    public void testCompressedSerialization() throws Exception {
        byte [] serialized = serialize(testObject, true);
        assertEquals(testObject, deserialize(serialized, true));
    }

    @Test
    public void testBase64Encoding() throws Exception {
        byte[] base64 = toBase64(testObject);
        assertEquals(testObject, fromBase64(base64));
    }
}
