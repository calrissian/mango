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

import org.calrissian.mango.types.encoders.AbstractBigDecimalEncoder;

import java.math.BigDecimal;
import java.math.BigInteger;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

public class BigDecimalEncoder extends AbstractBigDecimalEncoder<String> {
    private static final long serialVersionUID = 1L;

    private static final IntegerEncoder intEncoder = new IntegerEncoder();

    /**
     * Used to provide natural ordering for negative numbers.  We are forced to use
     * 10's complement due to the way numbers are represented in BigDecimal.
     *
     * Assumes a string where all characters are 0-9 (0x30 - 0x39 ascii).
     */
    private static String tensComplement(String digitStr) {
        byte[] digits = digitStr.getBytes();
        byte[] comp = new byte[digits.length];

        //9's complement
        for (int i = 0;i< digits.length;i++)
            comp[i] = (byte)((9 - (digits[i] & 0x0f)) ^ 0x30);

        //add 1
        for (int i = comp.length -1; i >=0 ;i--) {
            comp[i] += 1;

            //handle overlow if exceeded 0x39 and continue backward.
            if (comp[i] != 0x3A)
                break;

            comp[i] = 0x30;
        }

        return new String(comp);
    }

    @Override
    public String encode(BigDecimal value) {
        checkNotNull(value, "Null values are not allowed");

        int exp = value.precision() - value.scale() - 1;
        String mantissa;

        if (value.signum() == 0) {
            //Zero requires special handling as BigInteger.toString() has a special shortcutting for zeros which
            //ignores the least significant zeros.  These are required however to reconstruct the original scale.
            mantissa = new String(new char[1 - exp]).replace('\0', '0');
            exp = 0;
        } else if (value.signum() < 0) {
            mantissa = tensComplement(value.unscaledValue().negate().toString());
            exp = -exp;
        } else {
            mantissa = value.unscaledValue().toString();
        }

        return (value.signum() < 0 ? "0" : "1") + intEncoder.encode(exp) + mantissa;
    }

    @Override
    public BigDecimal decode(String value) {
        checkNotNull(value, "Null values are not allowed");
        checkArgument(value.length() > 9, "The value is not a valid encoding");

        int exp = intEncoder.decode(value.substring(1, 9));
        String mantissa = value.substring(9);
        BigInteger unscaled;
        if (value.charAt(0) == '0') {
            exp = -exp;
            unscaled = new BigInteger(tensComplement(mantissa)).negate();
        } else {
            unscaled = new BigInteger(mantissa);
        }

        return new BigDecimal(unscaled, mantissa.length() - exp - 1);
    }
}
