/*
 * Copyright (C) 2019 The Calrissian Authors
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
import static org.calrissian.mango.types.LexiTypeEncoders.*;
import static org.calrissian.mango.types.SimpleTypeEncodersTest.verifyBasicFunctionality;
import static org.calrissian.mango.types.encoders.AliasConstants.*;
import static org.junit.Assert.assertEquals;

public class LexiTypeEncodersTest {

    @Test
    public void testBasicFunctionality() throws Exception {
        verifyBasicFunctionality(BOOLEAN_ALIAS, true, booleanEncoder());
        verifyBasicFunctionality(BYTE_ALIAS, (byte) 3, byteEncoder());
        verifyBasicFunctionality(DATE_ALIAS, new Date(), dateEncoder());
        verifyBasicFunctionality(INSTANT_ALIAS, Instant.now(), instantEncoder());
        verifyBasicFunctionality(DOUBLE_ALIAS, 0.0D, doubleEncoder());
        verifyBasicFunctionality(DOUBLE_ALIAS, -0.0D, doubleEncoder());
        verifyBasicFunctionality(DOUBLE_ALIAS, -1.5D, doubleEncoder());
        verifyBasicFunctionality(FLOAT_ALIAS, 0.0F, floatEncoder());
        verifyBasicFunctionality(FLOAT_ALIAS, -0.0F, floatEncoder());
        verifyBasicFunctionality(FLOAT_ALIAS, -1.5F, floatEncoder());
        verifyBasicFunctionality(INTEGER_ALIAS, 3, integerEncoder());
        verifyBasicFunctionality(LONG_ALIAS, 3L, longEncoder());
        verifyBasicFunctionality(STRING_ALIAS, "testing", stringEncoder());
        verifyBasicFunctionality(URI_ALIAS, new URI("http://testing.org"), uriEncoder());
        verifyBasicFunctionality(BIGINTEGER_ALIAS, new BigInteger(Integer.toString(Integer.MAX_VALUE)).pow(10), bigIntegerEncoder());
        verifyBasicFunctionality(BIGDECIMAL_ALIAS, BigDecimal.valueOf(Double.MAX_VALUE).pow(2), bigDecimalEncoder());
        verifyBasicFunctionality(BIGDECIMAL_ALIAS, new BigDecimal("1.00000"), bigDecimalEncoder());
        verifyBasicFunctionality(BIGDECIMAL_ALIAS, new BigDecimal("0.00000"), bigDecimalEncoder()); //zero is special case
        verifyBasicFunctionality(IPV4_ALIAS, IPv4.fromString("192.168.1.1"), ipv4Encoder());
        verifyBasicFunctionality(IPV6_ALIAS, IPv6.fromString("::192.168.1.1"), ipv6Encoder());
        verifyBasicFunctionality(IPV6_ALIAS, IPv6.fromString("::ffff:192.168.1.1"), ipv6Encoder());
        verifyBasicFunctionality(INET4_ALIAS, forIPv4String("192.168.1.1"), inet4AddressEncoder());
        verifyBasicFunctionality(INET6_ALIAS, forIPv6String("::192.168.1.1"), inet6AddressEncoder());
        verifyBasicFunctionality(INET6_ALIAS, forIPv6String("::ffff:192.168.1.1"), inet6AddressEncoder());
        verifyBasicFunctionality(ENTITY_IDENTIFIER_ALIAS, new EntityIdentifier("type", "id"), entityIdentifierEncoder());
        verifyBasicFunctionality(ENTITY_IDENTIFIER_ALIAS, new EntityIdentifier("", ""), entityIdentifierEncoder());
        verifyBasicFunctionality(EVENT_IDENTIFIER_ALIAS, new EventIdentifier("type", "id", Long.MAX_VALUE), eventIdentifierEncoder());
        verifyBasicFunctionality(EVENT_IDENTIFIER_ALIAS, new EventIdentifier("", "", 0), eventIdentifierEncoder());
        verifyBasicFunctionality(UNSIGNEDINTEGER_ALIAS, UnsignedInteger.fromIntBits(3), unsignedIntegerEncoder());
        verifyBasicFunctionality(UNSIGNEDINTEGER_ALIAS, UnsignedInteger.MAX_VALUE, unsignedIntegerEncoder());
        verifyBasicFunctionality(UNSIGNEDLONG_ALIAS, UnsignedLong.fromLongBits(3), unsignedLongEncoder());
        verifyBasicFunctionality(UNSIGNEDLONG_ALIAS, UnsignedLong.MAX_VALUE, unsignedLongEncoder());

        verifyBasicFunctionality(BOOLEAN_ALIAS, true, booleanRevEncoder());
        verifyBasicFunctionality(BYTE_ALIAS, (byte) 3, byteRevEncoder());
        verifyBasicFunctionality(DATE_ALIAS, new Date(), dateRevEncoder());
        verifyBasicFunctionality(INSTANT_ALIAS, Instant.now(), instantRevEncoder());
        verifyBasicFunctionality(DOUBLE_ALIAS, 0.0D, doubleRevEncoder());
        verifyBasicFunctionality(DOUBLE_ALIAS, -0.0D, doubleRevEncoder());
        verifyBasicFunctionality(DOUBLE_ALIAS, -1.5D, doubleRevEncoder());
        verifyBasicFunctionality(FLOAT_ALIAS, 0.0F, floatRevEncoder());
        verifyBasicFunctionality(FLOAT_ALIAS, -0.0F, floatRevEncoder());
        verifyBasicFunctionality(FLOAT_ALIAS, -1.5F, floatRevEncoder());
        verifyBasicFunctionality(INTEGER_ALIAS, 3, integerRevEncoder());
        verifyBasicFunctionality(LONG_ALIAS, 3L, longRevEncoder());
        verifyBasicFunctionality(STRING_ALIAS, "testing", stringRevEncoder());
        verifyBasicFunctionality(URI_ALIAS, new URI("http://testing.org"), uriRevEncoder());
        verifyBasicFunctionality(BIGINTEGER_ALIAS, new BigInteger(Integer.toString(Integer.MAX_VALUE)).pow(10), bigIntegerRevEncoder());
        verifyBasicFunctionality(BIGDECIMAL_ALIAS, BigDecimal.valueOf(Double.MAX_VALUE).pow(2), bigDecimalRevEncoder());
        verifyBasicFunctionality(BIGDECIMAL_ALIAS, new BigDecimal("1.00000"), bigDecimalRevEncoder());
        verifyBasicFunctionality(BIGDECIMAL_ALIAS, new BigDecimal("0.00000"), bigDecimalRevEncoder()); //zero is special case
        verifyBasicFunctionality(IPV4_ALIAS, IPv4.fromString("192.168.1.1"), ipv4RevEncoder());
        verifyBasicFunctionality(IPV6_ALIAS, IPv6.fromString("::192.168.1.1"), ipv6RevEncoder());
        verifyBasicFunctionality(IPV6_ALIAS, IPv6.fromString("::ffff:192.168.1.1"), ipv6RevEncoder());
        verifyBasicFunctionality(INET4_ALIAS, forIPv4String("192.168.1.1"), inet4AddressRevEncoder());
        verifyBasicFunctionality(INET6_ALIAS, forIPv6String("::192.168.1.1"), inet6AddressRevEncoder());
        verifyBasicFunctionality(INET6_ALIAS, forIPv6String("::ffff:192.168.1.1"), inet6AddressRevEncoder());
        verifyBasicFunctionality(ENTITY_IDENTIFIER_ALIAS, new EntityIdentifier("type", "id"), entityIdentifierRevEncoder());
        verifyBasicFunctionality(ENTITY_IDENTIFIER_ALIAS, new EntityIdentifier("", ""), entityIdentifierRevEncoder());
        verifyBasicFunctionality(EVENT_IDENTIFIER_ALIAS, new EventIdentifier("type", "id", Long.MAX_VALUE), eventIdentifierRevEncoder());
        verifyBasicFunctionality(EVENT_IDENTIFIER_ALIAS, new EventIdentifier("", "", 0), eventIdentifierRevEncoder());
        verifyBasicFunctionality(UNSIGNEDINTEGER_ALIAS, UnsignedInteger.fromIntBits(3), unsignedIntegerRevEncoder());
        verifyBasicFunctionality(UNSIGNEDINTEGER_ALIAS, UnsignedInteger.MAX_VALUE, unsignedIntegerRevEncoder());
        verifyBasicFunctionality(UNSIGNEDLONG_ALIAS, UnsignedLong.fromLongBits(3), unsignedLongRevEncoder());
        verifyBasicFunctionality(UNSIGNEDLONG_ALIAS, UnsignedLong.MAX_VALUE, unsignedLongRevEncoder());
    }

    @Test
    public void testCorrectEncoding() throws Exception {

        assertEquals("1", booleanEncoder().encode(true));
        assertEquals("0", booleanEncoder().encode(false));

        assertEquals("03", byteEncoder().encode((byte) 3));

        assertEquals("800000000000000a", dateEncoder().encode(new Date(10)));

        assertEquals("7ffffffffffffff5bb9ac9ff", instantEncoder().encode(Instant.ofEpochSecond(-10, 0).minusNanos(1)));
        assertEquals("7ffffffffffffff680000000", instantEncoder().encode(Instant.ofEpochSecond(-10, 0)));
        assertEquals("7fffffffffffffffbb02337f", instantEncoder().encode(Instant.ofEpochMilli(-10).minusNanos(1)));
        assertEquals("7fffffffffffffffbb023380", instantEncoder().encode(Instant.ofEpochMilli(-10)));
        assertEquals("800000000000000080989680", instantEncoder().encode(Instant.ofEpochMilli(10)));
        assertEquals("800000000000000080989681", instantEncoder().encode(Instant.ofEpochMilli(10).plusNanos(1)));
        assertEquals("800000000000000a80000000", instantEncoder().encode(Instant.ofEpochSecond(10, 0)));
        assertEquals("800000000000000a80000001", instantEncoder().encode(Instant.ofEpochSecond(10, 0).plusNanos(1)));

        assertEquals("bff8000000000000", doubleEncoder().encode(1.5D));
        assertEquals("8000000000000000", doubleEncoder().encode(0.0));
        assertEquals("7fffffffffffffff", doubleEncoder().encode(-0.0));
        assertEquals("4007ffffffffffff", doubleEncoder().encode(-1.5D));

        assertEquals("bfc00000", floatEncoder().encode(1.5F));
        assertEquals("80000000", floatEncoder().encode(0.0F));
        assertEquals("7fffffff", floatEncoder().encode(-0.0F));
        assertEquals("403fffff", floatEncoder().encode(-1.5F));

        assertEquals("80000003", integerEncoder().encode(3));
        assertEquals("7ffffffd", integerEncoder().encode(-3));

        assertEquals("8000000000000003", longEncoder().encode(3L));
        assertEquals("7ffffffffffffffd", longEncoder().encode(-3L));

        assertEquals("test", stringEncoder().encode("test"));

        assertEquals("http://testing.org", uriEncoder().encode(new URI("http://testing.org")));

        assertEquals("800000103fffffffffffffff0000000000000001", bigIntegerEncoder().encode(BigInteger.valueOf(Long.MAX_VALUE).pow(2)));
        assertEquals("7ffffff0c000000000000000ffffffffffffffff", bigIntegerEncoder().encode(BigInteger.valueOf(Long.MAX_VALUE).pow(2).negate()));

        //0 is a special case that requires different handling to preserve the scale.
        assertEquals("10000000000000000", bigDecimalEncoder().encode(new BigDecimal("0.0000000")));
        assertEquals("180000268323170060713109998320439596646649", bigDecimalEncoder().encode(BigDecimal.valueOf(Double.MAX_VALUE).pow(2)));
        assertEquals("07ffffd98676829939286890001679560403353351", bigDecimalEncoder().encode(BigDecimal.valueOf(Double.MAX_VALUE).pow(2).negate()));
        assertEquals("1800000001000", bigDecimalEncoder().encode(new BigDecimal("1.000")));
        assertEquals("1800000001", bigDecimalEncoder().encode(new BigDecimal("1")));

        assertEquals("c0a80101", ipv4Encoder().encode(IPv4.fromString("192.168.1.1")));
        assertEquals("ffffffff", ipv4Encoder().encode(IPv4.fromString("255.255.255.255")));

        assertEquals("000000000000000000000000c0a80101", ipv6Encoder().encode(IPv6.fromString("::192.168.1.1")));
        assertEquals("00000000000000000000ffffc0a80101", ipv6Encoder().encode(IPv6.fromString("::ffff:192.168.1.1")));
        assertEquals("ffffffffffffffffffffffffffffffff", ipv6Encoder().encode(IPv6.fromString("ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff")));

        assertEquals("c0a80101", inet4AddressEncoder().encode(forIPv4String("192.168.1.1")));
        assertEquals("ffffffff", inet4AddressEncoder().encode(forIPv4String("255.255.255.255")));

        assertEquals("000000000000000000000000c0a80101", inet6AddressEncoder().encode(forIPv6String("::192.168.1.1")));
        assertEquals("00000000000000000000ffffc0a80101", inet6AddressEncoder().encode(forIPv6String("::ffff:192.168.1.1")));
        assertEquals("ffffffffffffffffffffffffffffffff", inet6AddressEncoder().encode(forIPv6String("ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff")));

        assertEquals("entity://type#id", entityIdentifierEncoder().encode(new EntityIdentifier("type", "id")));

        assertEquals("00000003", unsignedIntegerEncoder().encode(UnsignedInteger.fromIntBits(3)));
        assertEquals("ffffffff", unsignedIntegerEncoder().encode(UnsignedInteger.MAX_VALUE));

        assertEquals("0000000000000003", unsignedLongEncoder().encode(UnsignedLong.fromLongBits(3)));
        assertEquals("ffffffffffffffff", unsignedLongEncoder().encode(UnsignedLong.MAX_VALUE));
    }

    @Test
    public void testCorrectReverseEncoding() {

        assertEquals("0", booleanRevEncoder().encode(true));
        assertEquals("1", booleanRevEncoder().encode(false));

        assertEquals("fc", byteRevEncoder().encode((byte) 3));

        assertEquals("7ffffffffffffff5", dateRevEncoder().encode(new Date(10)));

        assertEquals("800000000000000a44653600", instantRevEncoder().encode(Instant.ofEpochSecond(-10, 0).minusNanos(1)));
        assertEquals("80000000000000097fffffff", instantRevEncoder().encode(Instant.ofEpochSecond(-10, 0)));
        assertEquals("800000000000000044fdcc80", instantRevEncoder().encode(Instant.ofEpochMilli(-10).minusNanos(1)));
        assertEquals("800000000000000044fdcc7f", instantRevEncoder().encode(Instant.ofEpochMilli(-10)));
        assertEquals("7fffffffffffffff7f67697f", instantRevEncoder().encode(Instant.ofEpochMilli(10)));
        assertEquals("7fffffffffffffff7f67697e", instantRevEncoder().encode(Instant.ofEpochMilli(10).plusNanos(1)));
        assertEquals("7ffffffffffffff57fffffff", instantRevEncoder().encode(Instant.ofEpochSecond(10, 0)));
        assertEquals("7ffffffffffffff57ffffffe", instantRevEncoder().encode(Instant.ofEpochSecond(10, 0).plusNanos(1)));

        assertEquals("4007ffffffffffff", doubleRevEncoder().encode(1.5D));
        assertEquals("bff8000000000000", doubleRevEncoder().encode(-1.5D));

        assertEquals("403fffff", floatRevEncoder().encode(1.5F));
        assertEquals("bfc00000", floatRevEncoder().encode(-1.5F));

        assertEquals("7ffffffc", integerRevEncoder().encode(3));
        assertEquals("80000002", integerRevEncoder().encode(-3));

        assertEquals("7ffffffffffffffc", longRevEncoder().encode(3L));

        assertEquals("7ffffff0c000000000000000fffffffffffffffe", bigIntegerRevEncoder().encode(BigInteger.valueOf(Long.MAX_VALUE).pow(2)));
        assertEquals("800000103fffffffffffffff0000000000000000", bigIntegerRevEncoder().encode(BigInteger.valueOf(Long.MAX_VALUE).pow(2).negate()));

        //0 is a special case that requires different handling to preserve the scale.
        assertEquals("10000000000000000", bigDecimalRevEncoder().encode(new BigDecimal("0.0000000")));
        assertEquals("07ffffd98676829939286890001679560403353351", bigDecimalRevEncoder().encode(BigDecimal.valueOf(Double.MAX_VALUE).pow(2)));
        assertEquals("180000268323170060713109998320439596646649", bigDecimalRevEncoder().encode(BigDecimal.valueOf(Double.MAX_VALUE).pow(2).negate()));
        assertEquals("0800000009000", bigDecimalRevEncoder().encode(new BigDecimal("1.000")));
        assertEquals("0800000009", bigDecimalRevEncoder().encode(new BigDecimal("1")));

        assertEquals("3f57fefe", ipv4RevEncoder().encode(IPv4.fromString("192.168.1.1")));
        assertEquals("00000000", ipv4RevEncoder().encode(IPv4.fromString("255.255.255.255")));

        assertEquals("ffffffffffffffffffffffff3f57fefe", ipv6RevEncoder().encode(IPv6.fromString("::192.168.1.1")));
        assertEquals("ffffffffffffffffffff00003f57fefe", ipv6RevEncoder().encode(IPv6.fromString("::ffff:192.168.1.1")));
        assertEquals("00000000000000000000000000000000", ipv6RevEncoder().encode(IPv6.fromString("ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff")));

        assertEquals("3f57fefe", inet4AddressRevEncoder().encode(forIPv4String("192.168.1.1")));
        assertEquals("00000000", inet4AddressRevEncoder().encode(forIPv4String("255.255.255.255")));

        assertEquals("ffffffffffffffffffffffff3f57fefe", inet6AddressRevEncoder().encode(forIPv6String("::192.168.1.1")));
        assertEquals("ffffffffffffffffffff00003f57fefe", inet6AddressRevEncoder().encode(forIPv6String("::ffff:192.168.1.1")));
        assertEquals("00000000000000000000000000000000", inet6AddressRevEncoder().encode(forIPv6String("ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff")));

        assertEquals("fffffffc", unsignedIntegerRevEncoder().encode(UnsignedInteger.fromIntBits(3)));
        assertEquals("00000000", unsignedIntegerRevEncoder().encode(UnsignedInteger.MAX_VALUE));

        assertEquals("fffffffffffffffc", unsignedLongRevEncoder().encode(UnsignedLong.fromLongBits(3)));
        assertEquals("0000000000000000", unsignedLongRevEncoder().encode(UnsignedLong.MAX_VALUE));

    }

    @Test
    public void testSerialization() throws Exception {
        deserialize(serialize(LEXI_TYPES));
        deserialize(serialize(LEXI_REV_TYPES));
    }
}
