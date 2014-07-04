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
package org.calrissian.mango.domain.entity;

import org.calrissian.mango.domain.BaseTupleStore;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.UUID.randomUUID;

/**
 * Default implementation of {@link Entity}.
 */
public class BaseEntity extends BaseTupleStore implements Entity {

    private String id;
    private String type;

    /**
     * Defines an {@link Entity} object for the given type and a random uuid.
     */
    public BaseEntity(String type) {
        this(type, randomUUID().toString());
    }

    /**
     * Defines an {@link Entity} for the given type and id
     */
    public BaseEntity(String type, String id) {
        checkNotNull(type);
        checkNotNull(id);
        this.id = id;
        this.type = type;
    }

    /**
     * Copy constructor.
     */
    public BaseEntity(Entity entity) {
        this(checkNotNull(entity).getType(), entity.getId());
        putAll(entity.getTuples());
    }

    /**
     * {@inheritDoc}
     */
    public String getId() {
        return id;
    }

    /**
     * {@inheritDoc}
     */
    public String getType() {
        return type;
    }

    @Override
    public String toString() {
        return "BaseEntity{" +
                "id='" + id + '\'' +
                ", type='" + type + '\'' +
                ", tuples='" + getTuples() + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        BaseEntity that = (BaseEntity) o;

        if (!id.equals(that.id)) return false;
        if (!type.equals(that.type)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + id.hashCode();
        result = 31 * result + type.hashCode();
        return result;
    }
}
