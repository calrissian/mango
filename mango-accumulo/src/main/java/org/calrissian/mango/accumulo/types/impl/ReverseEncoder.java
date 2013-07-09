package org.calrissian.mango.accumulo.types.impl;


import org.calrissian.mango.types.TypeEncoder;
import org.calrissian.mango.types.exception.TypeDecodingException;
import org.calrissian.mango.types.exception.TypeEncodingException;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.calrissian.mango.accumulo.types.impl.EncodingUtils.reverse;

public class ReverseEncoder <T> implements TypeEncoder<T, String> {

    private final TypeEncoder<T, String> encoder;

    public ReverseEncoder(TypeEncoder<T, String> encoder) {
        this.encoder = encoder;
    }

    @Override
    public String getAlias() {
        return encoder.getAlias();
    }

    @Override
    public Class<T> resolves() {
        return encoder.resolves();
    }

    @Override
    public String encode(T value) throws TypeEncodingException {
        String encoded = encoder.encode(value);
        return new String(reverse(encoded.getBytes()));
    }

    @Override
    public T decode(String value) throws TypeDecodingException {
        checkNotNull(value, "Null values are not allowed");

        String reversed = new String(reverse(value.getBytes()));
        return encoder.decode(reversed);
    }
}
