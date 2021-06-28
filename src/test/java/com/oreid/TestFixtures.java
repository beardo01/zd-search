package com.oreid;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.oreid.domain.EntityType;
import com.oreid.model.*;

import java.util.Arrays;
import java.util.List;

public class TestFixtures {

    public static final String JSON_STRING = "{ \"name\": \"Oliver\", \"person\": true, \"countries\": [\"New Zealand\", \"Australia\"]," +
            "\"age\": 24 }";
    public static final String JSON_STRING_2 = "{ \"name\": \"John\", \"person\": false, \"countries\": [\"New Zealand\", \"USA\"]," +
            "\"age\": 24 }";
    public static final List<String> ENTITY_KEYS = List.of("name", "person", "countries", "age");
    public static final List<String> ENTITY_1_VALUES = List.of("Oliver", "true", "[\"New Zealand\",\"Australia\"]", "24");
    public static final List<String> ENTITY_2_VALUES = List.of("John", "false", "[\"New Zealand\",\"USA\"]", "24");
    public static final String SEARCH_TYPE_NAME = "SEARCH_TYPE_NAME";


    public static JsonObject createJsonObject() {
        return createJsonObject(JSON_STRING);
    }

    public static JsonObject createJsonObject(String jsonString) {
        return new Gson().fromJson(jsonString, JsonObject.class);
    }

    public static AbstractEntity createAbstractEntity(EntityType entityType, String jsonString) {
        JsonObject jsonObject = createJsonObject(jsonString);
        switch (entityType) {
            case ORGANIZATION:
                return new Organization(jsonObject);
            case TICKET:
                return new Ticket(jsonObject);
            case USER:
                return new User(jsonObject);
            default:
                throw new IllegalStateException("Unexpected value: " + entityType);
        }
    }

    public static SearchType createSearchType(EntityType entityType) {
        return new SearchType(SEARCH_TYPE_NAME, createAbstractEntity(entityType, JSON_STRING));
    }

    public static int sumStringLengths(String... strings) {
        return Arrays.stream(strings)
                .map(String::length)
                .reduce(0, Integer::sum);
    }

}
