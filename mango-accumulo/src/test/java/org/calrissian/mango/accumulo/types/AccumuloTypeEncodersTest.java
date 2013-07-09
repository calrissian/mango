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
package org.calrissian.mango.accumulo.types;

import org.calrissian.mango.domain.IPv4;
import org.calrissian.mango.types.TypeEncoder;
import org.calrissian.mango.types.exception.TypeDecodingException;
import org.calrissian.mango.types.exception.TypeEncodingException;
import org.junit.Test;

import java.net.URI;
import java.util.Date;


import static org.calrissian.mango.accumulo.types.AccumuloTypeEncoders.*;
import static org.junit.Assert.assertEquals;

public class AccumuloTypeEncodersTest {

    private static<T> void verifyBasicFunctionality(String alias, T testObject, TypeEncoder<T, String> encoder) throws TypeEncodingException, TypeDecodingException {
        assertEquals(alias, encoder.getAlias());
        assertEquals(testObject.getClass(), encoder.resolves());

        //test encode decode returns same value
        assertEquals(testObject, encoder.decode(encoder.encode(testObject)));
    }

    @Test
    public void testBasicFunctionality() throws Exception{
        verifyBasicFunctionality("boolean", true, booleanEncoder());
        verifyBasicFunctionality("byte", (byte) 3, byteEncoder());
        verifyBasicFunctionality("date", new Date(), dateEncoder());
        verifyBasicFunctionality("double", -1.5D, doubleEncoder());
        verifyBasicFunctionality("float", -1.5F, floatEncoder());
        verifyBasicFunctionality("integer", 3, integerEncoder());
        verifyBasicFunctionality("ipv4", new IPv4("192.168.1.1"), ipv4Encoder());
        verifyBasicFunctionality("long", 3L, longEncoder());
        verifyBasicFunctionality("string", "testing", stringEncoder());
        verifyBasicFunctionality("uri", new URI("http://testing.org"), uriEncoder());

        verifyBasicFunctionality("boolean", true, reverseBooleanEncoder());
        verifyBasicFunctionality("byte", (byte) 3, reverseByteEncoder());
        verifyBasicFunctionality("date", new Date(), reverseDateEncoder());
        verifyBasicFunctionality("double", -1.5D, reverseDoubleEncoder());
        verifyBasicFunctionality("float", -1.5F, reverseFloatEncoder());
        verifyBasicFunctionality("integer", 3, reverseIntegerEncoder());
        verifyBasicFunctionality("ipv4", new IPv4("192.168.1.1"), reverseIPv4Encoder());
        verifyBasicFunctionality("long", 3L, reverseLongEncoder());
    }

    @Test
    public void testCorrectEncoding () throws Exception {

        assertEquals("1", booleanEncoder().encode(true));
        assertEquals("0", booleanEncoder().encode(false));

        assertEquals("03", byteEncoder().encode((byte) 3));

        assertEquals("800000000000000a", dateEncoder().encode(new Date(10)));

        assertEquals("bff8000000000000", doubleEncoder().encode(1.5D));
        assertEquals("3ff8000000000000", doubleEncoder().encode(-1.5D));

        assertEquals("bfc00000", floatEncoder().encode(1.5F));
        assertEquals("3fc00000", floatEncoder().encode(-1.5F));

        assertEquals("80000003", integerEncoder().encode(3));
        assertEquals("7ffffffd", integerEncoder().encode(-3));

        assertEquals("c0a80101", ipv4Encoder().encode(new IPv4("192.168.1.1")));
        assertEquals("ffffffff", ipv4Encoder().encode(new IPv4("255.255.255.255")));

        assertEquals("8000000000000003", longEncoder().encode(3L));
        assertEquals("7ffffffffffffffd", longEncoder().encode(-3L));

        assertEquals("test", stringEncoder().encode("test"));

        assertEquals("http://testing.org", uriEncoder().encode(new URI("http://testing.org")));
    }

    @Test
    public void testCorrectReverseEncoding () throws Exception {

        assertEquals("0", reverseBooleanEncoder().encode(true));
        assertEquals("1", reverseBooleanEncoder().encode(false));

        assertEquals("fc", reverseByteEncoder().encode((byte) 3));

        assertEquals("7ffffffffffffff5", reverseDateEncoder().encode(new Date(10)));

        assertEquals("4007ffffffffffff", reverseDoubleEncoder().encode(1.5D));
        assertEquals("c007ffffffffffff", reverseDoubleEncoder().encode(-1.5D));

        assertEquals("403fffff", reverseFloatEncoder().encode(1.5F));
        assertEquals("c03fffff", reverseFloatEncoder().encode(-1.5F));

        assertEquals("7ffffffc", reverseIntegerEncoder().encode(3));
        assertEquals("80000002", reverseIntegerEncoder().encode(-3));

        assertEquals("3f57fefe", reverseIPv4Encoder().encode(new IPv4("192.168.1.1")));
        assertEquals("00000000", reverseIPv4Encoder().encode(new IPv4("255.255.255.255")));

        assertEquals("7ffffffffffffffc", reverseLongEncoder().encode(3L));
        assertEquals("8000000000000002", reverseLongEncoder().encode(-3L));
    }
}
