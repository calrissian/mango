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

import com.google.common.collect.DiscreteDomain;
import com.google.common.collect.Range;

import java.math.BigInteger;
import java.net.Inet6Address;

import static com.google.common.collect.Range.closed;
import static com.google.common.primitives.UnsignedBytes.lexicographicalComparator;
import static java.lang.String.format;
import static org.calrissian.mango.net.MoreInetAddresses.*;

/**
 * A Domain object that represents an IPv6 network address.  This is functionally a wrapper for {@link Inet6Address}
 * that is comparable to allow its use in other data objects such as {@link java.util.TreeMap} or
 * {@link com.google.common.collect.Range}
 */
public final class IPv6 extends IP<Inet6Address> implements Comparable<IPv6>{

    public IPv6(Inet6Address address) {
        super(address);
    }

    @Override
    public int compareTo(IPv6 o) {
        if (o == null)
            return 1;

        return lexicographicalComparator().compare(toByteArray(), o.toByteArray());
    }

    /**
     * Generates a new IPv6 instance from the provided address. This will NOT do a dns lookup on the address if it
     * does not represent a valid ip address like {@code InetAddress.getByName()}.
     *
     * If the provided string is an ipv6 "mapped" address ('ffff:x.x.x.x' this method will still generate a valid IPv6
     * instance.
     */
    public static IPv6 fromString(String addr) {
        return new IPv6(forIPv6String(addr));
    }

    /**
     * Parses the provided CIDR string and produces a closed {@link Range} encapsulating all IPv6 addresses between
     * the network and broadcast addresses in the subnet represented by the CIDR.
     */
    public static Range<IPv6> cidrRange(String cidr) {
        try {
            MoreInetAddresses.CidrInfo cidrInfo = parseCIDR(cidr);
            if (cidrInfo.getNetwork() instanceof Inet6Address &&
                    cidrInfo.getBroadcast() instanceof Inet6Address) {

                return closed(new IPv6((Inet6Address) cidrInfo.getNetwork()),
                        new IPv6((Inet6Address) cidrInfo.getBroadcast()));
            }

        } catch (Exception ignored) {}

        throw new IllegalArgumentException(format("Invalid IPv6 cidr representation %s", cidr));
    }

    private static final DiscreteDomain<IPv6> DISCRETE_DOMAIN = new DiscreteDomain<IPv6>() {
        private final IPv6 MIN_VALUE = fromString("::");
        private final IPv6 MAX_VALUE = fromString("ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff");

        @Override
        public IPv6 next(IPv6 iPv6) {
            return new IPv6(increment(iPv6.getAddress()));
        }

        @Override
        public IPv6 previous(IPv6 iPv6) {
            return new IPv6(decrement(iPv6.getAddress()));
        }

        @Override
        public long distance(IPv6 start, IPv6 end) {
            //Use the bigInteger distance for simplicity, but be careful to set the signum as positive (1) so that
            //BigInteger does not decode the bytes as a signed number.
            return bigIntegers().distance(
                    new BigInteger(1, start.toByteArray()),
                    new BigInteger(1, end.toByteArray())
            );
        }

        @Override
        public IPv6 minValue() {
            return MIN_VALUE;
        }

        @Override
        public IPv6 maxValue() {
            return MAX_VALUE;
        }
    };

    public static DiscreteDomain<IPv6> discreteDomain() {
        return DISCRETE_DOMAIN;
    }
}
