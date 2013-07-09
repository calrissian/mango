package org.calrissian.mango.accumulo.types.impl;

import org.calrissian.mango.types.encoders.AbstractByteEncoder;

import static com.google.common.base.Preconditions.checkNotNull;

public class ByteReverseEncoder extends AbstractByteEncoder<String> {

    private static final ByteEncoder byteEncoder = new ByteEncoder();

    @Override
    public String encode(Byte value) {
        checkNotNull(value, "Null values are not allowed");
        return byteEncoder.encode((byte)~value);
    }

    @Override
    public Byte decode(String value) {
        checkNotNull(value, "Null values are not allowed");
        return (byte)~byteEncoder.decode(value);
    }
}
