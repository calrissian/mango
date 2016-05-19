/*
 * Copyright (C) 2016 The Calrissian Authors
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
package org.calrissian.mango.types.encoders;

import com.google.common.primitives.UnsignedInteger;
import org.calrissian.mango.types.TypeEncoder;

import static org.calrissian.mango.types.encoders.AliasConstants.UNSIGNEDINTEGER_ALIAS;

public abstract class AbstractUnsignedIntegerEncoder<U> implements TypeEncoder<UnsignedInteger, U> {
    @Override
    public String getAlias() {
        return UNSIGNEDINTEGER_ALIAS;
    }

    @Override
    public Class<UnsignedInteger> resolves() {
        return UnsignedInteger.class;
    }
}
