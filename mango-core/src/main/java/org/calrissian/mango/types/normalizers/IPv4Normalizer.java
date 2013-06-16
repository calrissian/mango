package org.calrissian.mango.types.normalizers;

import org.calrissian.mango.types.TypeNormalizer;
import org.calrissian.mango.types.exception.TypeNormalizationException;
import org.calrissian.mango.types.types.IPv4;


public class IPv4Normalizer implements TypeNormalizer<IPv4> {

    @Override
    public String normalize(IPv4 obj) throws TypeNormalizationException {
        try {
            return String.format("%010d", obj.getValue());
        }
        catch (Exception e) {
            throw new TypeNormalizationException(e);
        }
    }

    @Override
    public IPv4 denormalize(String str) throws TypeNormalizationException {
        try {
            return new IPv4(Long.parseLong(str));
        }
        catch (Exception e) {
            throw new TypeNormalizationException(e);
        }
    }

    @Override
    public String getAlias() {
        return "ipv4";
    }

    @Override
    public Class resolves() {
        return IPv4.class;
    }

    @Override
    public IPv4 fromString(String str) throws TypeNormalizationException {
        try {
            return new IPv4(str);
        }
        catch (Exception e) {
            throw new TypeNormalizationException(e);
        }
    }

    @Override
    public String asString(IPv4 obj) throws TypeNormalizationException {

        try {
            return obj.toString();
        }
        catch (Exception e) {
            throw new TypeNormalizationException(e);
        }
    }
}
