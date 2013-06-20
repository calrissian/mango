package org.calrissian.mango.types.normalizers;

import org.calrissian.mango.types.TypeNormalizer;
import org.calrissian.mango.types.exception.TypeNormalizationException;

public class LongNormalizer implements TypeNormalizer<Long> {

    private int padding = 20;

    @Override
    public String normalize(Long obj) throws TypeNormalizationException {
        try {
            String signPadding = obj < 0 ? "" : "0";
            return String.format("%s%019d", signPadding, obj);
        }
        catch (Exception e) {
            throw new TypeNormalizationException(e);
        }
    }

    @Override
    public Long denormalize(String str) throws TypeNormalizationException {
        try {
            return Long.parseLong(str);
        }
        catch (Exception e) {
            throw new TypeNormalizationException(e);
        }
    }

    @Override
    public String getAlias() {
        return "long";
    }

    @Override
    public Class resolves() {
        return Long.class;
    }

    @Override
    public Long fromString(String str) throws TypeNormalizationException {
        return denormalize(str);
    }

    @Override
    public String asString(Long obj) throws TypeNormalizationException {
        try {
            return obj.toString();
        }
        catch (Exception e) {
            throw new TypeNormalizationException(e);
        }
    }
}
