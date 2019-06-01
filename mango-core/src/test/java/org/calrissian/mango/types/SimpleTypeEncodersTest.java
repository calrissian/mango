/*
 * Copyright (C) 2017 The Calrissian Authors
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


import com.google.common.primitives.UnsignedInteger;
import com.google.common.primitives.UnsignedLong;
import org.calrissian.mango.domain.entity.EntityIdentifier;
import org.calrissian.mango.domain.event.EventIdentifier;
import org.calrissian.mango.net.IPv4;
import org.calrissian.mango.net.IPv6;
import org.junit.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URI;
import java.time.Instant;
import java.util.Date;

import static org.calrissian.mango.io.Serializables.deserialize;
import static org.calrissian.mango.io.Serializables.serialize;
import static org.calrissian.mango.net.MoreInetAddresses.forIPv4String;
import static org.calrissian.mango.net.MoreInetAddresses.forIPv6String;
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
        verifyBasicFunctionality(INSTANT_ALIAS, Instant.now(), instantEncoder());
        verifyBasicFunctionality(DOUBLE_ALIAS, 0.0D, doubleEncoder());
        verifyBasicFunctionality(DOUBLE_ALIAS, -0.0D, doubleEncoder());
        verifyBasicFunctionality(DOUBLE_ALIAS, 1.5D, doubleEncoder());
        verifyBasicFunctionality(FLOAT_ALIAS, 0.0F, floatEncoder());
        verifyBasicFunctionality(FLOAT_ALIAS, -0.0F, floatEncoder());
        verifyBasicFunctionality(FLOAT_ALIAS, 1.5F, floatEncoder());
        verifyBasicFunctionality(INTEGER_ALIAS, 3, integerEncoder());
        verifyBasicFunctionality(LONG_ALIAS, 3L, longEncoder());
        verifyBasicFunctionality(STRING_ALIAS, "testing", stringEncoder());
        verifyBasicFunctionality(URI_ALIAS, new URI("http://testing.org"), uriEncoder());
        verifyBasicFunctionality(BIGINTEGER_ALIAS, BigInteger.valueOf(Long.MAX_VALUE).pow(2), bigIntegerEncoder());
        verifyBasicFunctionality(BIGDECIMAL_ALIAS, BigDecimal.valueOf(Double.MAX_VALUE).pow(2), bigDecimalEncoder());
        verifyBasicFunctionality(BIGDECIMAL_ALIAS, new BigDecimal("1.00000"), bigDecimalEncoder());
        verifyBasicFunctionality(INET4_ALIAS, forIPv4String("192.168.1.1"), inet4AddressEncoder());
        verifyBasicFunctionality(INET6_ALIAS, forIPv6String("::192.168.1.1"), inet6AddressEncoder());
        verifyBasicFunctionality(INET6_ALIAS, forIPv6String("::ffff:192.168.1.1"), inet6AddressEncoder());
        verifyBasicFunctionality(IPV4_ALIAS, IPv4.fromString("192.168.1.1"), ipv4Encoder());
        verifyBasicFunctionality(IPV6_ALIAS, IPv6.fromString("::192.168.1.1"), ipv6Encoder());
        verifyBasicFunctionality(IPV6_ALIAS, IPv6.fromString("::ffff:192.168.1.1"), ipv6Encoder());
        verifyBasicFunctionality(ENTITY_IDENTIFIER_ALIAS, new EntityIdentifier("type", "id"), entityIdentifierEncoder());
        verifyBasicFunctionality(ENTITY_IDENTIFIER_ALIAS, new EntityIdentifier("", ""), entityIdentifierEncoder());
        verifyBasicFunctionality(UNSIGNEDINTEGER_ALIAS, UnsignedInteger.fromIntBits(3), unsignedIntegerEncoder());
        verifyBasicFunctionality(UNSIGNEDINTEGER_ALIAS, UnsignedInteger.MAX_VALUE, unsignedIntegerEncoder());
        verifyBasicFunctionality(UNSIGNEDLONG_ALIAS, UnsignedLong.fromLongBits(3), unsignedLongEncoder());
        verifyBasicFunctionality(UNSIGNEDLONG_ALIAS, UnsignedLong.MAX_VALUE, unsignedLongEncoder());
    }

    @Test
    public void testCorrectEncoding() throws Exception {
        assertEquals("true", booleanEncoder().encode(true));
        assertEquals("false", booleanEncoder().encode(false));

        assertEquals("3", byteEncoder().encode((byte) 3));

        assertEquals("10", dateEncoder().encode(new Date(10)));

        assertEquals("1970-01-01T00:00:00.010Z", instantEncoder().encode(Instant.ofEpochMilli(10)));

        assertEquals("0.0", doubleEncoder().encode(0.0D));
        assertEquals("-0.0", doubleEncoder().encode(-0.0D));
        assertEquals("1.5", doubleEncoder().encode(1.5D));

        assertEquals("0.0", floatEncoder().encode(0.0F));
        assertEquals("-0.0", floatEncoder().encode(-0.0F));
        assertEquals("1.5", floatEncoder().encode(1.5F));

        assertEquals("3", integerEncoder().encode(3));

        assertEquals("3", longEncoder().encode(3L));

        assertEquals("test", stringEncoder().encode("test"));

        assertEquals("http://testing.org", uriEncoder().encode(new URI("http://testing.org")));

        assertEquals("85070591730234615847396907784232501249", bigIntegerEncoder().encode(BigInteger.valueOf(Long.MAX_VALUE).pow(2)));

        assertEquals("entity://type#id", entityIdentifierEncoder().encode(new EntityIdentifier("type", "id")));

        assertEquals("event://type#id@9223372036854775807", eventIdentifierEncoder().encode(new EventIdentifier("type", "id", Long.MAX_VALUE)));

        assertEquals("192.168.1.1", ipv4Encoder().encode(IPv4.fromString("192.168.1.1")));

        assertEquals("::c0a8:101", ipv6Encoder().encode(IPv6.fromString("::192.168.1.1")));
        assertEquals("::ffff:c0a8:101", ipv6Encoder().encode(IPv6.fromString("::ffff:192.168.1.1")));

        assertEquals("192.168.1.1", inet4AddressEncoder().encode(forIPv4String("192.168.1.1")));

        assertEquals("::c0a8:101", inet6AddressEncoder().encode(forIPv6String("::192.168.1.1")));
        assertEquals("::ffff:c0a8:101", inet6AddressEncoder().encode(forIPv6String("::ffff:192.168.1.1")));

        assertEquals("3", unsignedIntegerEncoder().encode(UnsignedInteger.fromIntBits(3)));
        assertEquals("4294967295", unsignedIntegerEncoder().encode(UnsignedInteger.MAX_VALUE));

        assertEquals("3", unsignedLongEncoder().encode(UnsignedLong.fromLongBits(3)));
        assertEquals("18446744073709551615", unsignedLongEncoder().encode(UnsignedLong.MAX_VALUE));
    }

    @Test
    public void testSerialization() throws Exception {
        deserialize(serialize(SIMPLE_TYPES));
    }
}
