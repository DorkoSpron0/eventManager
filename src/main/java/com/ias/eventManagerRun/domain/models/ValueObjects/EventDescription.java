package com.ias.eventManagerRun.domain.models.ValueObjects;

import com.fasterxml.jackson.annotation.JsonValue;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.util.Objects;

@Embeddable
public class EventDescription {

    @Column(name = "description", nullable = false)
    private String description;

    public EventDescription() {
    }

    public EventDescription(String description) {
        if(!description.matches("^[a-zA-Z0-9 ]{2,}")) throw new IllegalArgumentException("Description is not valid");
        this.description = description;
    }

    @JsonValue
    public String getDescription() {
        return description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EventDescription that = (EventDescription) o;
        return Objects.equals(description, that.description);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(description);
    }

    @Override
    public String toString() {
        return "EventDescription{" +
                "description='" + description + '\'' +
                '}';
    }
}
