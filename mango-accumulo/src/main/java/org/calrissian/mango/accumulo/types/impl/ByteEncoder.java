package org.calrissian.mango.accumulo.types.impl;


import org.calrissian.mango.types.encoders.AbstractByteEncoder;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.calrissian.mango.accumulo.types.impl.EncodingUtils.fromHex;

public class ByteEncoder extends AbstractByteEncoder<String> {
    @Override
    public String encode(Byte value) {
        checkNotNull(value, "Null values are not allowed");
        return String.format("%02x", value);
    }

    @Override
    public Byte decode(String value) {
        checkNotNull(value, "Null values are not allowed");
        return (byte) fromHex(value);
    }
}
