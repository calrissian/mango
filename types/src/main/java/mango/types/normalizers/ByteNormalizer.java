package mango.types.normalizers;

import mango.types.TypeNormalizer;
import mango.types.exception.TypeNormalizationException;

public class ByteNormalizer implements TypeNormalizer<Byte> {

    private int padding = 8;

    @Override
    public String normalize(Byte obj) throws TypeNormalizationException {
        try {
            return padBits(Byte.toString(obj));
        }

        catch(Exception e) {
            throw new TypeNormalizationException(e);
        }
    }

    @Override
    public Byte denormalize(String str) throws TypeNormalizationException {

        try {
            return Byte.parseByte(str);
        }

        catch(Exception e) {
            throw new TypeNormalizationException(e);
        }
    }

    @Override
    public String getAlias() {
        return "byte";
    }

    @Override
    public Class resolves() {
        return Byte.class;
    }

    @Override
    public Byte fromString(String str) throws TypeNormalizationException{

        try {
            return Byte.parseByte(str);
        }

        catch(Exception e) {
            throw new TypeNormalizationException(e);
        }
    }

    @Override
    public String asString(Byte obj) throws TypeNormalizationException {
        try {
            return obj.toString();
        }
        catch (Exception e) {
            throw new TypeNormalizationException(e);
        }
    }

    private String padBits(String value) {

        int padAmt = padding - value.length();

        String finalPad = "";
        for(int i = 0; i < padAmt; i++) {
            finalPad += "0";
        }

        return finalPad + value;
    }
}
