package org.calrissian.mango.accumulo.types.impl;


import org.calrissian.mango.types.encoders.AbstractIntegerEncoder;

import static com.google.common.base.Preconditions.checkNotNull;

public class IntegerReverseEncoder extends AbstractIntegerEncoder<String> {

    private static final IntegerEncoder intEncoder = new IntegerEncoder();

    @Override
    public String encode(Integer value) {
        checkNotNull(value, "Null values are not allowed");
        return intEncoder.encode(~value);
    }

    @Override
    public Integer decode(String value) {
        checkNotNull(value, "Null values are not allowed");
        return ~intEncoder.decode(value);
    }
}
