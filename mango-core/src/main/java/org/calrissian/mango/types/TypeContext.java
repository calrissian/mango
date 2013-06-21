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


import org.calrissian.mango.types.exception.TypeNormalizationException;
import org.calrissian.mango.types.normalizers.*;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import static java.util.Arrays.asList;
import static java.util.Collections.unmodifiableCollection;

public class TypeContext {

    public static final TypeContext DEFAULT_TYPES =
            new TypeContext(
                    new BooleanNormalizer(), new DateNormalizer(),
                    new DoubleNormalizer(), new IntegerNormalizer(),
                    new LongNormalizer(), new IPv4Normalizer(),
                    new StringNormalizer(),new URINormalizer()
            );


    private final Map<String, TypeNormalizer> typeToNormalizer = new LinkedHashMap<String, TypeNormalizer>();
    private final Map<Class, TypeNormalizer> classToNormalizer = new LinkedHashMap<Class, TypeNormalizer>();

    public TypeContext(TypeNormalizer... normalizers) {
        this(asList(normalizers));
    }

    public TypeContext(Iterable<TypeNormalizer> normalizers) {
        for(TypeNormalizer resolver: normalizers) {
            typeToNormalizer.put(resolver.getAlias(), resolver);
            classToNormalizer.put(resolver.resolves(), resolver);
        }
    }

    /**
     * Gets a normalization alias for a given java types.
     * @param obj
     * @return
     */
    public String getAliasForType(Object obj) {
        TypeNormalizer typeNormalizer = classToNormalizer.get(obj.getClass());
        if(typeNormalizer != null)
            return typeNormalizer.getAlias();

        return null;
    }

    public Object fromString(String str, String objType) throws TypeNormalizationException {

        TypeNormalizer typeNormalizer = typeToNormalizer.get(objType);
        if(typeNormalizer != null)
            return typeNormalizer.fromString(str);

        throw new TypeNormalizationException("An unknown type [" + objType + "] was encountered");
    }

    public String asString(Object obj) throws TypeNormalizationException {
        TypeNormalizer typeNormalizer = classToNormalizer.get(obj.getClass());
        if(typeNormalizer != null)
            return typeNormalizer.asString(obj);

        throw new TypeNormalizationException("An unknown type [" + obj.getClass() + "] was encountered");
    }

    /**
     * Normalizes an object into its lexicographically sorted form
     * @param obj
     * @return
     */
    public String normalize(Object obj) throws TypeNormalizationException {
        TypeNormalizer typeNormalizer = classToNormalizer.get(obj.getClass());
        if(typeNormalizer != null)
            return typeNormalizer.normalize(obj);

        throw new TypeNormalizationException("An unknown type [" + obj.getClass() + "] was encountered");
    }

    /**
     * Denormalizes a normalized form into a java form.
     * @param str
     * @param objType
     * @return
     */
    public Object denormalize(String str, String objType) throws TypeNormalizationException {
        TypeNormalizer typeNormalizer = typeToNormalizer.get(objType);
        if(typeNormalizer != null)
            return typeNormalizer.denormalize(str);

        throw new TypeNormalizationException("An unknown type [" + objType + "] was encountered");
    }

    public Collection<TypeNormalizer> getAllNormalizers() {
        return unmodifiableCollection(classToNormalizer.values());
    }

}
