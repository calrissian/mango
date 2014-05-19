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


import org.calrissian.mango.types.TypeEncoder;
import org.calrissian.mango.types.exception.TypeDecodingException;
import org.calrissian.mango.types.exception.TypeEncodingException;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.calrissian.mango.types.encoders.lexi.EncodingUtils.reverse;

public class ReverseEncoder<T> implements TypeEncoder<T, String> {

    private final TypeEncoder<T, String> encoder;

    public ReverseEncoder(TypeEncoder<T, String> encoder) {
        this.encoder = encoder;
    }

    @Override
    public String getAlias() {
        return encoder.getAlias();
    }

    @Override
    public Class<T> resolves() {
        return encoder.resolves();
    }

    @Override
    public String encode(T value) throws TypeEncodingException {
        String encoded = encoder.encode(value);
        return new String(reverse(encoded.getBytes()));
    }

    @Override
    public T decode(String value) throws TypeDecodingException {
        checkNotNull(value, "Null values are not allowed");

        String reversed = new String(reverse(value.getBytes()));
        return encoder.decode(reversed);
    }
}
