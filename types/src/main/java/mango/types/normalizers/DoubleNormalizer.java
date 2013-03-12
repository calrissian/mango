package mango.types.normalizers;


import mango.types.TypeNormalizer;
import mango.types.exception.TypeNormalizationException;

import java.text.DecimalFormat;

public class DoubleNormalizer implements TypeNormalizer<Double> {

    protected static String DECIMAL_FORMAT = "0.0#############################E000";

    @Override
    public String normalize(Double obj) throws TypeNormalizationException {
        try {
            return normalizeDouble(obj);
        }

        catch(Exception e) {
            throw new TypeNormalizationException(e);
        }
    }

    @Override
    public Double denormalize(String str) throws TypeNormalizationException{
        try {
            return denormalizeDouble(str);
        }
        catch (Exception e) {
            throw new TypeNormalizationException(e);
        }
    }

    @Override
    public String getAlias() {
        return "double";
    }

    @Override
    public Class resolves() {
        return Double.class;
    }

    @Override
    public Double fromString(String str) throws TypeNormalizationException {
        return Double.parseDouble(str);
    }

    @Override
    public String asString(Double obj) throws TypeNormalizationException {
        return Double.toString(obj);
    }

    /**
     * Normalized version takes the following form:
     * --000 1.9
     * First byte is the value's sign
     * Second byte is the exponent's sign
     * Third - Fifth bytes are the exponent (or complement of the exponent if negative number)
     * Everything after the space is the value
     */
    private String normalizeDouble(Double value) {

        String valueSign = (value < 0) ? "-" : "0";
        String expSign = "0";
        Integer finalExp = 0;

        DecimalFormat df = new DecimalFormat(DECIMAL_FORMAT);
        String[] splits = df.format(value).split("E");

        // if there's an exponent, complement it
        if(splits.length > 1) {

            String exponent = splits[1];

            if(exponent.startsWith("-")) {

                expSign = "-";
                exponent = exponent.replace("-", "");
                finalExp = 999 - Integer.parseInt(exponent);
            }

            else {
                finalExp = Integer.parseInt(exponent);
            }
        }

        return String.format("%s%s%03d %s", valueSign, expSign, finalExp, splits[0]);
    }

    private Double denormalizeDouble(String value) {

        String exp = value.substring(2, 5);

        char valueSign = value.charAt(0);
        char expSign = value.charAt(1);

        Integer expInt = Integer.parseInt(exp);

        if(expSign == '-') {
            expInt = 999 - expInt;
        }

        String newVal = String.format("%s%sE%s%d", valueSign, value.substring(6), expSign, expInt);
        return Double.parseDouble(newVal);
    }

}
