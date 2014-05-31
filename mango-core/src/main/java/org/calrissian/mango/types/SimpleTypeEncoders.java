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
import org.calrissian.mango.types.encoders.*;
import org.calrissian.mango.types.encoders.simple.*;
import org.calrissian.mango.types.exception.TypeDecodingException;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Date;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.lang.Boolean.parseBoolean;
import static java.lang.Byte.parseByte;
import static java.lang.Double.parseDouble;
import static java.lang.Float.parseFloat;
import static java.lang.Integer.parseInt;
import static java.lang.Long.parseLong;

public class SimpleTypeEncoders {

    @SuppressWarnings("unchecked")
    public static final TypeRegistry<String> SIMPLE_TYPES = new TypeRegistry<String>(
            booleanEncoder(), byteEncoder(), dateEncoder(), doubleEncoder(), floatEncoder(),
            integerEncoder(), ipv4Encoder(), longEncoder(), stringEncoder(), uriEncoder(),
            entityRelationshipEncoder()

    );

    public SimpleTypeEncoders() {/* private constructor */}

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

    public static TypeEncoder<EntityRelationship, String> entityRelationshipEncoder() {
        return new EntityRelationshipEncoder();
    }
}
