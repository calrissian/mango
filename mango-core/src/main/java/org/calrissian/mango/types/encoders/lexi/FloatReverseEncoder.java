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


import org.calrissian.mango.types.encoders.AbstractFloatEncoder;

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.Objects.requireNonNull;
import static org.calrissian.mango.types.encoders.lexi.EncodingUtils.*;

public class FloatReverseEncoder extends AbstractFloatEncoder<String> {
    private static final long serialVersionUID = 1L;

    @Override
    public String encode(Float value) {
        requireNonNull(value, "Null values are not allowed");
        return encodeUInt(~normalizeFloat(value));
    }

    @Override
    public Float decode(String value) {
        requireNonNull(value, "Null values are not allowed");
        checkArgument(value.length() == 8, "The value is not a valid encoding");
        return denormalizeFloat(~(int) fromHex(value));
    }
}
