package org.calrissian.mango.accumulo.types.impl;


import org.calrissian.mango.types.encoders.AbstractDateEncoder;

import java.util.Date;

import static com.google.common.base.Preconditions.checkNotNull;

public class DateEncoder extends AbstractDateEncoder<String> {

    private static final LongEncoder longEncoder = new LongEncoder();

    @Override
    public String encode(Date value) {
        checkNotNull(value, "Null values are not allowed");
        return longEncoder.encode(value.getTime());
    }

    @Override
    public Date decode(String value) {
        checkNotNull(value, "Null values are not allowed");
        return new Date(longEncoder.decode(value));
    }
}
