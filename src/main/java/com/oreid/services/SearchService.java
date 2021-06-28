package com.oreid.services;

import com.google.gson.JsonElement;
import com.oreid.domain.EntityType;
import com.oreid.model.AbstractEntity;
import com.oreid.model.SearchType;
import com.oreid.repository.EntityDatastore;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.oreid.domain.SearchConstants.*;

public class SearchService {

    private final EntityDatastore datastore;
    private final Map<EntityType, SearchType> searchTypes;

    public SearchService(EntityDatastore datastore) {
        this.datastore = datastore;
        this.searchTypes = new HashMap<>();
    }

    public Map<EntityType, SearchType> getSearchTypes() {
        return searchTypes;
    }

    public void addSearchType(EntityType entityType, SearchType searchType) {
        this.searchTypes.put(entityType, searchType);
    }

    public boolean isValidField(EntityType entityType, String fieldName) {
        return searchTypes.containsKey(entityType) && searchTypes.get(entityType).isValidField(fieldName);
    }

    public void addEntity(AbstractEntity entity) {
        this.datastore.addEntity(entity);
    }

    public void printSearchResults(EntityType entityType, String key, String value) {
        List<List<String[]>> matchDescriptions = this.datastore.search(entityType, key, value).stream()
                .map(this::getDescription)
                .collect(Collectors.toList());

        matchDescriptions.forEach(description -> {
            System.out.print(PRINT_SEPARATOR);
            description.forEach(matchLine -> System.out.format(ENTITY_DESCRIPTION_FORMAT, matchLine));
        });
    }

    public void printSearchTypes() {
        this.searchTypes.values().forEach(searchType -> {
            System.out.print(PRINT_SEPARATOR);
            searchType.getFields().forEach(System.out::println);
        });
    }

    private List<String[]> getDescription(AbstractEntity entity) {
        if(entity == null) {
            return null;
        }

        // Add entity details
        List<String[]> rows = entity.getData().entrySet().stream()
                .map(e -> new String[] {e.getKey(), getJsonElementString(e.getValue())})
                .collect(Collectors.toList());

        // Add related entity details
        IntStream.range(0, entity.getRelatedEntities().size()).forEach(i -> {
            AbstractEntity relatedEntity = entity.getRelatedEntities().get(i);

            rows.add(new String[]{
                    String.format(RELATED_ENTITY_DESCRIPTION_FORMAT, entity.getEntityType().toString(), i + 1),
                    getRelatedDescription(relatedEntity)
            });
        });

        return rows;
    }

    private String getRelatedDescription(AbstractEntity entity) {
        String relatedDescriptionKey = entity.getRelatedDescriptionKey();

        if(StringUtils.isEmpty(relatedDescriptionKey) || entity.getData() == null || !entity.getData().has(relatedDescriptionKey)) {
            return null;
        }
        return getJsonElementString(entity.getData().get(relatedDescriptionKey));
    }

    private String getJsonElementString(JsonElement jsonElement) {
        return jsonElement.isJsonPrimitive() ?
                jsonElement.getAsString() :
                jsonElement.toString();
    }

}
