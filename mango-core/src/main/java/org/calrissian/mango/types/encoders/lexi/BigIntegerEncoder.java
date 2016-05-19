/*
 * Copyright (C) 2016 The Calrissian Authors
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
import org.calrissian.mango.types.encoders.AbstractBigIntegerEncoder;
import org.calrissian.mango.types.exception.TypeDecodingException;

import java.math.BigInteger;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static org.apache.commons.codec.binary.Hex.decodeHex;
import static org.apache.commons.codec.binary.Hex.encodeHex;

public class BigIntegerEncoder extends AbstractBigIntegerEncoder<String> {
    private static final long serialVersionUID = 1L;

    private static IntegerEncoder integerEncoder = new IntegerEncoder();

    @Override
    public String encode(BigInteger value) {
        checkNotNull(value, "Null values are not allowed");

        byte[] bytes = value.toByteArray();
        int length = bytes.length;

        //Length is always positive so use it to encode the actual sign of the big int.
        if (value.signum() < 0)
            length = -length;

        return integerEncoder.encode(length) + new String(encodeHex(bytes));
    }

    @Override
    public BigInteger decode(String value) throws TypeDecodingException {
        checkNotNull(value, "Null values are not allowed");
        checkArgument(value.length() > 8, "The value is not a valid encoding");
        try {
            return new BigInteger(decodeHex(value.substring(8).toCharArray()));
        } catch (DecoderException e) {
            throw new TypeDecodingException(e);
        }
    }
}
