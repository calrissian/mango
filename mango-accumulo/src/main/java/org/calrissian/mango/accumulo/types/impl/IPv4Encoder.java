package org.calrissian.mango.accumulo.types.impl;


import org.calrissian.mango.domain.IPv4;
import org.calrissian.mango.types.encoders.AbstractIPv4Encoder;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.calrissian.mango.accumulo.types.impl.EncodingUtils.encodeUInt;
import static org.calrissian.mango.accumulo.types.impl.EncodingUtils.fromHex;

public class IPv4Encoder extends AbstractIPv4Encoder<String> {
    @Override
    public String encode(IPv4 value) {
        checkNotNull(value, "Null values are not allowed");
        return encodeUInt((int)value.getValue());
    }

    @Override
    public IPv4 decode(String value) {
        checkNotNull(value, "Null values are not allowed");
        return new IPv4(fromHex(value));
    }
}