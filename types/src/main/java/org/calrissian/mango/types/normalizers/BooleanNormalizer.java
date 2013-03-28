package org.calrissian.mango.types.normalizers;

import org.calrissian.mango.types.TypeNormalizer;
import org.calrissian.mango.types.exception.TypeNormalizationException;

public class BooleanNormalizer implements TypeNormalizer<Boolean> {
    @Override
    public String normalize(Boolean obj) {
        return Boolean.toString(obj);
    }

    @Override
    public Boolean denormalize(String str) throws TypeNormalizationException {

        try {

            if(!validate(str)) {
                throw new RuntimeException("The value " + str + " is not a valid boolean.");
            }

            return Boolean.parseBoolean(str);
        }

        catch(Exception e) {
            throw new TypeNormalizationException(e);
        }
    }

    @Override
    public String getAlias() {
        return "boolean";
    }

    @Override
    public Class resolves() {
        return Boolean.class;
    }

    @Override
    public Boolean fromString(String str) throws TypeNormalizationException {

        if(!validate(str)) {
            throw new RuntimeException("The value " + str + " is not a valid boolean.");
        }

        try {

            return Boolean.parseBoolean(str);
        }

        catch(Exception e) {
            throw new TypeNormalizationException(e);
        }
    }

    @Override
    public String asString(Boolean obj) {
        return obj.toString();
    }


    private boolean validate(String str) {
        if(str.toLowerCase().equals("true") || str.toLowerCase().equals("false")) {
            return true;
        }

        return false;
    }
}
