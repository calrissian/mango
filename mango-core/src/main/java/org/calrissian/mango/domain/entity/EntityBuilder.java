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
package org.calrissian.mango.domain.entity;

import com.google.common.base.Preconditions;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import org.calrissian.mango.domain.Attribute;

import java.util.Map;

public class EntityBuilder {

    protected String type;
    protected String id;

    protected Multimap<String, Attribute> attributes;

    public EntityBuilder(String type, String id) {

        Preconditions.checkNotNull(type);
        Preconditions.checkNotNull(id);
        this.type = type;
        this.id = id;
        this.attributes = ArrayListMultimap.create();
    }

    public EntityBuilder attr(String key, Object val) {
        attributes.put(key, new Attribute(key, val));
        return this;
    }

    public EntityBuilder attr(String key, Object val, Map<String,String> meta) {
        attributes.put(key, new Attribute(key, val, meta));
        return this;
    }

    public EntityBuilder attr(Attribute attribute) {
        attributes.put(attribute.getKey(), attribute);
        return this;
    }

    public Entity build() {
        return new BaseEntity(type, id, attributes);
    }
}
