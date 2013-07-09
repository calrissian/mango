package org.calrissian.mango.accumulo.types.impl;


import org.calrissian.mango.types.encoders.AbstractLongEncoder;

import static com.google.common.base.Preconditions.checkNotNull;

public class LongReverseEncoder extends AbstractLongEncoder<String> {

    private static final LongEncoder longEncoder = new LongEncoder();

    @Override
    public String encode(Long value) {
        checkNotNull(value, "Null values are not allowed");
        return longEncoder.encode(~value);
    }

    @Override
    public Long decode(String value) {
        checkNotNull(value, "Null values are not allowed");
        return ~longEncoder.decode(value);
    }
}
