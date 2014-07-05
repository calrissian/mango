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
package org.calrissian.mango.types;


import org.calrissian.mango.domain.entity.EntityRelationship;
import org.calrissian.mango.domain.ip.IPv4;
import org.junit.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URI;
import java.util.Date;

import static org.calrissian.mango.types.SimpleTypeEncoders.*;
import static org.calrissian.mango.types.encoders.AliasConstants.*;
import static org.junit.Assert.assertEquals;

public class SimpleTypeEncodersTest {


    protected static <T> void verifyBasicFunctionality(String alias, T testObject, TypeEncoder<T, String> encoder) {
        assertEquals(alias, encoder.getAlias());
        assertEquals(testObject.getClass(), encoder.resolves());

        //test encode decode returns same value
        assertEquals(testObject, encoder.decode(encoder.encode(testObject)));
    }

    @Test
    public void testBasicFunctionality() throws Exception {

        verifyBasicFunctionality(BOOLEAN_ALIAS, true, booleanEncoder());
        verifyBasicFunctionality(BYTE_ALIAS, (byte) 3, byteEncoder());
        verifyBasicFunctionality(DATE_ALIAS, new Date(), dateEncoder());
        verifyBasicFunctionality(DOUBLE_ALIAS, 0.0D, doubleEncoder());
        verifyBasicFunctionality(DOUBLE_ALIAS, -0.0D, doubleEncoder());
        verifyBasicFunctionality(DOUBLE_ALIAS, 1.5D, doubleEncoder());
        verifyBasicFunctionality(FLOAT_ALIAS, 0.0F, floatEncoder());
        verifyBasicFunctionality(FLOAT_ALIAS, -0.0F, floatEncoder());
        verifyBasicFunctionality(FLOAT_ALIAS, 1.5F, floatEncoder());
        verifyBasicFunctionality(INTEGER_ALIAS, 3, integerEncoder());
        verifyBasicFunctionality(IPV4_ALIAS, new IPv4("192.168.1.1"), ipv4Encoder());
        verifyBasicFunctionality(LONG_ALIAS, 3L, longEncoder());
        verifyBasicFunctionality(STRING_ALIAS, "testing", stringEncoder());
        verifyBasicFunctionality(URI_ALIAS, new URI("http://testing.org"), uriEncoder());
        verifyBasicFunctionality(BIGINTEGER_ALIAS, BigInteger.valueOf(Long.MAX_VALUE).pow(2), bigIntegerEncoder());
        verifyBasicFunctionality(BIGDECIMAL_ALIAS, BigDecimal.valueOf(Double.MAX_VALUE).pow(2), bigDecimalEncoder());
        verifyBasicFunctionality(BIGDECIMAL_ALIAS, new BigDecimal("1.00000"), bigDecimalEncoder());
        verifyBasicFunctionality(ENTITY_RELATIONSHIP_ALIAS, new EntityRelationship("type", "id"), entityRelationshipEncoder());
    }

    @Test
    public void testCorrectEncoding() throws Exception {
        assertEquals("true", booleanEncoder().encode(true));
        assertEquals("false", booleanEncoder().encode(false));

        assertEquals("3", byteEncoder().encode((byte) 3));

        assertEquals("10", dateEncoder().encode(new Date(10)));

        assertEquals("0.0", doubleEncoder().encode(0.0D));
        assertEquals("-0.0", doubleEncoder().encode(-0.0D));
        assertEquals("1.5", doubleEncoder().encode(1.5D));

        assertEquals("0.0", floatEncoder().encode(0.0F));
        assertEquals("-0.0", floatEncoder().encode(-0.0F));
        assertEquals("1.5", floatEncoder().encode(1.5F));

        assertEquals("3", integerEncoder().encode(3));

        assertEquals("192.168.1.1", ipv4Encoder().encode(new IPv4("192.168.1.1")));

        assertEquals("3", longEncoder().encode(3L));

        assertEquals("test", stringEncoder().encode("test"));

        assertEquals("http://testing.org", uriEncoder().encode(new URI("http://testing.org")));

        assertEquals("85070591730234615847396907784232501249", bigIntegerEncoder().encode(BigInteger.valueOf(Long.MAX_VALUE).pow(2)));

        assertEquals("entity://type#id", entityRelationshipEncoder().encode(new EntityRelationship("type", "id")));
    }
}
