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

public interface TypeNormalizer<T> {

    /**
     * Normalizes a types into a form that can be sorted lexicographically & scanned in a range
     * @param obj
     * @return
     */
    public String normalize(T obj) throws TypeNormalizationException;

    /**
     * Denormalizes a normalized form back into a native java form.
     * @param str
     * @return
     */
    public T denormalize(String str) throws TypeNormalizationException;

    /**
     * Returns the "alias" of the types so that the normalized form can be denormalized
     * @return
     */
    public String getAlias();

    /**
     * The java class that the normalized form denormalizes to
     * @return
     */
    public Class resolves();


    /**
     * Creates the java type from a user-readable string
     * @param str
     * @return
     * @throws TypeNormalizationException
     */
    T fromString(String str) throws TypeNormalizationException;

    /**
     * Serializes the java type into a user-readable string
     * (this should be parseable by the fromString() method)
     * @param obj
     * @return
     * @throws TypeNormalizationException
     */
    String asString(T obj) throws TypeNormalizationException;
}
