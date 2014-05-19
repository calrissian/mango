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
package org.calrissian.mango.types.encoders.lexi;


import static java.lang.Character.digit;
import static java.lang.Double.doubleToRawLongBits;
import static java.lang.Double.longBitsToDouble;
import static java.lang.Float.floatToIntBits;
import static java.lang.Float.intBitsToFloat;

class EncodingUtils {

    /**
     * Returns the raw bit representation of a string of hex digits.
     * <p/>
     * Helper function simply because Long.parseLong(hex,16) does not handle negative numbers that were
     * converted to hex.
     */
    protected static long fromHex(String hex) {
        long value = 0;
        for (int i = 0; i < hex.length(); i++)
            value = (value << 4) | digit(hex.charAt(i), 16);

        return value;
    }

    protected static String encodeULong(long value) {
        return String.format("%016x", value);
    }

    protected static String encodeUInt(int value) {
        return String.format("%08x", value);
    }

    protected static int normalizeFloat(float value) {
        if (value > 0)
            return floatToIntBits(value) ^ Integer.MIN_VALUE;
        else
            return ~floatToIntBits(value);
    }

    protected static float denormalizeFloat(int value) {
        if (value > 0)
            return intBitsToFloat(~value);
        else
            return intBitsToFloat(value ^ Integer.MIN_VALUE);
    }

    protected static long normalizeDouble(double value) {
        if (value > 0)
            return doubleToRawLongBits(value) ^ Long.MIN_VALUE;
        else
            return ~doubleToRawLongBits(value);
    }

    protected static double denormalizeDouble(long value) {
        if (value > 0)
            return longBitsToDouble(~value);
        else
            return longBitsToDouble(value ^ Long.MIN_VALUE);
    }

    protected static byte[] reverse(byte[] bytes) {
        byte[] result = new byte[bytes.length];

        for (int i = 0; i < bytes.length; i++)
            result[i] = (byte) (0xff - (0xff & bytes[i]));

        return result;
    }
}
