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
package org.calrissian.mango.types;


import org.calrissian.mango.types.exception.TypeDecodingException;
import org.calrissian.mango.types.exception.TypeEncodingException;

import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.Iterables.concat;
import static java.util.Arrays.asList;
import static java.util.Collections.unmodifiableCollection;

/**
 * The TypeRegistry is used as a means to register and easily utilize various encoding schemes to provide a simple
 * mechanism to encode and decode from one Object type to another.  The core the the TypeRegistry are the encoders
 * which are loaded during initialization, and can be used to fully customize how data is encoded and decoded.
 * </p>
 * One of the core concepts with the TypeRegistry is the use of an alias. The use of an alias is used as a simple
 * mechanism to identify which {@link TypeEncoder} should be used for decoding a value. Any value for an alias can be
 * used on a {@link TypeEncoder}, with the the limitation that all {@link TypeEncoder}s added to the TypeRegistry must
 * be unique.
 */
public class TypeRegistry<U> implements Serializable {

    private final Map<String, TypeEncoder<?, U>> aliasMapping = new LinkedHashMap<>();
    private final Map<Class<?>, TypeEncoder<?, U>> classMapping = new LinkedHashMap<>();

    public TypeRegistry(TypeEncoder<?, U>... normalizers) {
        this(asList(normalizers));
    }

    public TypeRegistry(TypeRegistry<U> registry, TypeEncoder<?, U>... normalizers) {
        this(concat(asList(normalizers), registry.getAllEncoders()));
    }

    public TypeRegistry(Iterable<TypeEncoder<?, U>> normalizers) {
        checkNotNull(normalizers);

        for (TypeEncoder<?, U> resolver : normalizers) {
            if (aliasMapping.containsKey(resolver.getAlias()))
                throw new IllegalArgumentException("The aliases provided by the TypeEncoder must be unique");
            if (classMapping.containsKey(resolver.resolves()))
                throw new IllegalArgumentException("There can only be one TypeEncoder per class type.");

            aliasMapping.put(resolver.getAlias(), resolver);
            classMapping.put(resolver.resolves(), resolver);
        }
    }

    /**
     * Gets registered alias for a provided object type.  This is used to identify which {@link TypeEncoder} to use for decoding.
     */
    public String getAlias(Object obj) {
        if (obj == null)
            return null;

        return getClassAlias(obj.getClass());
    }

    /**
     * Retrieves the registered alias for the provided {@link Class}.
     */
    public String getClassAlias(Class clazz) {
        if (clazz == null)
            return null;

        TypeEncoder<?, U> encoder = classMapping.get(clazz);
        if (encoder != null)
            return encoder.getAlias();

        return null;
    }

    /**
     * Encodes the given {@code value} using one of the registered {@link TypeEncoder}s.
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public U encode(Object value) {
        checkNotNull(value, "Value for encoding can not be null");

        TypeEncoder encoder = classMapping.get(value.getClass());
        if (encoder != null)
            return (U) encoder.encode(value);

        throw new TypeEncodingException("An unknown type [" + value.getClass() + "] was encountered");
    }

    /**
     * Decodes the given {@code value} using one of the registered {@link TypeEncoder}s.  The {@code alias} is used
     * to determine which {@link TypeEncoder} will be used and shall correlate to the result of the {@code getAlias()}
     * method for the original object.
     */
    public Object decode(String alias, U value) {
        checkNotNull(alias, "Not allowed to have a null alias");
        checkNotNull(value, "Value for decoding can not be null");

        TypeEncoder<?, U> encoder = aliasMapping.get(alias);
        if (encoder != null)
            return encoder.decode(value);

        throw new TypeDecodingException("An unknown alias [" + alias + "] was encountered");
    }

    /**
     * Returns all the registered {@link TypeEncoder}s in the registry
     */
    public Collection<TypeEncoder<?, U>> getAllEncoders() {
        return unmodifiableCollection(classMapping.values());
    }

}
