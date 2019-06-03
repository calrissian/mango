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


import org.calrissian.mango.types.encoders.AbstractInstantEncoder;

import java.time.Instant;

import static com.google.common.base.Preconditions.checkArgument;
import static java.time.Instant.ofEpochSecond;
import static java.util.Objects.requireNonNull;

public class InstantEncoder extends AbstractInstantEncoder<String> {
    private static final long serialVersionUID = 1L;

    private static final LongEncoder longEncoder = new LongEncoder();
    private static final IntegerEncoder intEncoder = new IntegerEncoder();

    @Override
    public String encode(Instant value) {
        requireNonNull(value, "Null values are not allowed");
        return longEncoder.encode(value.getEpochSecond()) + intEncoder.encode(value.getNano());
    }

    @Override
    public Instant decode(String value) {
        requireNonNull(value, "Null values are not allowed");
        checkArgument(value.length() == 24, "The value is not a valid encoding");
        return ofEpochSecond(longEncoder.decode(value.substring(0, 16)), intEncoder.decode(value.substring(16)));
    }
}
