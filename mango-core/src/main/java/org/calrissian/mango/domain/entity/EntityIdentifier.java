/*
 * Copyright (C) 2016 The Calrissian Authors
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

import org.calrissian.mango.domain.Identifiable;

import java.io.Serializable;

import static com.google.common.base.Preconditions.checkNotNull;

public class EntityIdentifier implements Identifiable, Serializable {

    private static final long serialVersionUID = 1L;

    private final String type;
    private final String id;

    public EntityIdentifier(String type, String id) {
        this.type = checkNotNull(type);
        this.id = checkNotNull(id);
    }

    public String getType() {
        return type;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String toString() {
        return "EntityIndex{" +
                "type='" + type + '\'' +
                ", id='" + id + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EntityIdentifier that = (EntityIdentifier) o;

        if (!type.equals(that.type)) return false;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        int result = type.hashCode();
        result = 31 * result + id.hashCode();
        return result;
    }
}
