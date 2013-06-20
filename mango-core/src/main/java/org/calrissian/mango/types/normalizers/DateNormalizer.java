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
