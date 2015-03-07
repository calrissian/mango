/*
* Copyright (C) 2015 The Calrissian Authors
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
package org.calrissian.mango.domain;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

import java.util.Map;

public abstract class BaseAttributeStoreBuilder<T, B extends BaseAttributeStoreBuilder> implements AttributeStoreBuilder<T> {

    protected Multimap<String, Attribute> attributes = ArrayListMultimap.create();

    public B attr(String key, Object val) {
        attributes.put(key, new Attribute(key, val));
        return (B)this;
    }

    public B attr(String key, Object val, Map<String,String> meta) {
        attributes.put(key, new Attribute(key, val, meta));
        return (B)this;
    }

    public B attr(Attribute attribute) {
        attributes.put(attribute.getKey(), attribute);
        return (B)this;
    }

    public abstract T build();
}
