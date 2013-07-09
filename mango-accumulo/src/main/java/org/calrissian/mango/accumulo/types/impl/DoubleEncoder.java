package org.calrissian.mango.accumulo.types.impl;


import org.calrissian.mango.types.encoders.AbstractDoubleEncoder;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.calrissian.mango.accumulo.types.impl.EncodingUtils.*;

public class DoubleEncoder extends AbstractDoubleEncoder<String> {
    @Override
    public String encode(Double value) {
        checkNotNull(value, "Null values are not allowed");
        return encodeULong(normalizeDouble(value));
    }

    @Override
    public Double decode(String value) {
        checkNotNull(value, "Null values are not allowed");
        return denormalizeDouble(fromHex(value));
    }
}
