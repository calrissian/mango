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
package org.calrissian.mango.net;

import org.junit.Test;

import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.UnknownHostException;

import static com.google.common.net.InetAddresses.toAddrString;
import static org.calrissian.mango.net.MoreInetAddresses.forIPv4String;
import static org.calrissian.mango.net.MoreInetAddresses.forIPv6String;
import static org.junit.Assert.*;

public class MoreInetAddressesTest {


    @Test
    public void forStringTest() {
        InetAddress address;

        address = MoreInetAddresses.forString("1.2.3.4");
        assertTrue(address instanceof Inet4Address);
        assertEquals("1.2.3.4", toAddrString(address));

        address = MoreInetAddresses.forString("::1.2.3.4");
        assertTrue(address instanceof Inet6Address);
        assertEquals("::102:304", toAddrString(address));

        //Mapped ipv4 addresses should be ipv6
        address = MoreInetAddresses.forString("::ffff:1.2.3.4");
        assertTrue(address instanceof Inet6Address);
        assertEquals("::ffff:102:304", toAddrString(address));
    }

    @Test(expected = NullPointerException.class)
    public void forStringNullTest() {
        MoreInetAddresses.forString(null);
    }

    @Test
    public void forIpv4StringTest() {
        Inet4Address address;

        address = forIPv4String("1.2.3.4");
        assertEquals("1.2.3.4", toAddrString(address));
    }

    @Test(expected = NullPointerException.class)
    public void forIpv4StringNullTest() {
        forIPv4String(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void forIpv4StringWithIpv6Test() {
        forIPv4String("::1.2.3.4");
    }

    @Test(expected = IllegalArgumentException.class)
    public void forIpv4StringWithMappedIpv4Test() {
        forIPv4String("::ffff:1.2.3.4");
    }

    @Test
    public void forIpv6StringTest() {
        Inet6Address address;

        address = forIPv6String("::1.2.3.4");
        assertEquals("::102:304", toAddrString(address));

        //Mapped ipv4 addresses should be ipv6
        address = forIPv6String("::ffff:1.2.3.4");
        assertEquals("::ffff:102:304", toAddrString(address));
    }

    @Test(expected = NullPointerException.class)
    public void forIpv6StringNullTest() {
        forIPv6String(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void forIpv6StringWithIpv4Test() {
        forIPv6String("1.2.3.4");
    }

    @Test
    public void isMappedIPv4AddressTest() throws UnknownHostException {
        byte[] bytes = new byte[] {
                0x0,0x0,0x0,0x0,
                0x0,0x0,0x0,0x0,
                0x0,0x0,(byte)0xff,(byte)0xff,
                0x1,0x2,0x3,0x4
        };
        Inet6Address address = Inet6Address.getByAddress(null, bytes, -1);

        assertTrue(MoreInetAddresses.isMappedIPv4Address(address));

        //Test compat address instead of mapped address.
        bytes = new byte[] {
                0x0,0x0,0x0,0x0,
                0x0,0x0,0x0,0x0,
                0x0,0x0,0x0,0x0,
                0x1,0x2,0x3,0x4
        };
        address = Inet6Address.getByAddress(null, bytes, -1);

        assertFalse(MoreInetAddresses.isMappedIPv4Address(address));
    }

    @Test(expected = NullPointerException.class)
    public void isMappedIPv4AddressNullTest() {
        MoreInetAddresses.isMappedIPv4Address(null);
    }

    @Test
    public void getMappedIPv4AddressTest() throws UnknownHostException {
        byte[] bytes = new byte[] {
                0x0,0x0,0x0,0x0,
                0x0,0x0,0x0,0x0,
                0x0,0x0,(byte)0xff,(byte)0xff,
                0x1,0x2,0x3,0x4
        };
        Inet6Address address = Inet6Address.getByAddress(null, bytes, -1);

        Inet4Address inet4Address = MoreInetAddresses.getMappedIPv4Address(address);

        //assert against java inet address
        assertEquals(InetAddress.getByAddress(bytes), inet4Address);
    }

    @Test(expected = NullPointerException.class)
    public void getMappedIPv4AddressNullTest() {
        MoreInetAddresses.getMappedIPv4Address(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void getMappedIPv4AddressInvalidTypeTest() throws UnknownHostException {
        byte[] bytes = new byte[] {
                0x0,0x0,0x0,0x0,
                0x0,0x0,0x0,0x0,
                0x0,0x0,0x0,0x0,
                0x1,0x2,0x3,0x4
        };
        Inet6Address address = Inet6Address.getByAddress(null, bytes, -1);

        MoreInetAddresses.getMappedIPv4Address(address);
    }

    @Test
    public void hasEmbeddedIPv4ClientAddressTest() {
        //Compat
        assertTrue(MoreInetAddresses.hasEmbeddedIPv4ClientAddress(forIPv6String("::ffff:1.2.3.4")));

        //Mapped
        assertTrue(MoreInetAddresses.hasEmbeddedIPv4ClientAddress(forIPv6String("::1.2.3.4")));

        //6to4
        assertTrue(MoreInetAddresses.hasEmbeddedIPv4ClientAddress(forIPv6String("2002:102:304::")));

        //Non valid representation of ipv4 in ipv6
        assertFalse(MoreInetAddresses.hasEmbeddedIPv4ClientAddress(forIPv6String("1::1.2.3.4")));
    }

    @Test(expected = NullPointerException.class)
    public void hasEmbeddedIPv4ClientAddressNullTest() {
        MoreInetAddresses.hasEmbeddedIPv4ClientAddress(null);
    }

    @Test
    public void getEmbeddedIPv4ClientAddressTest() {
        //Compat
        assertEquals(MoreInetAddresses.forString("1.2.3.4"), MoreInetAddresses.getEmbeddedIPv4ClientAddress(forIPv6String("::ffff:1.2.3.4")));

        //Mapped
        assertEquals(MoreInetAddresses.forString("1.2.3.4"), MoreInetAddresses.getEmbeddedIPv4ClientAddress(forIPv6String("::1.2.3.4")));

        //6to4
        assertEquals(MoreInetAddresses.forString("1.2.3.4"), MoreInetAddresses.getEmbeddedIPv4ClientAddress(forIPv6String("2002:102:304::")));
    }

    @Test(expected = NullPointerException.class)
    public void getEmbeddedIPv4ClientAddressNullTest() {
        MoreInetAddresses.getEmbeddedIPv4ClientAddress(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void getEmbeddedIPv4ClientAddressNotValidTest() {
        //Non valid representation of ipv4 in ipv6
        MoreInetAddresses.getEmbeddedIPv4ClientAddress(forIPv6String("1::1.2.3.4"));
    }

    @Test
    public void getIPv4MappedIPv6AddressTest() {
        assertEquals("::ffff:102:304", toAddrString(MoreInetAddresses.getIPv4MappedIPv6Address(forIPv4String("1.2.3.4"))));
    }

    @Test(expected = NullPointerException.class)
    public void getIPv4MappedIPv6AddressNullTest() {
        MoreInetAddresses.getIPv4MappedIPv6Address(null);
    }

    @Test
    public void getIPv4CompatIPv6AddressTest() {
        assertEquals("::102:304", toAddrString(MoreInetAddresses.getIPV4CompatIPv6Address(forIPv4String("1.2.3.4"))));
    }

    @Test(expected = NullPointerException.class)
    public void getIPv4CompatIPv6AddressNullTest() {
        MoreInetAddresses.getIPV4CompatIPv6Address(null);
    }
}