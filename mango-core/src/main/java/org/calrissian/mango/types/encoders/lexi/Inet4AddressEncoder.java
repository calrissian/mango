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
package org.calrissian.mango.types.encoders.lexi;


import org.apache.commons.codec.DecoderException;
import org.calrissian.mango.types.encoders.AbstractInet4AddressEncoder;
import org.calrissian.mango.types.exception.TypeDecodingException;

import java.net.Inet4Address;
import java.net.UnknownHostException;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static java.net.InetAddress.getByAddress;
import static org.apache.commons.codec.binary.Hex.decodeHex;
import static org.apache.commons.codec.binary.Hex.encodeHexString;

public class Inet4AddressEncoder extends AbstractInet4AddressEncoder<String> {
    @Override
    public String encode(Inet4Address value) {
        checkNotNull(value, "Null values are not allowed");
        return encodeHexString(value.getAddress());
    }

    @Override
    public Inet4Address decode(String value) {
        checkNotNull(value, "Null values are not allowed");
        checkArgument(value.length() == 8, "The value is not a valid encoding");
        try {
            return (Inet4Address) getByAddress(decodeHex(value.toCharArray()));
        } catch (UnknownHostException e) {
            throw new TypeDecodingException(e);
        } catch (DecoderException e) {
            throw new TypeDecodingException(e);
        }
    }
}
