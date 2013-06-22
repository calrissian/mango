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
package org.calrissian.mango.domain;

import org.apache.commons.net.util.SubnetUtils;


public class CidrValueRangeIPv4 extends ValueRange<IPv4> {

    public CidrValueRangeIPv4(String cidrString) {

        SubnetUtils utils = new SubnetUtils(cidrString);
        SubnetUtils.SubnetInfo info = utils.getInfo();

        setStart(new IPv4(info.getNetworkAddress()));
        setStop(new IPv4(info.getBroadcastAddress()));
    }

    public CidrValueRangeIPv4(IPv4 start, IPv4 stop) {
        super(start, stop);
    }
}
