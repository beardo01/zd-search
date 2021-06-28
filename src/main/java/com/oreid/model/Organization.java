package com.oreid.model;

import com.google.gson.JsonObject;
import com.oreid.domain.EntityType;

import java.util.ArrayList;

public class Organization extends AbstractEntity {

    private static final String RELATED_DESCRIPTION_KEY = "name";

    public Organization(JsonObject data) {
        super(data, EntityType.ORGANIZATION, new ArrayList<>());
    }

    @Override
    public String getRelatedDescriptionKey() {
        return RELATED_DESCRIPTION_KEY;
    }

}
