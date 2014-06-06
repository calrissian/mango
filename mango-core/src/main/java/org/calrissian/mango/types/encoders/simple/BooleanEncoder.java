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
package org.calrissian.mango.types.encoders.simple;


import org.calrissian.mango.types.encoders.AbstractBooleanEncoder;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.lang.Boolean.parseBoolean;

public class BooleanEncoder extends AbstractBooleanEncoder<String> {
    private static final long serialVersionUID = 1L;

    @Override
    public String encode(Boolean value) {
        checkNotNull(value, "Null values are not allowed");
        return value.toString();
    }

    @Override
    public Boolean decode(String value) {
        checkNotNull(value, "Null values are not allowed");

        String lowercase = value.toLowerCase();
        if (!lowercase.equals("true") && !lowercase.equals("false"))
            throw new IllegalArgumentException("The value is not a valid encoding");

        return parseBoolean(lowercase);
    }
}
