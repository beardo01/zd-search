package com.oreid.model;

import java.util.ArrayList;
import java.util.List;

public class SearchType {

    private final String name;
    private final List<String> fields;

    public SearchType(String name, AbstractEntity entity) {
        this.name = name;
        this.fields = new ArrayList<>(entity.getData().keySet());
    }

    public String getName() {
        return name;
    }

    public List<String> getFields() {
        return fields;
    }

    public boolean isValidField(String field) {
        return fields.contains(field);
    }

}
