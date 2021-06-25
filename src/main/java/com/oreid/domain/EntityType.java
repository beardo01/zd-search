package com.oreid.domain;

import org.apache.commons.lang3.StringUtils;

public enum EntityType {
    ORGANIZATION,
    TICKET,
    USER;

    @Override
    public String toString() {
        return StringUtils.toRootLowerCase(this.name());
    }
}
