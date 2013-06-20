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
package org.calrissian.mango.serialization;

import org.codehaus.jackson.map.ObjectMapper;

public class ObjectMapperContext {

    protected static ObjectMapperContext instance = new ObjectMapperContext();

    public static ObjectMapperContext getInstance() {
        return instance;
    }

    protected ObjectMapper objectMapper;

    public ObjectMapperContext() {
        objectMapper = new ObjectMapper();
    }

    public ObjectMapper getObjectMapper() {

        return objectMapper;
    }
}
