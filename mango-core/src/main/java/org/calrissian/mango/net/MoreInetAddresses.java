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

import com.google.common.base.Preconditions;
import com.google.common.net.InetAddresses;

import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.UnknownHostException;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.net.InetAddresses.toAddrString;
import static java.lang.String.format;
import static java.util.Arrays.copyOfRange;

/**
 * Static utility methods pertaining to {@link InetAddress} instances and not alread provided in {@link com.google.common.net.InetAddresses}.
 *
 * Unlike {@link com.google.common.net.InetAddresses} these methods, all provide full support for IPV6 "IPv4 mapped"
 * addresses {@code "::ffff:192.168.0.1"} inside an Inet6Address container.  This is a deviation from the
 * InetAddress.getAddress() functionality which will automatically create an {@link Inet4Address} for these addresses.  That
 * behavior, however, can be problematic during serialization and comparisons for InetAddresses.
 */
public class MoreInetAddresses {
    private MoreInetAddresses() {/*intentionally private*/}

    /**
     * Returns an {@link Inet4Address}, given a byte array representation of the IPv4 address.
     *
     * @param bytes byte array representing an IPv4 address (should be of length 4)
     * @return {@link Inet4Address} corresponding to the supplied byte array
     * @throws IllegalArgumentException if a valid {@link Inet4Address} can not be created
     */
    private static Inet4Address getInet4Address(byte[] bytes) {
        Preconditions.checkArgument(bytes.length == 4,
                "Byte array has invalid length for an IPv4 address: %s != 4.",
                bytes.length);

        try {
            // Given a 4-byte array, this cast should always succeed.
            return (Inet4Address) InetAddress.getByAddress(bytes);
        } catch (UnknownHostException e) {
            throw new AssertionError(e);
        }
    }

    /**
     * Returns an {@link Inet6Address}, given a byte array representation of the IPv6 address.
     *
     * @param bytes byte array representing an IPv4 address (should be of length 16)
     * @return {@link Inet6Address} corresponding to the supplied byte array
     * @throws IllegalArgumentException if a valid {@link Inet6Address} can not be created
     */
    private static Inet6Address getInet6Address(byte[] bytes) {
        Preconditions.checkArgument(bytes.length == 16,
                "Byte array has invalid length for an IPv4 address: %s != 16.",
                bytes.length);

        try {
            //This is a simple way to bypass the automatic conversion to Inet4Address as the default behavior
            //in InetAddress.getByAddress(byte[]) for mapped addresses.
            return Inet6Address.getByAddress(null, bytes, -1);
        } catch (UnknownHostException e) {
            throw new AssertionError(e);
        }
    }

    /**
     * Returns the {@link InetAddress} having the given string representation.  It additionally respects
     * IPV6 "IPv4 mapped" addresses, {@code "::ffff:192.168.0.1"} as an {@link java.net.Inet6Address}.
     *
     * <p>This deliberately avoids all nameservice lookups (e.g. no DNS).
     *
     * <p>NOTE: This simply extends the {@code InetAddresses.forString} functionality
     * to support "IPv4 mapped" addresses.
     *
     * @param ipString {@code String} containing an IPv4 or IPv6 string literal, e.g.
     *     {@code "192.168.0.1"} or {@code "2001:db8::1"}
     * @return {@link InetAddress} representing the argument
     * @throws IllegalArgumentException if the argument is not a valid IP string literal
     */
    public static InetAddress forString(String ipString) {
        checkNotNull(ipString);

        InetAddress address = InetAddresses.forString(ipString);

        //If the string was an ipv6 representation and the result was a valid Inet4Address then return it as the
        //mapped representation that it was originally.
        if (address instanceof Inet4Address && ipString.contains(":"))
            return getIPv4MappedIPv6Address((Inet4Address) address);

        return address;
    }

    /**
     * Generates a new Inet4Address instance from the provided address. This will NOT do a dns lookup on the address if it
     * does not represent a valid ip address like {@code InetAddress.getByName()}.
     */
    public static Inet4Address forIPv4String(String ipString) {
        checkNotNull(ipString);

        try{
            InetAddress parsed = forString(ipString);

            if (parsed instanceof Inet4Address)
                return (Inet4Address) parsed;

        }catch(Exception ignored){}

        throw new IllegalArgumentException(format("Invalid IPv4 representation: %s", ipString));
    }

    /**
     * Generates a new {@link Inet6Address} instance from the provided address. This will NOT do a dns lookup on the address if it
     * does not represent a valid ip address like {@code InetAddress.getByName()}.
     *
     * If the provided string is an ipv4 "mapped" address ('ffff:x.x.x.x' this method will still generate a valid {@link Inet6Address}
     * instance.
     */
    public static Inet6Address forIPv6String(String ipString) {
        checkNotNull(ipString);

        try{
            InetAddress parsed = forString(ipString);

            if (parsed instanceof Inet6Address)
                return (Inet6Address) parsed;

        }catch(Exception ignored){}

        throw new IllegalArgumentException(format("Invalid IPv6 representation: %s", ipString));
    }

    /**
     * Evaluates whether the argument is an IPv6 "mapped" address.
     *
     * <p>An "IPv4 mapped", or "mapped", address is one with 80 leading
     * bits of zero followed by 16 bits of 1, with the remaining 32 bits interpreted as an
     * IPv4 address.  These are conventionally represented in string
     * literals as {@code "::ffff:192.168.0.1"}, though {@code "::ffff:c0a8:1"} is
     * also considered an IPv4 compatible address (and equivalent to
     * {@code "::192.168.0.1"}).
     *
     * @param ip {@link Inet6Address} to be examined for embedded IPv4 mapped address format
     * @return {@code true} if the argument is a valid "mapped" address
     */
    public static boolean isMappedIPv4Address(Inet6Address ip) {
        byte bytes[] = ip.getAddress();
        return ((bytes[0] == 0x00) && (bytes[1] == 0x00) &&
                (bytes[2] == 0x00) && (bytes[3] == 0x00) &&
                (bytes[4] == 0x00) && (bytes[5] == 0x00) &&
                (bytes[6] == 0x00) && (bytes[7] == 0x00) &&
                (bytes[8] == 0x00) && (bytes[9] == 0x00) &&
                (bytes[10] == (byte)0xff) && (bytes[11] == (byte)0xff));
    }

    /**
     * Returns the IPv4 address embedded in an IPv4 mapped address.
     *
     * @param ip {@link Inet6Address} to be examined for an embedded IPv4 address
     * @return {@link Inet4Address} of the embedded IPv4 address
     * @throws IllegalArgumentException if the argument is not a valid IPv4 mapped address
     */
    public static Inet4Address getMappedIPv4Address(Inet6Address ip) {
        Preconditions.checkArgument(isMappedIPv4Address(ip),
                "Address '%s' is not IPv4-compatible.", toAddrString(ip));

        return getInet4Address(copyOfRange(ip.getAddress(), 12, 16));
    }

    /**
     * Examines the Inet6Address to determine if it is an IPv6 address of one
     * of the specified address types that contain an embedded IPv4 address.
     *
     * <p>NOTE: This simply extends the {@code InetAddresses.hasEmbeddedIPv4ClientAddress} functionality
     * to support "IPv4 mapped" addresses.
     *
     * @param ip {@link Inet6Address} to be examined for embedded IPv4 client address
     * @return {@code true} if there is an embedded IPv4 client address
     */
    public static boolean hasEmbeddedIPv4ClientAddress(Inet6Address ip) {
        return isMappedIPv4Address(ip) || InetAddresses.hasEmbeddedIPv4ClientAddress(ip);
    }

    /**
     * Examines the Inet6Address to extract the embedded IPv4 client address
     * if the InetAddress is an IPv6 address of one of the specified address
     * types that contain an embedded IPv4 address.
     *
     * <p>NOTE: This simply extends the {@code InetAddresses.getEmbeddedIPv4ClientAddress} functionality
     * to support "IPv4 mapped" addresses.
     *
     * @param ip {@link Inet6Address} to be examined for embedded IPv4 client address
     * @return {@link Inet4Address} of embedded IPv4 client address
     * @throws IllegalArgumentException if the argument does not have a valid embedded IPv4 address
     */
    public static Inet4Address getEmbeddedIPv4ClientAddress(Inet6Address ip) {
        if (isMappedIPv4Address(ip))
            return getMappedIPv4Address(ip);

        return InetAddresses.getEmbeddedIPv4ClientAddress(ip);
    }

    public static Inet6Address getIPv4MappedIPv6Address(Inet4Address ip) {
        byte[] from = ip.getAddress();
        byte[] bytes = new byte[] {
                0x0,0x0,0x0,0x0,
                0x0,0x0,0x0,0x0,
                0x0,0x0,(byte)0xff,(byte)0xff,
                from[0],from[1],from[2],from[3]
        };

        return getInet6Address(bytes);
    }

    public static Inet6Address getIPV4CompatIPv6Address(Inet4Address ip) {
        byte[] from = ip.getAddress();
        byte[] bytes = new byte[] {
                0x0,0x0,0x0,0x0,
                0x0,0x0,0x0,0x0,
                0x0,0x0,0x0,0x0,
                from[0],from[1],from[2],from[3]
        };

        return getInet6Address(bytes);
    }
}
