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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.Integer.parseInt;
import static java.util.regex.Pattern.compile;

public class IPv4 implements Comparable<IPv4>, Serializable {

    private final long value;

    private static final Pattern IP_PATTERN = compile("\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}");

    public IPv4(String ip) {
        Matcher m = IP_PATTERN.matcher(ip);
        if(!m.matches()) {
            throw new IllegalArgumentException("For input string: " + ip);
        }

        value = ipToLong(ip);
    }

    public IPv4(Long ip) {
        this.value = ip;
    }

    public long getValue() {
        return value;
    }

    private static long ipToLong(String addr) {
        long num = 0;
        for (String octStr : addr.split("\\."))
            num = num << 8 | (parseInt(octStr) & 0xFF);

        return num;
    }

    @Override
    public int compareTo(IPv4 o) {
        if (o == null)
            return 1;
        return (value<o.value ? -1 : (value==o.value ? 0 : 1));
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
