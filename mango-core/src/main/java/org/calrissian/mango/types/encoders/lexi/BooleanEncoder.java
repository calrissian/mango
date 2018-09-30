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


import org.calrissian.mango.types.encoders.AbstractBooleanEncoder;

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.Objects.requireNonNull;

public class BooleanEncoder extends AbstractBooleanEncoder<String> {
    private static final long serialVersionUID = 1L;

    @Override
    public String encode(Boolean value) {
        requireNonNull(value, "Null values are not allowed");
        return (value ? "1" : "0");
    }

    @Override
    public Boolean decode(String value) {
        requireNonNull(value, "Null values are not allowed");
        checkArgument(value.equals("1") || value.equals("0"), "The value is not a valid encoding");
        return value.equals("1");
    }
}
