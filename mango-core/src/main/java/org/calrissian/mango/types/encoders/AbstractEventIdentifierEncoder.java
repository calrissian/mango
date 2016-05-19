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

import org.calrissian.mango.domain.event.EventIdentifier;
import org.calrissian.mango.types.TypeEncoder;

import static org.calrissian.mango.types.encoders.AliasConstants.EVENT_IDENTIFIER_ALIAS;

public abstract class AbstractEventIdentifierEncoder<U> implements TypeEncoder<EventIdentifier, U> {

    @Override
    public String getAlias() {
        return EVENT_IDENTIFIER_ALIAS;
    }

    @Override
    public Class<EventIdentifier> resolves() {
        return EventIdentifier.class;
    }
}
