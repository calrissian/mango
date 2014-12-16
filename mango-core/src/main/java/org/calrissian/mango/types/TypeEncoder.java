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


import java.io.Serializable;

public interface TypeEncoder<T, U> extends Serializable {

    /**
     * Returns the "alias" of the type so that the encoded form can be decoded.
     */
    public String getAlias();

    /**
     * The java class that the implementations encodes.
     */
    public Class<T> resolves();

    /**
     * Encodes a value.
     */
    U encode(T value);

    /**
     * Decodes the provided value.
     */
    T decode(U value);

}
