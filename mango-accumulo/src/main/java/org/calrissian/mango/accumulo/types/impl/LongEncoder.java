package org.calrissian.mango.accumulo.types.impl;

import org.calrissian.mango.types.encoders.AbstractLongEncoder;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.calrissian.mango.accumulo.types.impl.EncodingUtils.encodeULong;
import static org.calrissian.mango.accumulo.types.impl.EncodingUtils.fromHex;

public class LongEncoder extends AbstractLongEncoder<String> {
    @Override
    public String encode(Long value) {
        checkNotNull(value, "Null values are not allowed");
        return encodeULong(value ^ Long.MIN_VALUE);
    }

    @Override
    public Long decode(String value) {
        checkNotNull(value, "Null values are not allowed");
        return fromHex(value) ^ Long.MIN_VALUE;
    }
}
