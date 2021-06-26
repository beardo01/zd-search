package com.oreid.repository;

import com.oreid.domain.EntityType;
import com.oreid.model.AbstractEntity;

import java.util.List;

public interface EntityDatastore {

    /**
     * Adds an entity to datastore.
     * @param entity Entity to store.
     */
    void addEntity(AbstractEntity entity);

    /**
     * Retrieves list of entities matching search from datastore.
     * @param entityType Type of entity.
     * @param key Key to search on.
     * @param value Value to search for.
     * @return List of matching entities. Empty list if no matching entities.
     */
    List<AbstractEntity> search(EntityType entityType, String key, String value);

}
