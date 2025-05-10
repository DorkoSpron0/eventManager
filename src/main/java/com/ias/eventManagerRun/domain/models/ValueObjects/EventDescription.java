package com.ias.eventManagerRun.domain.models.ValueObjects;

public record EventDescription(String value) {

    public EventDescription {
        if(value == null || value.isEmpty() || !value.matches("^[a-zA-Z0-9 ]{2,}")) {
            throw new IllegalArgumentException("Description is not valid");
        }
    }
}
