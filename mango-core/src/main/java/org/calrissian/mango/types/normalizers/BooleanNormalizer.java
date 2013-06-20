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
