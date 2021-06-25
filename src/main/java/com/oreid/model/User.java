package com.oreid.model;

import com.google.gson.JsonObject;
import com.oreid.domain.EntityType;

import java.util.ArrayList;

public class User extends AbstractEntity {

    private static final String RELATED_DESCRIPTION_KEY = "name";

    public User(JsonObject data) {
        super(data, EntityType.USER, new ArrayList<>());
    }

    @Override
    String getRelatedDescriptionKey() {
        return RELATED_DESCRIPTION_KEY;
    }

}
