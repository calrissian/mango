package org.calrissian.mango.accumulo.types.impl;


import org.calrissian.mango.types.encoders.AbstractIntegerEncoder;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.calrissian.mango.accumulo.types.impl.EncodingUtils.encodeUInt;
import static org.calrissian.mango.accumulo.types.impl.EncodingUtils.fromHex;

public class IntegerEncoder extends AbstractIntegerEncoder<String> {
    @Override
    public String encode(Integer value) {
        checkNotNull(value, "Null values are not allowed");
        return encodeUInt(value ^ Integer.MIN_VALUE);
    }

    @Override
    public Integer decode(String value) {
        checkNotNull(value, "Null values are not allowed");
        return (int)fromHex(value) ^ Integer.MIN_VALUE;
    }
}
