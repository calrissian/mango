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

import org.calrissian.mango.types.encoders.AbstractLongEncoder;

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.Objects.requireNonNull;
import static org.calrissian.mango.types.encoders.lexi.EncodingUtils.encodeULong;
import static org.calrissian.mango.types.encoders.lexi.EncodingUtils.fromHex;

public class LongEncoder extends AbstractLongEncoder<String> {
    private static final long serialVersionUID = 1L;

    @Override
    public String encode(Long value) {
        requireNonNull(value, "Null values are not allowed");
        return encodeULong(value ^ Long.MIN_VALUE);
    }

    @Override
    public Long decode(String value) {
        requireNonNull(value, "Null values are not allowed");
        checkArgument(value.length() == 16, "The value is not a valid encoding");
        return fromHex(value) ^ Long.MIN_VALUE;
    }
}
