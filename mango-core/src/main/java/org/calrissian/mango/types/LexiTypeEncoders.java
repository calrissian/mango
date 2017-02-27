/*
 * Copyright (C) 2017 The Calrissian Authors
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
package org.calrissian.mango.types;


import com.google.common.primitives.UnsignedInteger;
import com.google.common.primitives.UnsignedLong;
import org.calrissian.mango.domain.entity.EntityIdentifier;
import org.calrissian.mango.domain.event.EventIdentifier;
import org.calrissian.mango.net.IPv4;
import org.calrissian.mango.net.IPv6;
import org.calrissian.mango.types.encoders.lexi.*;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.URI;
import java.util.Date;

/**
 * Provides a default {@link TypeRegistry} and utility methods for constructing {@link TypeEncoder}s which produce
 * lexicographically sortable strings. Additionally, utilities are provided for reverse encodings which will produce
 * strings which will produce a reverse lexicographical sort ordering.
 */
public class LexiTypeEncoders {

    private LexiTypeEncoders() {/*private constructor*/}

    /**
     * Simple Java types only
     */
    public static final TypeRegistry<String> LEXI_JAVA_TYPES = new TypeRegistry<>(
            booleanEncoder(), byteEncoder(), dateEncoder(), doubleEncoder(), floatEncoder(),
            integerEncoder(), longEncoder(), stringEncoder(), uriEncoder(), bigIntegerEncoder(),
            bigDecimalEncoder(), inet4AddressEncoder(), inet6AddressEncoder()
    );

    public static final TypeRegistry<String> LEXI_REV_JAVA_TYPES = new TypeRegistry<>(
            booleanRevEncoder(), byteRevEncoder(), dateRevEncoder(), doubleRevEncoder(), floatRevEncoder(),
            integerRevEncoder(), longRevEncoder(), stringRevEncoder(), uriRevEncoder(), bigIntegerRevEncoder(),
            bigDecimalRevEncoder(), inet4AddressRevEncoder(), inet6AddressRevEncoder()
    );

    /**
     * Contains the full set of supported type encoders
     */
    public static final TypeRegistry<String> LEXI_TYPES = new TypeRegistry<>(LEXI_JAVA_TYPES,
            ipv4Encoder(), ipv6Encoder(), entityIdentifierEncoder(), eventIdentifierEncoder(),
            unsignedIntegerEncoder(), unsignedLongEncoder()
    );

    public static final TypeRegistry<String> LEXI_REV_TYPES = new TypeRegistry<>(LEXI_REV_JAVA_TYPES,
            ipv4RevEncoder(), ipv6RevEncoder(), entityIdentifierRevEncoder(), eventIdentifierRevEncoder(),
            unsignedIntegerRevEncoder(), unsignedLongRevEncoder()
    );

    private static <T> TypeEncoder<T, String> reverseEncoder(TypeEncoder<T, String> sourceEncoder) {
        return new ReverseEncoder<>(sourceEncoder);
    }

    public static TypeEncoder<Boolean, String> booleanEncoder() {
        return new BooleanEncoder();
    }

    public static TypeEncoder<Boolean, String> booleanRevEncoder() {
        return new BooleanReverseEncoder();
    }

    public static TypeEncoder<Byte, String> byteEncoder() {
        return new ByteEncoder();
    }

    public static TypeEncoder<Byte, String> byteRevEncoder() {
        return new ByteReverseEncoder();
    }

    public static TypeEncoder<Date, String> dateEncoder() {
        return new DateEncoder();
    }

    public static TypeEncoder<Date, String> dateRevEncoder() {
        return new DateReverseEncoder();
    }

    public static TypeEncoder<Double, String> doubleEncoder() {
        return new DoubleEncoder();
    }

    public static TypeEncoder<Double, String> doubleRevEncoder() {
        return new DoubleReverseEncoder();
    }

    public static TypeEncoder<Float, String> floatEncoder() {
        return new FloatEncoder();
    }

    public static TypeEncoder<Float, String> floatRevEncoder() {
        return new FloatReverseEncoder();
    }

    public static TypeEncoder<Integer, String> integerEncoder() {
        return new IntegerEncoder();
    }

    public static TypeEncoder<Integer, String> integerRevEncoder() {
        return new IntegerReverseEncoder();
    }

    public static TypeEncoder<Long, String> longEncoder() {
        return new LongEncoder();
    }

    public static TypeEncoder<Long, String> longRevEncoder() {
        return new LongReverseEncoder();
    }

    public static TypeEncoder<String, String> stringEncoder() {
        return SimpleTypeEncoders.stringEncoder();
    }

    public static TypeEncoder<String, String> stringRevEncoder() {
        return reverseEncoder(stringEncoder());
    }

    public static TypeEncoder<URI, String> uriEncoder() {
        return SimpleTypeEncoders.uriEncoder();
    }

    public static TypeEncoder<URI, String> uriRevEncoder() {
        return reverseEncoder(uriEncoder());
    }

    public static TypeEncoder<BigInteger, String> bigIntegerEncoder() {
        return new BigIntegerEncoder();
    }

    public static TypeEncoder<BigInteger, String> bigIntegerRevEncoder() {
        return new BigIntegerReverseEncoder();
    }

    public static TypeEncoder<BigDecimal, String> bigDecimalEncoder() {
        return new BigDecimalEncoder();
    }

    public static TypeEncoder<BigDecimal, String> bigDecimalRevEncoder() {
        return new BigDecimalReverseEncoder();
    }

    public static TypeEncoder<Inet4Address, String> inet4AddressEncoder() {
        return new Inet4AddressEncoder();
    }

    public static TypeEncoder<Inet4Address, String> inet4AddressRevEncoder() {
        return new Inet4AddressReverseEncoder();
    }

    public static TypeEncoder<Inet6Address, String> inet6AddressEncoder() {
        return new Inet6AddressEncoder();
    }

    public static TypeEncoder<Inet6Address, String> inet6AddressRevEncoder() {
        return new Inet6AddressReverseEncoder();
    }

    public static TypeEncoder<IPv4, String> ipv4Encoder() {
        return new IPv4Encoder();
    }

    public static TypeEncoder<IPv4, String> ipv4RevEncoder() {
        return new IPv4ReverseEncoder();
    }

    public static TypeEncoder<IPv6, String> ipv6Encoder() {
        return new IPv6Encoder();
    }

    public static TypeEncoder<IPv6, String> ipv6RevEncoder() {
        return new IPv6ReverseEncoder();
    }

    public static TypeEncoder<EntityIdentifier, String> entityIdentifierEncoder() {
        return SimpleTypeEncoders.entityIdentifierEncoder();
    }

    public static TypeEncoder<EntityIdentifier, String> entityIdentifierRevEncoder() {
        return reverseEncoder(SimpleTypeEncoders.entityIdentifierEncoder());
    }

    public static TypeEncoder<EventIdentifier, String> eventIdentifierEncoder() {
        return new EventIdentifierEncoder();
    }

    public static TypeEncoder<EventIdentifier, String> eventIdentifierRevEncoder() {
        return reverseEncoder(eventIdentifierEncoder());
    }

    public static TypeEncoder<UnsignedInteger, String> unsignedIntegerEncoder() {
        return new UnsignedIntegerEncoder();
    }

    public static TypeEncoder<UnsignedInteger, String> unsignedIntegerRevEncoder() {
        return new UnsignedIntegerReverseEncoder();
    }

    public static TypeEncoder<UnsignedLong, String> unsignedLongEncoder() {
        return new UnsignedLongEncoder();
    }

    public static TypeEncoder<UnsignedLong, String> unsignedLongRevEncoder() {
        return new UnsignedLongReverseEncoder();
    }
}
