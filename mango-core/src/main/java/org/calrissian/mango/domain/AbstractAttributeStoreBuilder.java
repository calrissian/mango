/*
* Copyright (C) 2017 The Calrissian Authors
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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import static com.google.common.collect.Iterables.addAll;

public abstract class AbstractAttributeStoreBuilder<T extends AttributeStore, B extends AttributeStoreBuilder<T>> implements AttributeStoreBuilder<T> {

    protected Collection<Attribute> attributes = new ArrayList<>();

    @Override
    public B attr(String key, Object val) {
        this.attributes.add(new Attribute<>(key, val));
        return (B)this;
    }

    @Override
    public B attr(String key, Object val, Map<String,String> meta) {
        this.attributes.add(new Attribute<>(key, val, meta));
        return (B)this;
    }

    @Override
    public B attr(Attribute attribute) {
        this.attributes.add(attribute);
        return (B)this;
    }

    @Override
    public B attrs(Iterable<? extends Attribute> attributes) {
        addAll(this.attributes, attributes);
        return (B)this;
    }

    public abstract T build();
}
