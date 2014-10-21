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
package org.calrissian.mango.types;


import org.calrissian.mango.domain.entity.EntityRelationship;
import org.calrissian.mango.domain.ip.IPv4;
import org.calrissian.mango.types.encoders.simple.*;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.URI;
import java.util.Date;

public class SimpleTypeEncoders {

    private SimpleTypeEncoders() {/* private constructor */}

    @SuppressWarnings("unchecked")
    public static final TypeRegistry<String> SIMPLE_TYPES = new TypeRegistry<String>(
            booleanEncoder(), byteEncoder(), dateEncoder(), doubleEncoder(), floatEncoder(),
            integerEncoder(), ipv4Encoder(), longEncoder(), stringEncoder(), uriEncoder(),
            bigIntegerEncoder(), bigDecimalEncoder(), inet4AddressEncoder(), inet6AddressEncoder(),
            entityRelationshipEncoder()
    );

    public static TypeEncoder<Boolean, String> booleanEncoder() {
        return new BooleanEncoder();
    }

    public static TypeEncoder<Byte, String> byteEncoder() {
        return new ByteEncoder();
    }

    public static TypeEncoder<Date, String> dateEncoder() {
        return new DateEncoder();
    }

    public static TypeEncoder<Double, String> doubleEncoder() {
        return new DoubleEncoder();
    }

    public static TypeEncoder<Float, String> floatEncoder() {
        return new FloatEncoder();
    }

    public static TypeEncoder<Integer, String> integerEncoder() {
        return new IntegerEncoder();
    }

    public static TypeEncoder<IPv4, String> ipv4Encoder() {
        return new IPv4Encoder();
    }

    public static TypeEncoder<Long, String> longEncoder() {
        return new LongEncoder();
    }

    public static TypeEncoder<String, String> stringEncoder() {
        return new StringEncoder();
    }

    public static TypeEncoder<URI, String> uriEncoder() {
        return new UriEncoder();
    }

    public static TypeEncoder<BigInteger, String> bigIntegerEncoder() {
        return new BigIntegerEncoder();
    }

    public static TypeEncoder<BigDecimal, String> bigDecimalEncoder() {
        return new BigDecimalEncoder();
    }

    public static TypeEncoder<Inet4Address, String> inet4AddressEncoder() {
        return new Inet4AddressEncoder();
    }

    public static TypeEncoder<Inet6Address, String> inet6AddressEncoder() {
        return new Inet6AddressEncoder();
    }

    public static TypeEncoder<EntityRelationship, String> entityRelationshipEncoder() {
        return new EntityRelationshipEncoder();
    }
}
