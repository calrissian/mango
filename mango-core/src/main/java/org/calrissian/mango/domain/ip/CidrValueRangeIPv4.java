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
package org.calrissian.mango.domain.ip;

import com.google.common.collect.Range;
import org.calrissian.mango.domain.ValueRange;

@Deprecated
public class CidrValueRangeIPv4 extends ValueRange<IPv4> {

    public CidrValueRangeIPv4(String cidrString) {

        Range<IPv4> range = IPv4.cidrRange(cidrString);

        setStart(range.lowerEndpoint());
        setStop(range.upperEndpoint());
    }

    public CidrValueRangeIPv4(IPv4 start, IPv4 stop) {
        super(start, stop);
    }
}
