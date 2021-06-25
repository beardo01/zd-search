package com.oreid.model;

import com.google.gson.JsonObject;
import com.oreid.domain.EntityType;

import java.util.List;

public abstract class AbstractEntity {

    private final JsonObject data;
    private final EntityType entityType;
    private final List<AbstractEntity> relatedEntities;

    public AbstractEntity(JsonObject data, EntityType entityType, List<AbstractEntity> relatedEntities) {
        this.data = data;
        this.entityType = entityType;
        this.relatedEntities = relatedEntities;
    }

    public JsonObject getData() {
        return data;
    }

    public EntityType getEntityType() {
        return entityType;
    }

    public List<AbstractEntity> getRelatedEntities() {
        return relatedEntities;
    }

    public void addRelatedEntity(AbstractEntity relatedEntity) {
        this.relatedEntities.add(relatedEntity);
    }

    abstract String getRelatedDescriptionKey();

}
