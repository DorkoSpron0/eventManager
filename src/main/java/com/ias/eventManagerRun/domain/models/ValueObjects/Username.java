package com.ias.eventManagerRun.domain.models.ValueObjects;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public record Username(@JsonCreator String value) {
    public Username {
        if (value == null || value.isBlank() || !value.matches("^[A-Za-z ]{2,}$")) {
            throw new IllegalArgumentException("Username not valid");
        }
    }
}
