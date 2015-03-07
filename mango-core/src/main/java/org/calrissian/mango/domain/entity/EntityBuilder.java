package org.calrissian.mango.domain.entity;

import com.google.common.base.Preconditions;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import org.calrissian.mango.domain.Attribute;

import java.util.Map;

public class EntityBuilder {

    protected String type;
    protected String id;

    protected Multimap<String, Attribute> attributes;

    public EntityBuilder(String type, String id) {

        Preconditions.checkNotNull(type);
        Preconditions.checkNotNull(id);
        this.type = type;
        this.id = id;
        this.attributes = ArrayListMultimap.create();
    }

    public EntityBuilder attr(String key, Object val) {
        attributes.put(key, new Attribute(key, val));
        return this;
    }

    public EntityBuilder attr(String key, Object val, Map<String,String> meta) {
        attributes.put(key, new Attribute(key, val, meta));
        return this;
    }

    public EntityBuilder attr(Attribute attribute) {
        attributes.put(attribute.getKey(), attribute);
        return this;
    }

    public Entity build() {
        return new BaseEntity(type, id, attributes);
    }
}
