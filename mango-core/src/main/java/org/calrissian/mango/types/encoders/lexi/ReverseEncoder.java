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

import java.nio.charset.Charset;

import static com.google.common.base.Preconditions.checkNotNull;

public class ReverseEncoder<T> implements TypeEncoder<T, String> {
    private static final long serialVersionUID = 1L;

    //This is defined in Java 7 under StandardCharsets and should be replaced in the future.
    private static final Charset ISO_8859_1 = Charset.forName("ISO-8859-1");

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
    public String encode(T value) {
        String encoded = encoder.encode(value);
        return new String(reverse(encoded.getBytes()), ISO_8859_1);
    }

    @Override
    public T decode(String value) {
        checkNotNull(value, "Null values are not allowed");
        String reversed = new String(reverse(value.getBytes(ISO_8859_1)));
        return encoder.decode(reversed);
    }

    private static byte[] reverse(byte[] bytes) {
        byte[] result = new byte[bytes.length];

        for (int i = 0; i < bytes.length; i++)
            result[i] = (byte) (0xff - (0xff & bytes[i]));

        return result;
    }
}
