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
package org.calrissian.mango.criteria.domain.criteria;


import org.calrissian.mango.domain.Attribute;
import org.calrissian.mango.domain.AttributeStore;

import java.util.Collection;

public class HasCriteria extends KeyValueLeafCriteria {

    private final Class clazz;

    public HasCriteria(String key, Class clazz, ParentCriteria parentCriteria) {
        super(key, null, parentCriteria);
        this.clazz = clazz;
    }

    public HasCriteria(String key, ParentCriteria parentCriteria) {
        this(key, null, parentCriteria);
    }

    @Override
    public boolean apply(AttributeStore obj) {
        if(obj.get(key) == null)
            return false;

        Collection<? extends Attribute> keyValues = obj.getAll(key);
        if(keyValues.size() > 0 && clazz == null)
            return true;

        for(Attribute keyValue : keyValues) {
            if(keyValue.getValue().getClass().equals(clazz))
                return true;
        }

        return false;
    }

    @Override
    public Criteria clone(ParentCriteria parentCriteria) {
        return new HasCriteria(key, clazz, parentCriteria);
    }

}
