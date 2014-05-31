package org.calrissian.mango.io;


import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class SerializablesTest {


    @Test
    public void testBase64Encoding() throws IOException, ClassNotFoundException {

        Integer integer = 5;
        byte[] base64 = Serializables.toBase64(integer);
        assertEquals(integer, Serializables.fromBase64(base64));
    }
}
