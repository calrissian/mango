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
package org.calrissian.mango.domain.ip;


import org.junit.Test;

import java.net.Inet4Address;
import java.net.Inet6Address;

import static com.google.common.net.InetAddresses.forString;
import static org.junit.Assert.*;

public class IPTest {

    /**
     * IPv4 tests
     */
    @Test
    public void simpleIpv4Test() {
        IPv4 ip = new IPv4((Inet4Address) forString("1.2.3.4"));
        assertEquals(forString("1.2.3.4"), ip.getAddress());
        assertArrayEquals(forString("1.2.3.4").getAddress(), ip.toByteArray());
    }

    @Test
    public void ipv4FromString() {
        IPv4 ip = IPv4.fromString("1.2.3.4");
        assertNotNull(ip);
        assertEquals(new IPv4((Inet4Address) forString("1.2.3.4")), ip);
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
        IPv4 ip = new IPv4((Inet4Address) forString("1.2.3.4"));
        assertEquals("1.2.3.4", ip.toString());
    }

    @Test
    public void ipv4LegacyConstructorTest() {
        IPv4 ip = new IPv4("1.2.3.4");
        assertEquals(forString("1.2.3.4"), ip.getAddress());
        assertArrayEquals(forString("1.2.3.4").getAddress(), ip.toByteArray());

        ip = new IPv4(16909060);
        assertEquals(forString("1.2.3.4"), ip.getAddress());
        assertArrayEquals(forString("1.2.3.4").getAddress(), ip.toByteArray());

        //Check integer overflow
        ip = new IPv4(0xFFFFFFFF);
        assertEquals(forString("255.255.255.255"), ip.getAddress());
        assertArrayEquals(forString("255.255.255.255").getAddress(), ip.toByteArray());
    }

    /**
     * IPv6 Tests
     */

    @Test
    public void simpleIpv6Test() {
        IPv6 ip = new IPv6((Inet6Address) forString("::1.2.3.4"));
        assertEquals(forString("::1.2.3.4"), ip.getAddress());
        assertArrayEquals(forString("::1.2.3.4").getAddress(), ip.toByteArray());
    }

    @Test
    public void ipv6FromString() {
        IPv6 ip = IPv6.fromString("::1.2.3.4");
        assertNotNull(ip);
        assertEquals(new IPv6((Inet6Address) forString("::1.2.3.4")), ip);
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
        IPv6 ip = new IPv6((Inet6Address) forString("::1.2.3.4"));
        assertEquals("::102:304", ip.toString());

        ip = new IPv6((Inet6Address) forString("ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff"));
        assertEquals("ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff", ip.toString());

        ip = new IPv6((Inet6Address) forString("1234:0000:0000:0000:0000:0000:0000:1234"));
        assertEquals("1234::1234", ip.toString());
    }

}
