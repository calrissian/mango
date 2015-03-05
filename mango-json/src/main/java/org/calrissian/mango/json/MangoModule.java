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
package org.calrissian.mango.json;

import com.fasterxml.jackson.databind.module.SimpleModule;
import org.calrissian.mango.criteria.domain.Node;
import org.calrissian.mango.domain.Attribute;
import org.calrissian.mango.domain.entity.BaseEntity;
import org.calrissian.mango.domain.entity.Entity;
import org.calrissian.mango.domain.event.BaseEvent;
import org.calrissian.mango.domain.event.Event;
import org.calrissian.mango.json.deser.EntityDeserializer;
import org.calrissian.mango.json.deser.EventDeserializer;
import org.calrissian.mango.json.deser.NodeDeserializer;
import org.calrissian.mango.json.deser.AttributeDeserializer;
import org.calrissian.mango.json.ser.EntitySerializer;
import org.calrissian.mango.json.ser.EventSerializer;
import org.calrissian.mango.json.ser.NodeSerializer;
import org.calrissian.mango.json.ser.AttributeSerializer;
import org.calrissian.mango.types.TypeRegistry;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.calrissian.mango.types.SimpleTypeEncoders.SIMPLE_TYPES;


public class MangoModule extends SimpleModule {

    private final TypeRegistry<String> typeRegistry;

    public MangoModule() {
        this(SIMPLE_TYPES);
    }

    public MangoModule(TypeRegistry<String> typeRegistry) {
        super("MangoModule");
        this.typeRegistry = checkNotNull(typeRegistry);
    }

    @Override
    public void setupModule(SetupContext context) {

        addSerializer(Attribute.class, new AttributeSerializer(typeRegistry));
        addDeserializer(Attribute.class, new AttributeDeserializer(typeRegistry));

        //Register the class and a default serializer/deserializer for the interface.
        addSerializer(Entity.class, new EntitySerializer());
        addSerializer(BaseEntity.class, new EntitySerializer());
        addDeserializer(Entity.class, new EntityDeserializer());
        addDeserializer(BaseEntity.class, new EntityDeserializer());
        addSerializer(Event.class, new EventSerializer());
        addSerializer(BaseEvent.class, new EventSerializer());
        addDeserializer(Event.class, new EventDeserializer());
        addDeserializer(BaseEvent.class, new EventDeserializer());

        addSerializer(Node.class, new NodeSerializer(typeRegistry));
        addDeserializer(Node.class, new NodeDeserializer(typeRegistry));

        super.setupModule(context);
    }
}
