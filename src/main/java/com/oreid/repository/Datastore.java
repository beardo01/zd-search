package com.oreid.repository;

import com.oreid.domain.EntityType;
import com.oreid.model.AbstractEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Datastore {

    private final Map<EntityType, Map<String, Map<String, List<AbstractEntity>>>> data;
    private final List<AbstractEntity> entities;

    public Datastore() {
        this.data = new HashMap<>();
        this.entities = new ArrayList<>();
    }

    public Map<EntityType, Map<String, Map<String, List<AbstractEntity>>>> getData() {
        return data;
    }

    public List<AbstractEntity> getEntities() {
        return entities;
    }

    public void addEntity(AbstractEntity entity) {
        // TODO: Implement add method
    }

    public void search(EntityType entityType, String key, String value) {
        // TODO: Implement search method
    }

}
