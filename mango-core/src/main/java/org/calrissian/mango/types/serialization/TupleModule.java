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
package org.calrissian.mango.types.serialization;


import org.calrissian.mango.domain.Tuple;
import org.calrissian.mango.types.TypeContext;
import org.codehaus.jackson.Version;
import org.codehaus.jackson.map.module.SimpleModule;

import static org.calrissian.mango.types.TypeContext.DEFAULT_TYPES;

public class TupleModule extends SimpleModule{

    private final TypeContext typeContext;

    public TupleModule() {
        this(DEFAULT_TYPES);
    }

    public TupleModule(TypeContext typeContext) {
        super("TupleModule", new Version(0,0,1,null));
        this.typeContext = typeContext;
    }

    @Override
    public void setupModule(SetupContext context) {
        addSerializer(Tuple.class, new TupleSerializer(typeContext));
        addDeserializer(Tuple.class, new TupleDeserializer(typeContext));
        super.setupModule(context);
    }
}
