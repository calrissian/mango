package mango.types.normalizers;

import mango.types.TypeNormalizer;
import mango.types.exception.TypeNormalizationException;

import java.util.Date;

public class DateNormalizer implements TypeNormalizer<Date> {

    @Override
    public String normalize(Date obj) throws TypeNormalizationException {

        try {
            return String.format("%d", obj.getTime());
        }

        catch(Exception e) {
            throw new TypeNormalizationException(e);
        }
    }

    @Override
    public Date denormalize(String str) throws TypeNormalizationException {
        try {
            return new Date(Long.parseLong(str));
        }

        catch(Exception e) {
            throw new TypeNormalizationException(e);
        }
    }

    @Override
    public String getAlias() {
        return "date";
    }

    @Override
    public Class resolves() {
        return Date.class;
    }

    @Override
    public Date fromString(String str) throws TypeNormalizationException {
        return denormalize(str);
    }

    @Override
    public String asString(Date obj) throws TypeNormalizationException {
        return normalize(obj);
    }
}
