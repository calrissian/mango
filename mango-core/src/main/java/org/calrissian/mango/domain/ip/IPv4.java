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


import com.google.common.collect.DiscreteDomain;
import com.google.common.collect.Range;

import java.net.Inet4Address;

import static com.google.common.collect.Range.closed;
import static com.google.common.net.InetAddresses.fromInteger;
import static com.google.common.primitives.Ints.fromByteArray;
import static com.google.common.primitives.UnsignedBytes.lexicographicalComparator;
import static java.lang.String.format;
import static org.calrissian.mango.net.MoreInetAddresses.*;

/**
 * A Domain object that represents an IPv4 network address.  This is functionally a wrapper for {@link Inet4Address}
 * that is comparable to allow its use in other data objects such as {@link java.util.TreeMap} or
 * {@link com.google.common.collect.Range}
 */
public class IPv4 extends IP<Inet4Address> implements Comparable<IPv4> {

    public IPv4(Inet4Address address) {
        super(address);
    }

    /**
     * @deprecated use {@code IPv4.fromString()}
     */
    @Deprecated
    public IPv4(String ip) {
        super(forIPv4String(ip));
    }

    /**
     * @deprecated
     */
    @Deprecated
    public IPv4(long ip) {
        super(fromInteger((int) ip));
    }

    /**
     * @deprecated
     */
    @Deprecated
    public long getValue() {
        return fromByteArray(toByteArray()) & 0xFFFFFFFFL;
    }

    @Override
    public int compareTo(IPv4 o) {
        if (o == null)
            return 1;

        return lexicographicalComparator().compare(toByteArray(), o.toByteArray());
    }

    /**
     * Generates a new IPv4 instance from the provided address. This will NOT do a dns lookup on the address if it
     * does not represent a valid ip address like {@code InetAddress.getByName()}.
     */
    public static IPv4 fromString(String addr) {
        return new IPv4(forIPv4String(addr));
    }

    /**
     * Parses the provided CIDR string and produces a closed {@link Range} encapsulating all IPv4 addresses between
     * the network and broadcast addresses in the subnet represented by the CIDR.
     */
    public static Range<IPv4> cidrRange(String cidr) {
        try {
            CidrInfo cidrInfo = parseCIDR(cidr);
            if (cidrInfo.getNetwork() instanceof Inet4Address &&
                    cidrInfo.getBroadcast() instanceof Inet4Address) {

                return closed(new IPv4((Inet4Address) cidrInfo.getNetwork()),
                        new IPv4((Inet4Address) cidrInfo.getBroadcast()));
            }

        } catch (Exception ignored) {}

        throw new IllegalArgumentException(format("Invalid IPv4 cidr representation %s", cidr));
    }

    private static final DiscreteDomain<IPv4> DISCRETE_DOMAIN = new DiscreteDomain<IPv4>() {
        private final IPv4 MIN_VALUE = fromString("0.0.0.0");
        private final IPv4 MAX_VALUE = fromString("255.255.255.255");

        @Override
        public IPv4 next(IPv4 iPv4) {
            return new IPv4(increment(iPv4.getAddress()));
        }

        @Override
        public IPv4 previous(IPv4 iPv4) {
            return new IPv4(decrement(iPv4.getAddress()));
        }

        @Override
        public long distance(IPv4 start, IPv4 end) {
            return longs().distance(
                    fromByteArray(start.toByteArray()) & 0xFFFFFFFFL,
                    fromByteArray(end.toByteArray()) & 0xFFFFFFFFL
            );
        }

        @Override
        public IPv4 minValue() {
            return MIN_VALUE;
        }

        @Override
        public IPv4 maxValue() {
            return MAX_VALUE;
        }
    };

    public static DiscreteDomain<IPv4> discreteDomain() {
        return DISCRETE_DOMAIN;
    }
}
