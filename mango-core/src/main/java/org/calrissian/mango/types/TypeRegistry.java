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
package org.calrissian.mango.types;


import org.calrissian.mango.types.exception.TypeDecodingException;
import org.calrissian.mango.types.exception.TypeEncodingException;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import static java.util.Arrays.asList;
import static java.util.Collections.unmodifiableCollection;

public class TypeRegistry<U> {

    private final Map<String, TypeEncoder<?, U>> aliasMapping = new LinkedHashMap<String, TypeEncoder<?, U>>();
    private final Map<Class, TypeEncoder<?, U>> classMapping = new LinkedHashMap<Class, TypeEncoder<?, U>>();

    public TypeRegistry(TypeEncoder<?, U>... normalizers) {
        this(asList(normalizers));
    }

    public TypeRegistry(Iterable<TypeEncoder<?, U>> normalizers) {
        for(TypeEncoder<?, U> resolver: normalizers) {
            if (aliasMapping.containsKey(resolver.getAlias()))
                throw new IllegalArgumentException("The aliases provided by the normalizers must be unique");
            if (classMapping.containsKey(resolver.resolves()))
                throw new IllegalArgumentException("There can only be one normalizer per class type.");

            aliasMapping.put(resolver.getAlias(), resolver);
            classMapping.put(resolver.resolves(), resolver);
        }
    }

    /**
     * Gets a alias for a given java objects class.
     * @param obj
     * @return
     */
    public String getAlias(Object obj) {
        if (obj == null)
            return null;

        TypeEncoder<?, U> encoder = classMapping.get(obj.getClass());
        if(encoder != null)
            return encoder.getAlias();

        return null;
    }

    public U encode(Object value) throws TypeEncodingException {
        TypeEncoder encoder = classMapping.get(value.getClass());
        if(encoder != null)
            return (U)encoder.encode(value);

        throw new TypeEncodingException("An unknown type [" + value.getClass() + "] was encountered");
    }

    public Object decode(String alias, U value) throws TypeDecodingException {

        TypeEncoder<?, U> encoder = aliasMapping.get(alias);
        if(encoder != null)
            return encoder.decode(value);

        throw new TypeDecodingException("An unknown type [" + value + "] was encountered");
    }

    public Collection<TypeEncoder<?, U>> getAllEncoders() {
        return unmodifiableCollection(classMapping.values());
    }

}
