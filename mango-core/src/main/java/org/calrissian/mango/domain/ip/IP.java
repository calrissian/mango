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


import java.io.Serializable;
import java.net.InetAddress;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.net.InetAddresses.toAddrString;

/**
 * Intentionally package private.
 */
abstract class IP<T extends InetAddress> implements Serializable {
    private static final long serialVersionUID = 1L;

    private final T address;

    protected IP(T address) {
        checkNotNull(address);
        this.address = address;
    }

    public T getAddress() {
        return address;
    }

    public byte[] toByteArray() {
        return address.getAddress();
    }

    @Override
    public String toString() {
        return toAddrString(address);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof IP)) return false;

        IP ip = (IP) o;

        return address.equals(ip.address);
    }

    @Override
    public int hashCode() {
        return address.hashCode();
    }
}
