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


import com.google.common.primitives.UnsignedLong;
import org.calrissian.mango.types.encoders.AbstractUnsignedLongEncoder;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.primitives.UnsignedLong.fromLongBits;
import static org.calrissian.mango.types.encoders.lexi.EncodingUtils.encodeULong;
import static org.calrissian.mango.types.encoders.lexi.EncodingUtils.fromHex;

public class UnsignedLongEncoder extends AbstractUnsignedLongEncoder<String> {
    private static final long serialVersionUID = 1L;

    @Override
    public String encode(UnsignedLong value) {
        checkNotNull(value, "Null values are not allowed");
        return encodeULong(value.longValue());
    }

    @Override
    public UnsignedLong decode(String value) {
        checkNotNull(value, "Null values are not allowed");
        checkArgument(value.length() == 16, "The value is not a valid encoding");
        return fromLongBits((int) fromHex(value));
    }
}
