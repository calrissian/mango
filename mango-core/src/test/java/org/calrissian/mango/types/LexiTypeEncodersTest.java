/*
 * Copyright (C) 2014 The Calrissian Authors
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

import com.google.common.net.InetAddresses;
import org.calrissian.mango.domain.entity.EntityRelationship;
import org.calrissian.mango.domain.ip.IPv4;
import org.calrissian.mango.domain.ip.IPv6;
import org.junit.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.URI;
import java.util.Date;

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
        verifyBasicFunctionality(INET4_ALIAS, (Inet4Address) InetAddresses.forString("192.168.1.1"), inet4AddressEncoder());
        verifyBasicFunctionality(INET6_ALIAS, (Inet6Address) InetAddresses.forString("::192.168.1.1"), inet6AddressEncoder());
        verifyBasicFunctionality(ENTITY_RELATIONSHIP_ALIAS, new EntityRelationship("type", "id"), entityRelationshipEncoder());

        verifyBasicFunctionality(BOOLEAN_ALIAS, true, booleanRevEncoder());
        verifyBasicFunctionality(BYTE_ALIAS, (byte) 3, byteRevEncoder());
        verifyBasicFunctionality(DATE_ALIAS, new Date(), dateRevEncoder());
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
        verifyBasicFunctionality(INET4_ALIAS, (Inet4Address) InetAddresses.forString("192.168.1.1"), inet4AddressRevEncoder());
        verifyBasicFunctionality(INET6_ALIAS, (Inet6Address) InetAddresses.forString("::192.168.1.1"), inet6AddressRevEncoder());
        verifyBasicFunctionality(ENTITY_RELATIONSHIP_ALIAS, new EntityRelationship("type", "id"), entityRelationshipRevEncoder());
    }

    @Test
    public void testCorrectEncoding() throws Exception {

        assertEquals("1", booleanEncoder().encode(true));
        assertEquals("0", booleanEncoder().encode(false));

        assertEquals("03", byteEncoder().encode((byte) 3));

        assertEquals("800000000000000a", dateEncoder().encode(new Date(10)));

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
        assertEquals("18000000000000000", bigDecimalEncoder().encode(new BigDecimal("0.0000000")));
        assertEquals("180000268323170060713109998320439596646649", bigDecimalEncoder().encode(BigDecimal.valueOf(Double.MAX_VALUE).pow(2)));
        assertEquals("07ffffd98676829939286890001679560403353351", bigDecimalEncoder().encode(BigDecimal.valueOf(Double.MAX_VALUE).pow(2).negate()));
        assertEquals("1800000001000", bigDecimalEncoder().encode(new BigDecimal("1.000")));
        assertEquals("1800000001", bigDecimalEncoder().encode(new BigDecimal("1")));

        assertEquals("c0a80101", ipv4Encoder().encode(IPv4.fromString("192.168.1.1")));
        assertEquals("ffffffff", ipv4Encoder().encode(IPv4.fromString("255.255.255.255")));

        assertEquals("000000000000000000000000c0a80101", ipv6Encoder().encode(IPv6.fromString("::192.168.1.1")));
        assertEquals("ffffffffffffffffffffffffffffffff", ipv6Encoder().encode(IPv6.fromString("ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff")));

        assertEquals("c0a80101", inet4AddressEncoder().encode((Inet4Address) InetAddresses.forString("192.168.1.1")));
        assertEquals("ffffffff", inet4AddressEncoder().encode((Inet4Address) InetAddresses.forString("255.255.255.255")));

        assertEquals("000000000000000000000000c0a80101", inet6AddressEncoder().encode((Inet6Address) InetAddresses.forString("::192.168.1.1")));
        assertEquals("ffffffffffffffffffffffffffffffff", inet6AddressEncoder().encode((Inet6Address) InetAddresses.forString("ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff")));

        assertEquals("entity://type#id", entityRelationshipEncoder().encode(new EntityRelationship("type", "id")));
    }

    @Test
    public void testCorrectReverseEncoding() throws Exception {

        assertEquals("0", booleanRevEncoder().encode(true));
        assertEquals("1", booleanRevEncoder().encode(false));

        assertEquals("fc", byteRevEncoder().encode((byte) 3));

        assertEquals("7ffffffffffffff5", dateRevEncoder().encode(new Date(10)));

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
        assertEquals("18000000000000000", bigDecimalRevEncoder().encode(new BigDecimal("0.0000000")));
        assertEquals("07ffffd98676829939286890001679560403353351", bigDecimalRevEncoder().encode(BigDecimal.valueOf(Double.MAX_VALUE).pow(2)));
        assertEquals("180000268323170060713109998320439596646649", bigDecimalRevEncoder().encode(BigDecimal.valueOf(Double.MAX_VALUE).pow(2).negate()));
        assertEquals("0800000009000", bigDecimalRevEncoder().encode(new BigDecimal("1.000")));
        assertEquals("0800000009", bigDecimalRevEncoder().encode(new BigDecimal("1")));

        assertEquals("3f57fefe", ipv4RevEncoder().encode(IPv4.fromString("192.168.1.1")));
        assertEquals("00000000", ipv4RevEncoder().encode(IPv4.fromString("255.255.255.255")));

        assertEquals("ffffffffffffffffffffffff3f57fefe", ipv6RevEncoder().encode(IPv6.fromString("::192.168.1.1")));
        assertEquals("00000000000000000000000000000000", ipv6RevEncoder().encode(IPv6.fromString("ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff")));

        assertEquals("3f57fefe", inet4AddressRevEncoder().encode((Inet4Address) InetAddresses.forString("192.168.1.1")));
        assertEquals("00000000", inet4AddressRevEncoder().encode((Inet4Address) InetAddresses.forString("255.255.255.255")));

        assertEquals("ffffffffffffffffffffffff3f57fefe", inet6AddressRevEncoder().encode((Inet6Address) InetAddresses.forString("::192.168.1.1")));
        assertEquals("00000000000000000000000000000000", inet6AddressRevEncoder().encode((Inet6Address) InetAddresses.forString("ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff")));

    }
}
