package com.oreid.model;

import com.google.gson.JsonObject;
import com.oreid.domain.EntityType;

import java.util.ArrayList;

public class Ticket extends AbstractEntity {

    private static final String RELATED_DESCRIPTION_KEY = "subject";

    public Ticket(JsonObject data) {
        super(data, EntityType.TICKET, new ArrayList<>());
    }

    @Override
    public String getRelatedDescriptionKey() {
        return RELATED_DESCRIPTION_KEY;
    }

}
