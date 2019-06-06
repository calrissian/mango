/*
 * Copyright (C) 2019 The Calrissian Authors
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
import org.calrissian.mango.types.encoders.simple.*;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.URI;
import java.time.Instant;
import java.util.Date;

/**
 * Provides a default {@link TypeRegistry} and utility methods for constructing {@link TypeEncoder}s which produce
 * readable strings.
 */
public class SimpleTypeEncoders {
    private SimpleTypeEncoders() {/* private constructor */}

    private static final TypeEncoder<Boolean, String> BOOLEAN_ENCODER = new BooleanEncoder();
    private static final TypeEncoder<Byte, String> BYTE_ENCODER = new ByteEncoder();
    private static final TypeEncoder<Date, String> DATE_ENCODER = new DateEncoder();
    private static final TypeEncoder<Instant, String> INSTANT_ENCODER = new InstantEncoder();
    private static final TypeEncoder<Double, String> DOUBLE_ENCODER = new DoubleEncoder();
    private static final TypeEncoder<Float, String> FLOAT_ENCODER = new FloatEncoder();
    private static final TypeEncoder<Integer, String> INTEGER_ENCODER = new IntegerEncoder();
    private static final TypeEncoder<Long, String> LONG_ENCODER = new LongEncoder();
    private static final TypeEncoder<String, String> STRING_ENCODER = new StringEncoder();
    private static final TypeEncoder<URI, String> URI_ENCODER = new UriEncoder();
    private static final TypeEncoder<BigInteger, String> BIGINT_ENCODER = new BigIntegerEncoder();
    private static final TypeEncoder<BigDecimal, String> BIGDEC_ENCODER = new BigDecimalEncoder();
    private static final TypeEncoder<Inet4Address, String> INET4_ENCODER = new Inet4AddressEncoder();
    private static final TypeEncoder<Inet6Address, String> INET6_ENCODER = new Inet6AddressEncoder();
    private static final TypeEncoder<IPv4, String> IPV4_ENCODER = new IPv4Encoder();
    private static final TypeEncoder<IPv6, String> IPV6_ENCODER = new IPv6Encoder();
    private static final TypeEncoder<EntityIdentifier, String> ENTITY_ID_ENCODER = new EntityIdentifierEncoder();
    private static final TypeEncoder<EventIdentifier, String> EVENT_ID_ENCODER = new EventIdentifierEncoder();
    private static final TypeEncoder<UnsignedInteger, String> UINT_ENCODER = new UnsignedIntegerEncoder();
    private static final TypeEncoder<UnsignedLong, String> ULONG_ENCODER = new UnsignedLongEncoder();

    /**
     * Simple Java types only
     */
    public static final TypeRegistry<String> SIMPLE_JAVA_TYPES = new TypeRegistry<>(
            booleanEncoder(), byteEncoder(), dateEncoder(), instantEncoder(), doubleEncoder(), floatEncoder(),
            integerEncoder(), longEncoder(), stringEncoder(), uriEncoder(), bigIntegerEncoder(),
            bigDecimalEncoder(), inet4AddressEncoder(), inet6AddressEncoder()
    );

    /**
     * Contains the full set of supported type encoders
     */
    public static final TypeRegistry<String> SIMPLE_TYPES = new TypeRegistry<>(SIMPLE_JAVA_TYPES,
            ipv4Encoder(), ipv6Encoder(), entityIdentifierEncoder(),
            unsignedIntegerEncoder(), unsignedLongEncoder()
    );

    public static TypeEncoder<Boolean, String> booleanEncoder() {
        return BOOLEAN_ENCODER;
    }

    public static TypeEncoder<Byte, String> byteEncoder() {
        return BYTE_ENCODER;
    }

    public static TypeEncoder<Date, String> dateEncoder() {
        return DATE_ENCODER;
    }

    public static TypeEncoder<Instant, String> instantEncoder() {
        return INSTANT_ENCODER;
    }

    public static TypeEncoder<Double, String> doubleEncoder() {
        return DOUBLE_ENCODER;
    }

    public static TypeEncoder<Float, String> floatEncoder() {
        return FLOAT_ENCODER;
    }

    public static TypeEncoder<Integer, String> integerEncoder() {
        return INTEGER_ENCODER;
    }

    public static TypeEncoder<Long, String> longEncoder() {
        return LONG_ENCODER;
    }

    public static TypeEncoder<String, String> stringEncoder() {
        return STRING_ENCODER;
    }

    public static TypeEncoder<URI, String> uriEncoder() {
        return URI_ENCODER;
    }

    public static TypeEncoder<BigInteger, String> bigIntegerEncoder() {
        return BIGINT_ENCODER;
    }

    public static TypeEncoder<BigDecimal, String> bigDecimalEncoder() {
        return BIGDEC_ENCODER;
    }

    public static TypeEncoder<Inet4Address, String> inet4AddressEncoder() {
        return INET4_ENCODER;
    }

    public static TypeEncoder<Inet6Address, String> inet6AddressEncoder() {
        return INET6_ENCODER;
    }

    public static TypeEncoder<IPv4, String> ipv4Encoder() {
        return IPV4_ENCODER;
    }

    public static TypeEncoder<IPv6, String> ipv6Encoder() {
        return IPV6_ENCODER;
    }


    public static TypeEncoder<EntityIdentifier, String> entityIdentifierEncoder() {
        return ENTITY_ID_ENCODER;
    }

    public static TypeEncoder<EventIdentifier, String> eventIdentifierEncoder() {
        return EVENT_ID_ENCODER;
    }

    public static TypeEncoder<UnsignedInteger, String> unsignedIntegerEncoder() {
        return UINT_ENCODER;
    }

    public static TypeEncoder<UnsignedLong, String> unsignedLongEncoder() {
        return ULONG_ENCODER;
    }
}
