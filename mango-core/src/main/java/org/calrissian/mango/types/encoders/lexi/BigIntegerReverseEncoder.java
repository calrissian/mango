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
package org.calrissian.mango.types.encoders.lexi;

import org.calrissian.mango.types.encoders.AbstractBigIntegerEncoder;
import org.calrissian.mango.types.exception.TypeDecodingException;

import java.math.BigInteger;

import static java.util.Objects.requireNonNull;

public class BigIntegerReverseEncoder extends AbstractBigIntegerEncoder<String> {
    private static final long serialVersionUID = 1L;

    private static BigIntegerEncoder bigIntEncoder = new BigIntegerEncoder();

    @Override
    public String encode(BigInteger value) {
        requireNonNull(value, "Null values are not allowed");
        return bigIntEncoder.encode(value.not());
    }

    @Override
    public BigInteger decode(String value) throws TypeDecodingException {
        return bigIntEncoder.decode(value).not();
    }
}
