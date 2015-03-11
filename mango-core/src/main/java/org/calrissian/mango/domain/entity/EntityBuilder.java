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
import org.calrissian.mango.domain.BaseAttributeStoreBuilder;

public class EntityBuilder extends BaseAttributeStoreBuilder<Entity, EntityBuilder> {

    protected String type;
    protected String id;

    public static final EntityBuilder create(String type, String id) {
        return new EntityBuilder(type, id);
    }

    protected EntityBuilder(String type, String id) {

        super();
        Preconditions.checkNotNull(type);
        Preconditions.checkNotNull(id);
        this.type = type;
        this.id = id;
    }

    public Entity build() {
        return new BaseEntity(type, id, attributes);
    }
}
