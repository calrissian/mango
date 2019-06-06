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
package org.calrissian.mango.net;


import com.google.common.collect.DiscreteDomain;
import com.google.common.collect.Range;
import org.calrissian.mango.net.IPv4;
import org.calrissian.mango.net.IPv6;
import org.junit.Test;

import static org.calrissian.mango.net.MoreInetAddresses.forIPv4String;
import static org.calrissian.mango.net.MoreInetAddresses.forIPv6String;
import static org.junit.Assert.*;

public class IPTest {

    /**
     * IPv4 tests
     */
    @Test
    public void simpleIpv4Test() {
        IPv4 ip = new IPv4(forIPv4String("1.2.3.4"));
        assertEquals(forIPv4String("1.2.3.4"), ip.getAddress());
        assertArrayEquals(forIPv4String("1.2.3.4").getAddress(), ip.toByteArray());
    }

    @Test
    public void ipv4FromString() {
        IPv4 ip = IPv4.fromString("1.2.3.4");
        assertNotNull(ip);
        assertEquals(new IPv4(forIPv4String("1.2.3.4")), ip);
    }

    @Test(expected = IllegalArgumentException.class)
    public void ipv4FromStringMalformedTest() {
        IPv4.fromString("test");
    }

    @Test(expected = IllegalArgumentException.class)
    public void ipv4FromStringIpv6Test() {
        IPv4.fromString("::ffff:ffff");
    }

    @Test
    public void ipv4ToStringTest() {
        IPv4 ip = new IPv4(forIPv4String("1.2.3.4"));
        assertEquals("1.2.3.4", ip.toString());
    }

    @Test
    public void ipv4CidrRangeTest() {
        Range<IPv4> cidr = IPv4.cidrRange("255.255.255.255/16");
        assertEquals(IPv4.fromString("255.255.0.0"), cidr.lowerEndpoint());
        assertEquals(IPv4.fromString("255.255.255.255"), cidr.upperEndpoint());

        cidr = IPv4.cidrRange("0.0.0.0/16");
        assertEquals(IPv4.fromString("0.0.0.0"), cidr.lowerEndpoint());
        assertEquals(IPv4.fromString("0.0.255.255"), cidr.upperEndpoint());

        cidr = IPv4.cidrRange("255.255.255.255/20");
        assertEquals(IPv4.fromString("255.255.240.0"), cidr.lowerEndpoint());
        assertEquals(IPv4.fromString("255.255.255.255"), cidr.upperEndpoint());

        cidr = IPv4.cidrRange("0.0.0.0/20");
        assertEquals(IPv4.fromString("0.0.0.0"), cidr.lowerEndpoint());
        assertEquals(IPv4.fromString("0.0.15.255"), cidr.upperEndpoint());

        cidr = IPv4.cidrRange("1.2.3.4/32");
        assertEquals(IPv4.fromString("1.2.3.4"), cidr.lowerEndpoint());
        assertEquals(IPv4.fromString("1.2.3.4"), cidr.upperEndpoint());

        cidr = IPv4.cidrRange("1.2.3.4/0");
        assertEquals(IPv4.fromString("0.0.0.0"), cidr.lowerEndpoint());
        assertEquals(IPv4.fromString("255.255.255.255"), cidr.upperEndpoint());
    }

    @Test(expected = IllegalArgumentException.class)
    public void ipv4CidrRangeIPv6Test() {
        IPv4.cidrRange("::ffff:1.2.3.4/64");
    }

    @Test(expected = IllegalArgumentException.class)
    public void ipv4CidrRangeInvalidSyntaxTest() {
        IPv4.cidrRange("1.2.3.4/16/16");
    }

    @Test(expected = IllegalArgumentException.class)
    public void ipv4CidrRangeInvalidSyntax2Test() {
        IPv4.cidrRange("1.2.3.4/33");
    }

    /**
     * IPv6 Tests
     */

    @Test
    public void simpleIpv6Test() {
        IPv6 ip = new IPv6(forIPv6String("::1.2.3.4"));
        assertEquals(forIPv6String("::1.2.3.4"), ip.getAddress());
        assertArrayEquals(forIPv6String("::1.2.3.4").getAddress(), ip.toByteArray());
    }

    @Test
    public void ipv6FromString() {
        IPv6 ip = IPv6.fromString("::1.2.3.4");
        assertNotNull(ip);
        assertEquals(new IPv6(forIPv6String("::1.2.3.4")), ip);
    }

    @Test(expected = IllegalArgumentException.class)
    public void ipv6FromStringMalformedTest() {
        IPv6.fromString("test");
    }

    @Test(expected = IllegalArgumentException.class)
    public void ipv6FromStringIpv4Test() {
        IPv6.fromString("1.2.3.4");
    }

    @Test
    public void ipv6ToStringTest() {
        IPv6 ip = new IPv6(forIPv6String("::1.2.3.4"));
        assertEquals("::102:304", ip.toString());

        ip = new IPv6(forIPv6String("ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff"));
        assertEquals("ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff", ip.toString());

        ip = new IPv6(forIPv6String("1234:0000:0000:0000:0000:0000:0000:1234"));
        assertEquals("1234::1234", ip.toString());
    }

    @Test
    public void ipv6CidrRangeTest() {
        Range<IPv6> cidr = IPv6.cidrRange("ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff/64");
        assertEquals(IPv6.fromString("ffff:ffff:ffff:ffff::"), cidr.lowerEndpoint());
        assertEquals(IPv6.fromString("ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff"), cidr.upperEndpoint());

        cidr = IPv6.cidrRange("ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff/68");
        assertEquals(IPv6.fromString("ffff:ffff:ffff:ffff:f000::"), cidr.lowerEndpoint());
        assertEquals(IPv6.fromString("ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff"), cidr.upperEndpoint());

        cidr = IPv6.cidrRange("::/64");
        assertEquals(IPv6.fromString("::"), cidr.lowerEndpoint());
        assertEquals(IPv6.fromString("::ffff:ffff:ffff:ffff"), cidr.upperEndpoint());

        cidr = IPv6.cidrRange("::/68");
        assertEquals(IPv6.fromString("::"), cidr.lowerEndpoint());
        assertEquals(IPv6.fromString("::fff:ffff:ffff:ffff"), cidr.upperEndpoint());

        cidr = IPv6.cidrRange("1:2:3:4:5:6:7:8/128");
        assertEquals(IPv6.fromString("1:2:3:4:5:6:7:8"), cidr.lowerEndpoint());
        assertEquals(IPv6.fromString("1:2:3:4:5:6:7:8"), cidr.upperEndpoint());

        cidr = IPv6.cidrRange("1:2:3:4:5:6:7:8/0");
        assertEquals(IPv6.fromString("::"), cidr.lowerEndpoint());
        assertEquals(IPv6.fromString("ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff"), cidr.upperEndpoint());

        cidr = IPv6.cidrRange("::ffff:1.2.3.4/112");
        assertEquals(IPv6.fromString("::ffff:102:0"), cidr.lowerEndpoint());
        assertEquals(IPv6.fromString("::ffff:102:ffff"), cidr.upperEndpoint());
    }

    @Test(expected = IllegalArgumentException.class)
    public void ipv6CidrRangeIPv6Test() {
        IPv6.cidrRange("1.2.3.4/16");
    }

    @Test(expected = IllegalArgumentException.class)
    public void ipv6CidrRangeInvalidSyntaxTest() {
        IPv6.cidrRange("1:2:3:4:5:6:7:8/16/16");
    }

    @Test(expected = IllegalArgumentException.class)
    public void ipv6CidrRangeInvalidSyntax2Test() {
        IPv6.cidrRange("1:2:3:4:5:6:7:8/129");
    }

    @Test
    public void ipv4DiscreteDomainTest() {
        DiscreteDomain<IPv4> discreteDomain = IPv4.discreteDomain();

        assertEquals(IPv4.fromString("1.1.1.2"), discreteDomain.next(IPv4.fromString("1.1.1.1")));
        assertEquals(IPv4.fromString("1.1.1.0"), discreteDomain.previous(IPv4.fromString("1.1.1.1")));
        assertEquals(IPv4.fromString("0.0.0.0"), discreteDomain.minValue());
        assertEquals(IPv4.fromString("255.255.255.255"), discreteDomain.maxValue());

        assertEquals(0xFFFFFFFFL, discreteDomain.distance(discreteDomain.minValue(), discreteDomain.maxValue()));
        assertEquals(1, discreteDomain.distance(IPv4.fromString("1.1.1.1"), IPv4.fromString("1.1.1.2")));
        assertEquals(-1, discreteDomain.distance(IPv4.fromString("1.1.1.2"), IPv4.fromString("1.1.1.1")));
    }

    @Test
    public void ipv6DiscreteDomainTest() {
        DiscreteDomain<IPv6> discreteDomain = IPv6.discreteDomain();

        assertEquals(IPv6.fromString("::1.1.1.2"), discreteDomain.next(IPv6.fromString("::1.1.1.1")));
        assertEquals(IPv6.fromString("::1.1.1.0"), discreteDomain.previous(IPv6.fromString("::1.1.1.1")));
        assertEquals(IPv6.fromString("::"), discreteDomain.minValue());
        assertEquals(IPv6.fromString("ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff"), discreteDomain.maxValue());

        assertEquals(Long.MAX_VALUE, discreteDomain.distance(discreteDomain.minValue(), discreteDomain.maxValue()));
        assertEquals(1, discreteDomain.distance(IPv6.fromString("::1.1.1.1"), IPv6.fromString("::1.1.1.2")));
        assertEquals(-1, discreteDomain.distance(IPv6.fromString("::1.1.1.2"), IPv6.fromString("::1.1.1.1")));
    }
}
