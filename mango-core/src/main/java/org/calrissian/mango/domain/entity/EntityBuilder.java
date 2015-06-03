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

import org.calrissian.mango.domain.BaseAttributeStoreBuilder;

import static com.google.common.base.Preconditions.checkNotNull;

public class EntityBuilder extends BaseAttributeStoreBuilder<Entity, EntityBuilder> {

    private final EntityIdentifier identifier;

    public static EntityBuilder create(EntityIdentifier identifier) {
        checkNotNull(identifier);
        return new EntityBuilder(identifier);
    }

    public static EntityBuilder create(String type, String id) {
        return create(new EntityIdentifier(type, id));
    }

    public static EntityBuilder create(Entity entity) {
        return create(entity.getIdentifier())
                .attrs(entity.getAttributes());
    }

    protected EntityBuilder(EntityIdentifier identifier) {
        super();
        this.identifier = identifier;
    }

    @Override
    public Entity build() {
        return new BaseEntity(identifier, attributes);
    }
}
