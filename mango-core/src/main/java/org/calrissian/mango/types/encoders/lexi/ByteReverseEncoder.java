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
package org.calrissian.mango.types.encoders.lexi;

import org.calrissian.mango.types.encoders.AbstractByteEncoder;

import static java.util.Objects.requireNonNull;

public class ByteReverseEncoder extends AbstractByteEncoder<String> {
    private static final long serialVersionUID = 1L;

    private static final ByteEncoder byteEncoder = new ByteEncoder();

    @Override
    public String encode(Byte value) {
        requireNonNull(value, "Null values are not allowed");
        return byteEncoder.encode((byte) ~value);
    }

    @Override
    public Byte decode(String value) {
        return (byte) ~byteEncoder.decode(value);
    }
}
