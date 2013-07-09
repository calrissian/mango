package org.calrissian.mango.accumulo.types.impl;


import org.calrissian.mango.types.encoders.AbstractBooleanEncoder;

import static com.google.common.base.Preconditions.checkNotNull;

public class BooleanReverseEncoder extends AbstractBooleanEncoder<String> {

    private static final BooleanEncoder booleanEncoder = new BooleanEncoder();

    @Override
    public String encode(Boolean value) {
        checkNotNull(value, "Null values are not allowed");
        return booleanEncoder.encode(!value);
    }

    @Override
    public Boolean decode(String value) {
        return !booleanEncoder.decode(value);
    }
}
