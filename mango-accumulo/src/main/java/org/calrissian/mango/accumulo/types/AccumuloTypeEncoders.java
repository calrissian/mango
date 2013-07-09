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
package org.calrissian.mango.accumulo.types;


import org.calrissian.mango.domain.IPv4;
import org.calrissian.mango.types.TypeEncoder;
import org.calrissian.mango.types.TypeRegistry;
import org.calrissian.mango.types.encoders.*;
import org.calrissian.mango.types.exception.TypeDecodingException;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Date;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.lang.Character.digit;
import static java.lang.Double.doubleToRawLongBits;
import static java.lang.Double.longBitsToDouble;
import static java.lang.Float.floatToIntBits;
import static java.lang.Float.intBitsToFloat;

/**
 * Default type encoders for normalizing data to
 *
 * These methods implement the default implementations for the old TypesNormalizer's normalize and denormalize
 * functions.
 */
public class AccumuloTypeEncoders {

    public static final TypeRegistry<String> ACCUMULO_TYPES = new TypeRegistry<String>(
            booleanEncoder(), byteEncoder(), dateEncoder(), doubleEncoder(), floatEncoder(),
            integerEncoder(), ipv4Encoder(), longEncoder(), stringEncoder(), uriEncoder()
    );

    public static final TypeRegistry<String> ACCUMULO_REVERSE_TYPES = new TypeRegistry<String>(
            reverseBooleanEncoder(), reverseByteEncoder(), reverseDateEncoder(), reverseDoubleEncoder(),
            reverseFloatEncoder(), reverseIntegerEncoder(), reverseIPv4Encoder(), reverseLongEncoder()
    );

    /**
     * Returns the raw bit representation of a string of hex digits.
     *
     * Helper function simply because Long.parseLong(hex,16) does not handle negative numbers that were
     * converted to hex.
     */
    private static long fromHex(String hex) {
        long value = 0;
        for (int i = 0; i < hex.length(); i++)
            value = (value << 4) | digit(hex.charAt(i), 16);

        return value;
    }

    private static String encodeUInt(int value) {
        return String.format("%08x", value);
    }

    /**
     * Encodes an int so that it can be lexicographically sorted.
     */
    private static String encodeInt(int value) {
        //Flip the sign bit before encoding so that negative numbers are lexicographically before
        //positive integers.
        return encodeUInt(value ^ Integer.MIN_VALUE);
    }

    private static int decodeInt(String value) {
        //Flip the sign bit back to the value before encoding.
        return (int)fromHex(value) ^ Integer.MIN_VALUE;
    }

    private static String encodeULong(long value) {
        return String.format("%016x", value);
    }

    private static String encodeLong(long value) {
        //Flip the sign bit before encoding so that negative numbers are lexicographically before
        //positive integers.
        return encodeULong(value ^ Long.MIN_VALUE);
    }

    private static long decodeLong(String value) {
        //Flip the sign bit back to the value before encoding.
        return fromHex(value) ^ Long.MIN_VALUE;
    }

    private static int normalizeFloat(float value) {
        if (value > 0)
            return floatToIntBits(value) ^ Integer.MIN_VALUE;
        else
            return ~floatToIntBits(value);
    }

    private static float denormalizeFloat(int value) {
        if (value > 0)
            return intBitsToFloat(~value);
        else
            return intBitsToFloat(value ^ Integer.MIN_VALUE);
    }

    private static long normalizeDouble(double value) {
        if (value > 0)
            return doubleToRawLongBits(value) ^ Long.MIN_VALUE;
        else
            return ~doubleToRawLongBits(value);
    }

    private static double denormalizeDouble(long value) {
        if (value > 0)
            return longBitsToDouble(~value);
        else
            return longBitsToDouble(value ^ Long.MIN_VALUE);
    }

    public static TypeEncoder<Boolean, String> booleanEncoder() {
        return new AbstractBooleanEncoder<String>() {
            @Override
            public String encode(Boolean value) {
                checkNotNull(value, "Null values are not allowed");
                return (value ? "1" : "0");
            }

            @Override
            public Boolean decode(String value) {
                checkNotNull(value, "Null values are not allowed");

                String lowercase = value.toLowerCase();
                if(!lowercase.equals("1") && !lowercase.equals("0"))
                    throw new RuntimeException("The value " + value + " is not a valid boolean.");

                return value.equals("1");
            }
        };
    }

    public static TypeEncoder<Boolean, String> reverseBooleanEncoder() {
        return new AbstractBooleanEncoder<String>() {
            @Override
            public String encode(Boolean value) {
                checkNotNull(value, "Null values are not allowed");
                return (value ? "0" : "1");
            }

            @Override
            public Boolean decode(String value) {
                checkNotNull(value, "Null values are not allowed");

                String lowercase = value.toLowerCase();
                if(!lowercase.equals("1") && !lowercase.equals("0"))
                    throw new RuntimeException("The value " + value + " is not a valid boolean.");

                return value.equals("0");
            }
        };
    }

    public static TypeEncoder<Byte, String> byteEncoder() {
        return new AbstractByteEncoder<String>() {
            @Override
            public String encode(Byte value) {
                checkNotNull(value, "Null values are not allowed");
                return String.format("%02x", value);
            }

            @Override
            public Byte decode(String value) {
                checkNotNull(value, "Null values are not allowed");
                return (byte) fromHex(value);
            }
        };
    }

    public static TypeEncoder<Byte, String> reverseByteEncoder() {
        return new AbstractByteEncoder<String>() {
            @Override
            public String encode(Byte value) {
                checkNotNull(value, "Null values are not allowed");
                return String.format("%02x", (byte)~value);
            }

            @Override
            public Byte decode(String value) {
                checkNotNull(value, "Null values are not allowed");
                return (byte) ~fromHex(value);
            }
        };
    }

    public static TypeEncoder<Date, String> dateEncoder() {
        return new AbstractDateEncoder<String>() {
            @Override
            public String encode(Date value) {
                checkNotNull(value, "Null values are not allowed");
                return encodeLong(value.getTime());
            }

            @Override
            public Date decode(String value) {
                checkNotNull(value, "Null values are not allowed");
                return new Date(decodeLong(value));
            }
        };
    }

    public static TypeEncoder<Date, String> reverseDateEncoder() {
        return new AbstractDateEncoder<String>() {
            @Override
            public String encode(Date value) {
                checkNotNull(value, "Null values are not allowed");
                return encodeLong(~value.getTime());
            }

            @Override
            public Date decode(String value) {
                checkNotNull(value, "Null values are not allowed");
                return new Date(~decodeLong(value));
            }
        };
    }

    public static TypeEncoder<Double, String> doubleEncoder() {
        return new AbstractDoubleEncoder<String>() {
            @Override
            public String encode(Double value) {
                checkNotNull(value, "Null values are not allowed");
                return encodeULong(normalizeDouble(value));
            }

            @Override
            public Double decode(String value) {
                checkNotNull(value, "Null values are not allowed");
                return denormalizeDouble(fromHex(value));
            }
        };
    }

    public static TypeEncoder<Double, String> reverseDoubleEncoder() {
        return new AbstractDoubleEncoder<String>() {
            @Override
            public String encode(Double value) {
                checkNotNull(value, "Null values are not allowed");
                return encodeULong(~normalizeDouble(value));
            }

            @Override
            public Double decode(String value) {
                checkNotNull(value, "Null values are not allowed");
                return denormalizeDouble(~fromHex(value));
            }
        };
    }

    public static TypeEncoder<Float, String> floatEncoder() {
        return new AbstractFloatEncoder<String>() {
            @Override
            public String encode(Float value) {
                checkNotNull(value, "Null values are not allowed");
                return encodeUInt(normalizeFloat(value));
            }

            @Override
            public Float decode(String value) {
                checkNotNull(value, "Null values are not allowed");
                return denormalizeFloat((int)fromHex(value));
            }
        };
    }

    public static TypeEncoder<Float, String> reverseFloatEncoder() {
        return new AbstractFloatEncoder<String>() {
            @Override
            public String encode(Float value) {
                checkNotNull(value, "Null values are not allowed");
                return encodeUInt(~normalizeFloat(value));
            }

            @Override
            public Float decode(String value) {
                checkNotNull(value, "Null values are not allowed");
                return denormalizeFloat(~(int)fromHex(value));
            }
        };
    }

    public static TypeEncoder<Integer, String> integerEncoder() {
        return new AbstractIntegerEncoder<String>() {
            @Override
            public String encode(Integer value) {
                checkNotNull(value, "Null values are not allowed");
                return encodeInt(value);
            }

            @Override
            public Integer decode(String value) {
                checkNotNull(value, "Null values are not allowed");
                return decodeInt(value);
            }
        };
    }

    public static TypeEncoder<Integer, String> reverseIntegerEncoder() {
        return new AbstractIntegerEncoder<String>() {
            @Override
            public String encode(Integer value) {
                checkNotNull(value, "Null values are not allowed");
                return encodeInt(~value);
            }

            @Override
            public Integer decode(String value) {
                checkNotNull(value, "Null values are not allowed");
                return ~decodeInt(value);
            }
        };
    }

    public static TypeEncoder<IPv4, String> ipv4Encoder() {
        return new AbstractIPv4Encoder<String>() {
            @Override
            public String encode(IPv4 value) {
                checkNotNull(value, "Null values are not allowed");
                return encodeUInt((int)value.getValue());
            }

            @Override
            public IPv4 decode(String value) {
                checkNotNull(value, "Null values are not allowed");
                return new IPv4(fromHex(value));
            }
        };
    }

    public static TypeEncoder<IPv4, String> reverseIPv4Encoder() {
        return new AbstractIPv4Encoder<String>() {
            @Override
            public String encode(IPv4 value) {
                checkNotNull(value, "Null values are not allowed");
                return encodeUInt(~(int)value.getValue());
            }

            @Override
            public IPv4 decode(String value) {
                checkNotNull(value, "Null values are not allowed");
                return new IPv4(~fromHex(value) & 0xffffffffL);
            }
        };
    }

    public static TypeEncoder<Long, String> longEncoder() {
        return new AbstractLongEncoder<String>() {
            @Override
            public String encode(Long value) {
                checkNotNull(value, "Null values are not allowed");
                return encodeLong(value);
            }

            @Override
            public Long decode(String value) {
                checkNotNull(value, "Null values are not allowed");
                return decodeLong(value);
            }
        };
    }

    public static TypeEncoder<Long, String> reverseLongEncoder() {
        return new AbstractLongEncoder<String>() {
            @Override
            public String encode(Long value) {
                checkNotNull(value, "Null values are not allowed");
                return encodeLong(~value);
            }

            @Override
            public Long decode(String value) {
                checkNotNull(value, "Null values are not allowed");
                return ~decodeLong(value);
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
                checkNotNull(value, "Null vallues are not allowed");
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
