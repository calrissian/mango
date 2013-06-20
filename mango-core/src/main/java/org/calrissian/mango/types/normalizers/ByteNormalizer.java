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
