package com.oreid.repository;

import com.google.gson.JsonElement;
import com.oreid.domain.EntityType;
import com.oreid.model.AbstractEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Datastore implements EntityDatastore {

    private final Map<EntityType, Map<String, Map<String, List<AbstractEntity>>>> data;

    public Datastore() {
        this.data = new HashMap<>();
    }

    public Map<EntityType, Map<String, Map<String, List<AbstractEntity>>>> getData() {
        return data;
    }

    public void addEntity(AbstractEntity entity) {
        // Get or create new field name map
        Map<String, Map<String, List<AbstractEntity>>> fieldNameMap = data.getOrDefault(entity.getEntityType(), new HashMap<>());

        // Add each field name and value to map - format {fieldName: {fieldValue: [entity]}}
        entity.getData().entrySet().forEach(entry -> {
            String fieldName = entry.getKey();
            JsonElement element = entry.getValue();

            // Get or create new field value map
            Map<String, List<AbstractEntity>> fieldValueMap = fieldNameMap.getOrDefault(fieldName, new HashMap<>());

            // Add each field value and entity to map - format {fieldValue: [entity]}
            if(element.isJsonArray()) {
                element.getAsJsonArray().forEach(e -> addListEntity(fieldValueMap, e.getAsString(), entity));
            } else {
                addListEntity(fieldValueMap, element.getAsString(), entity);
            }

            fieldNameMap.put(fieldName, fieldValueMap);
        });

        data.put(entity.getEntityType(), fieldNameMap);
    }

    public List<AbstractEntity> search(EntityType entityType, String key, String value) {
        return data.getOrDefault(entityType, new HashMap<>())
                .getOrDefault(key, new HashMap<>())
                .getOrDefault(value, new ArrayList<>());
    }

    private void addListEntity(Map<String, List<AbstractEntity>> fieldValueMap, String fieldValue, AbstractEntity entity) {
        // Get or create new field value map
        List<AbstractEntity> entities = fieldValueMap.getOrDefault(fieldValue, new ArrayList<>());

        // Add new entity
        entities.add(entity);
        fieldValueMap.put(fieldValue, entities);
    }

}
