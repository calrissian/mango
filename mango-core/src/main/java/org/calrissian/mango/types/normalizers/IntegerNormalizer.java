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

public class IntegerNormalizer implements TypeNormalizer<Integer> {


    @Override
    public String normalize(Integer obj) throws TypeNormalizationException {
        try {
            String signPadding = obj < 0 ? "" : "0";
            return String.format("%s%010d", signPadding, obj);
        }
        catch (Exception e) {
            throw new TypeNormalizationException(e);
        }
    }

    @Override
    public Integer denormalize(String str) throws TypeNormalizationException {
        try {
            return Integer.parseInt(str);
        }
        catch (Exception e) {
            throw new TypeNormalizationException(e);
        }
    }

    @Override
    public String getAlias() {
        return "integer";
    }

    @Override
    public Class resolves() {
        return Integer.class;
    }

    @Override
    public Integer fromString(String str) throws TypeNormalizationException {
        return denormalize(str);
    }

    @Override
    public String asString(Integer obj) {
        return obj.toString();  //To change body of implemented methods use File | Settings | File Templates.
    }
}
