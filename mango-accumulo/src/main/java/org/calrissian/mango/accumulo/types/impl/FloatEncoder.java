package org.calrissian.mango.accumulo.types.impl;


import org.calrissian.mango.types.encoders.AbstractFloatEncoder;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.calrissian.mango.accumulo.types.impl.EncodingUtils.*;

public class FloatEncoder extends AbstractFloatEncoder<String> {
    @Override
    public String encode(Float value) {
        checkNotNull(value, "Null values are not allowed");
        return encodeUInt(normalizeFloat(value));
    }

    @Override
    public Float decode(String value) {
        checkNotNull(value, "Null values are not allowed");
        return denormalizeFloat((int) fromHex(value));
    }
}
