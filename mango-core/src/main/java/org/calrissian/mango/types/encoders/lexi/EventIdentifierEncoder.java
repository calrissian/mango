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

import org.calrissian.mango.domain.event.EventIdentifier;
import org.calrissian.mango.types.encoders.AbstractEventIdentifierEncoder;
import org.calrissian.mango.types.exception.TypeDecodingException;
import org.calrissian.mango.types.exception.TypeEncodingException;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static java.lang.Long.parseLong;
import static java.lang.String.format;

public class EventIdentifierEncoder extends AbstractEventIdentifierEncoder<String> {
    private static final long serialVersionUID = 1L;

    private static final String SCHEME = "event://";
    private static final LongEncoder longEncoder = new LongEncoder();

    @Override
    public String encode(EventIdentifier value) throws TypeEncodingException {
        checkNotNull(value, "Null values are not allowed");
        return format("%s%s#%s@%s", SCHEME, value.getType(), value.getId(), longEncoder.encode(value.getTimestamp()));
    }

    @Override
    public EventIdentifier decode(String value) throws TypeDecodingException {
        checkNotNull(value, "Null values are not allowed");
        checkArgument(value.startsWith(SCHEME) && value.contains("#") && value.contains("@"), "The value is not a valid encoding");

        String rel = value.substring(SCHEME.length(), value.length());
        int idx = rel.indexOf('#');
        int tsIdx = rel.lastIndexOf('@');
        return new EventIdentifier(rel.substring(0, idx), rel.substring(idx + 1, tsIdx), longEncoder.decode(rel.substring(tsIdx + 1)));
    }
}
