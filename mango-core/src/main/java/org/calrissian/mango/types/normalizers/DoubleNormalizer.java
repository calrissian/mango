/*
 * Copyright (C) 2013 The Calrissian Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.calrissian.mango.types.normalizers;


import org.calrissian.mango.types.TypeNormalizer;
import org.calrissian.mango.types.exception.TypeNormalizationException;

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
    private String normalizeDouble(Double data) {

        String valueSign = (data < 0) ? "-" : "0";
        String expSign = "0";
        Integer finalExp = 0;

        DecimalFormat df = new DecimalFormat(DECIMAL_FORMAT);
        String[] splits = df.format(data).split("E");
        //if there's an exponent, complement it
        if (splits.length > 1) {
            String exponent = splits[1];
            if (exponent.startsWith("-")) {
                expSign = "-";
                exponent = exponent.replace("-", "");
            }

            if(valueSign.equals("-")) {

                finalExp = 999 - Integer.parseInt(exponent);
            } else {
                finalExp = Integer.parseInt(exponent);
            }
        }

        if(splits[0].startsWith("-")) {
            splits[0] = splits[0].substring(1);
        }

        return String.format("%s%s%03d %s", valueSign, expSign, finalExp, splits[0]);

    }

    private Double denormalizeDouble(String value) {

        String exp = value.substring(2, 5);

        char valueSign = value.charAt(0);
        char expSign = value.charAt(1);

        Integer expInt = Integer.parseInt(exp);

        if(valueSign == '-') {
            expInt = 999 - expInt;
        }

        String newVal = String.format("%s%sE%s%d", valueSign, value.substring(6), expSign, expInt);
        return Double.parseDouble(newVal);
    }

}
