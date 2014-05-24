package org.calrissian.mango.domain.entity;


import org.calrissian.mango.domain.TypedIdentifiable;

public class EntityIndex implements TypedIdentifiable {

    private String type;
    private String id;

    public EntityIndex(String type, String id) {
        this.type = type;
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public String getId() {
        return id;
    }
}
