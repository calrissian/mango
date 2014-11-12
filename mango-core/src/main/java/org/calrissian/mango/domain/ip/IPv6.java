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


import java.net.Inet6Address;

import static com.google.common.primitives.UnsignedBytes.lexicographicalComparator;
import static org.calrissian.mango.net.MoreInetAddresses.forIPv6String;

/**
 * A Domain object that represents an IPv6 network address.  This is functionally a wrapper for {@link Inet6Address}
 * that is comparable to allow its use in other data objects such as {@link java.util.TreeMap} or
 * {@link com.google.common.collect.Range}
 */
public class IPv6 extends IP<Inet6Address> implements Comparable<IPv6>{

    /**
     * Generates a new IPv6 instance from the provided address. This will NOT do a dns lookup on the address if it
     * does not represent a valid ip address like {@code InetAddress.getByName()}.
     *
     * If the provided string is an ipv4 "mapped" address ('ffff:x.x.x.x' this method will still generate a valid IPv6
     * instance.
     */
    public static IPv6 fromString(String addr) {
        return new IPv6(forIPv6String(addr));
    }

    public IPv6(Inet6Address address) {
        super(address);
    }

    @Override
    public int compareTo(IPv6 o) {
        if (o == null)
            return 1;

        return lexicographicalComparator().compare(toByteArray(), o.toByteArray());
    }
}
