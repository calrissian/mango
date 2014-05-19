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
package org.calrissian.mango.types.canonicalizer.domain;

public class CanonicalDef implements Comparable<CanonicalDef> {

    private final String type;
    private final String dataType;

    public CanonicalDef(String type, String dataType) {
        this.type = type;
        this.dataType = dataType;
    }

    public String getType() {
        return type;
    }

    public String getDataType() {
        return dataType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CanonicalDef)) return false;

        CanonicalDef that = (CanonicalDef) o;

        if (dataType != null ? !dataType.equals(that.dataType) : that.dataType != null) return false;
        if (type != null ? !type.equals(that.type) : that.type != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = type != null ? type.hashCode() : 0;
        result = 31 * result + (dataType != null ? dataType.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "CanonicalDef{" +
                "type='" + type + '\'' +
                ", dataType='" + dataType + '\'' +
                '}';
    }

    @Override
    public int compareTo(CanonicalDef canonicalDef) {
        return getType().compareTo(canonicalDef.getType());
    }
}
