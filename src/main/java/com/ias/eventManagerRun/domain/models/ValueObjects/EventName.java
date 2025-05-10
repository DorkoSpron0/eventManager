package com.ias.eventManagerRun.domain.models.ValueObjects;

import com.fasterxml.jackson.annotation.JsonValue;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.util.Objects;

public record EventName(String value) {

    public EventName {
        if(value == null || value.isEmpty() || !value.matches("^[A-Za-z0-9 ]{2,}")) {
            throw new IllegalArgumentException("Event name invalid");
        }
    }
}
