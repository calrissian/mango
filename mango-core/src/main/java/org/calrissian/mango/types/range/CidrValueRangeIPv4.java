package org.calrissian.mango.types.range;

import org.apache.commons.net.util.SubnetUtils;
import org.calrissian.mango.types.types.IPv4;


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
