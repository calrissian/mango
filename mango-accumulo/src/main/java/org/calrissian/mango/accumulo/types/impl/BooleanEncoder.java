package org.calrissian.mango.accumulo.types.impl;


import org.calrissian.mango.types.encoders.AbstractBooleanEncoder;

import static com.google.common.base.Preconditions.checkNotNull;

public class BooleanEncoder extends AbstractBooleanEncoder<String> {
    @Override
    public String encode(Boolean value) {
        checkNotNull(value, "Null values are not allowed");
        return (value ? "1" : "0");
    }

    @Override
    public Boolean decode(String value) {
        checkNotNull(value, "Null values are not allowed");

        String lowercase = value.toLowerCase();
        if(!lowercase.equals("1") && !lowercase.equals("0"))
            throw new RuntimeException("The value " + value + " is not a valid boolean.");

        return value.equals("1");
    }
}
