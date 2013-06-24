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


import org.calrissian.mango.domain.IPv4;
import org.calrissian.mango.types.TypeEncoder;
import org.calrissian.mango.types.TypeRegistry;
import org.calrissian.mango.types.encoders.*;
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

public class GenericTypeEncoders {

    public static final TypeRegistry<String> DEFAULT_TYPES = new TypeRegistry<String>(
            booleanEncoder(), byteEncoder(), dateEncoder(), doubleEncoder(), floatEncoder(),
            integerEncoder(), ipv4Encoder(), longEncoder(), stringEncoder(), uriEncoder()
    );

    public static TypeEncoder<Boolean, String> booleanEncoder() {
        return new AbstractBooleanEncoder<String>() {
            @Override
            public String encode(Boolean value) {
                checkNotNull(value, "Null values are not allowed");
                return value.toString();
            }

            @Override
            public Boolean decode(String value) {
                checkNotNull(value, "Null values are not allowed");

                String lowercase = value.toLowerCase();
                if(!lowercase.equals("true") && !lowercase.equals("false"))
                    throw new RuntimeException("The value " + value + " is not a valid boolean.");

                return parseBoolean(lowercase);
            }
        };
    }

    public static TypeEncoder<Byte, String> byteEncoder() {
        return new AbstractByteEncoder<String>() {
            @Override
            public String encode(Byte value) {
                checkNotNull(value, "Null values are not allowed");
                return value.toString();
            }

            @Override
            public Byte decode(String value) {
                checkNotNull(value, "Null values are not allowed");
                return parseByte(value);
            }
        };
    }

    public static TypeEncoder<Date, String> dateEncoder() {
        return new AbstractDateEncoder<String>() {
            @Override
            public String encode(Date value) {
                checkNotNull(value, "Null values are not allowed");
                return Long.toString(value.getTime());
            }

            @Override
            public Date decode(String value) {
                checkNotNull(value, "Null values are not allowed");
                return new Date(parseLong(value));
            }
        };
    }

    public static TypeEncoder<Double, String> doubleEncoder() {
        return new AbstractDoubleEncoder<String>() {
            @Override
            public String encode(Double value) {
                checkNotNull(value, "Null values are not allowed");
                return value.toString();
            }

            @Override
            public Double decode(String value) {
                checkNotNull(value, "Null values are not allowed");
                return parseDouble(value);
            }
        };
    }

    public static TypeEncoder<Float, String> floatEncoder() {
        return new AbstractFloatEncoder<String>() {
            @Override
            public String encode(Float value) {
                checkNotNull(value, "Null values are not allowed");
                return value.toString();
            }

            @Override
            public Float decode(String value) {
                checkNotNull(value, "Null values are not allowed");
                return parseFloat(value);
            }
        };
    }

    public static TypeEncoder<Integer, String> integerEncoder() {
        return new AbstractIntegerEncoder<String>() {
            @Override
            public String encode(Integer value) {
                checkNotNull(value, "Null values are not allowed");
                return value.toString();
            }

            @Override
            public Integer decode(String value) {
                checkNotNull(value, "Null values are not allowed");
                return parseInt(value);
            }
        };
    }

    public static TypeEncoder<IPv4, String> ipv4Encoder() {
        return new AbstractIPv4Encoder<String>() {
            @Override
            public String encode(IPv4 value) {
                checkNotNull(value, "Null values are not allowed");
                return value.toString();
            }

            @Override
            public IPv4 decode(String value) {
                checkNotNull(value, "Null values are not allowed");
                return new IPv4(value);
            }
        };
    }

    public static TypeEncoder<Long, String> longEncoder() {
        return new AbstractLongEncoder<String>() {
            @Override
            public String encode(Long value) {
                checkNotNull(value, "Null values are not allowed");
                return value.toString();
            }

            @Override
            public Long decode(String value) {
                checkNotNull(value, "Null values are not allowed");
                return parseLong(value);
            }
        };
    }

    public static TypeEncoder<String, String> stringEncoder() {
        return new AbstractStringEncoder<String>() {
            @Override
            public String encode(String value) {
                checkNotNull(value, "Null values are not allowed");
                return value;
            }

            @Override
            public String decode(String value) {
                checkNotNull(value, "Null values are not allowed");
                return value;
            }
        };
    }

    public static TypeEncoder<URI, String> uriEncoder() {
        return new AbstractURIEncoder<String>() {
            @Override
            public String encode(URI value) {
                checkNotNull(value, "Null values are not allowed");
                return value.toString();
            }

            @Override
            public URI decode(String value) throws TypeDecodingException {
                checkNotNull(value, "Null values are not allowed");
                try {
                    return new URI(value);
                } catch (URISyntaxException e) {
                    throw new TypeDecodingException(e);
                }
            }
        };
    }
}
