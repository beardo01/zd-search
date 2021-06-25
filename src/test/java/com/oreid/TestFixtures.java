package com.oreid;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.List;

public class TestFixtures {

    public static final String JSON_STRING = "{ \"name\": \"Oliver\", \"person\": true, \"countries\": [\"New Zealand\", \"Australia\"]," +
            "\"age\": 24 }";
    public static final List<String> JSON_KEYS = List.of("name", "person", "countries", "age");
    public static final List<Object> JSON_VALUES = List.of("Oliver", "true", "[\"New Zealand\", \"Australia\"]", "24");

    public static JsonObject createJsonObject() {
        return new Gson().fromJson(JSON_STRING, JsonObject.class);
    }

}
