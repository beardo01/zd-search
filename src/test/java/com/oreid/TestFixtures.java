package com.oreid;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.oreid.domain.EntityType;
import com.oreid.model.AbstractEntity;
import com.oreid.model.Organization;
import com.oreid.model.Ticket;
import com.oreid.model.User;

public class TestFixtures {

    public static final String JSON_STRING = "{ \"name\": \"Oliver\", \"person\": true, \"countries\": [\"New Zealand\", \"Australia\"]," +
            "\"age\": 24 }";
    public static final String JSON_STRING_2 = "{ \"name\": \"John\", \"person\": false, \"countries\": [\"New Zealand\", \"USA\"]," +
            "\"age\": 24 }";

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
        }
        return null;
    }

}
