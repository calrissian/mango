/*
 * Copyright (C) 2013 The Calrissian Authors
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
package org.calrissian.mango.domain;


import java.io.Serializable;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.primitives.Longs.compare;
import static java.lang.Integer.parseInt;

public class IPv4 implements Comparable<IPv4>, Serializable {

    private final long value;

    private static long ipToLong(String addr) {
        checkNotNull(addr);

        String[] octets = addr.split("\\.", 5);
        checkArgument(octets.length == 4, "Invalid IPv4 representation: %s", addr);

        long num = 0;
        for (String octStr : octets) {
            try {
                int octet = parseInt(octStr);
                checkArgument(octet >= 0 && octet < 256, "Invalid IPv4 representation: %s", addr);

                num = num << 8 | (parseInt(octStr) & 0xFF);
            } catch (NumberFormatException nfe) {
                throw new IllegalArgumentException("Invalid IPv4 representation: " + addr);
            }

        }
        return num;
    }

    public IPv4(String ip) {
        this(ipToLong(ip));
    }

    public IPv4(long ip) {
        this.value = ip;
    }

    public long getValue() {
        return value;
    }

    @Override
    public int compareTo(IPv4 o) {
        if (o == null)
            return 1;
        return compare(value, o.value);
    }

    public String toString() {
        return ((value >>> 24) & 0xFF) + "." +
                ((value >>> 16) & 0xFF) + "." +
                ((value >>> 8) & 0xFF) + "." +
                (value & 0xFF);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof IPv4)) return false;

        IPv4 iPv4 = (IPv4) o;

        return value == iPv4.value;
    }

    @Override
    public int hashCode() {
        return (int) (value ^ (value >>> 32));
    }
}
