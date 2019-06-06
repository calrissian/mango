/*
 * Copyright (C) 2019 The Calrissian Authors
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

import org.calrissian.mango.domain.AbstractAttributeStore;
import org.calrissian.mango.domain.Attribute;

import static java.util.Objects.requireNonNull;

/**
 * Default implementation of {@link Entity}.
 */
public class BaseEntity extends AbstractAttributeStore implements Entity {

    private static final long serialVersionUID = 1L;

    private final EntityIdentifier identifier;

    public BaseEntity(EntityIdentifier identifier, Iterable<? extends Attribute> attributes) {
        super(attributes);
        this.identifier = requireNonNull(identifier);
    }

    /**
     * Copy constructor.
     */
    public BaseEntity(Entity entity) {
        this(entity.getIdentifier(), entity.getAttributes());
    }

    /**
     * {@inheritDoc}
     */
    public String getId() {
        return identifier.getId();
    }

    /**
     * {@inheritDoc}
     */
    public String getType() {
        return identifier.getType();
    }

    @Override
    public EntityIdentifier getIdentifier() {
        return identifier;
    }

    @Override
    public String toString() {
        return "BaseEntity{" +
                "id='" + getId() + '\'' +
                ", type='" + getType() + '\'' +
                ", attributes='" + getAttributes() + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BaseEntity that = (BaseEntity) o;

        return identifier.equals(that.identifier);
    }

    @Override
    public int hashCode() {
        return identifier.hashCode();
    }
}
