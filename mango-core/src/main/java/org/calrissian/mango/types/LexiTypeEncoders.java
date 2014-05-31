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
import org.calrissian.mango.types.encoders.lexi.*;

import java.net.URI;
import java.util.Date;

/**
 * Default type encoders for serializing data to strings that are lexicographically sortable.
 * <p/>
 * These methods implement the default implementations for the generic TypesNormalizer's normalize and denormalize
 * functions.
 */
public class LexiTypeEncoders {

    public static final TypeRegistry<String> LEXI_TYPES = new TypeRegistry<String>(
            booleanEncoder(), byteEncoder(), dateEncoder(), doubleEncoder(), floatEncoder(),
            integerEncoder(), ipv4Encoder(), longEncoder(), stringEncoder(), uriEncoder(),
            entityRelationshipEncoder()
    );

    public static final TypeRegistry<String> LEXI_REV_TYPES = new TypeRegistry<String>(
            booleanRevEncoder(), byteRevEncoder(), dateRevEncoder(), doubleRevEncoder(), floatRevEncoder(),
            integerRevEncoder(), ipv4RevEncoder(), longRevEncoder(), stringRevEncoder(), uriRevEncoder(),
            entityRelationshipRevEncoder()
    );

    public static <T> TypeEncoder<T, String> reverseEncoder(TypeEncoder<T, String> sourceEncoder) {
        return new ReverseEncoder<T>(sourceEncoder);
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

    public static TypeEncoder<IPv4, String> ipv4Encoder() {
        return new IPv4Encoder();
    }

    public static TypeEncoder<IPv4, String> ipv4RevEncoder() {
        return new IPv4ReverseEncoder();
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

    public static TypeEncoder<EntityRelationship, String> entityRelationshipEncoder() {
        return SimpleTypeEncoders.entityRelationshipEncoder();
    }

    public static TypeEncoder<EntityRelationship, String> entityRelationshipRevEncoder() {
        return reverseEncoder(SimpleTypeEncoders.entityRelationshipEncoder());
    }
}
