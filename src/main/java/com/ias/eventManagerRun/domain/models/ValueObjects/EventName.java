package com.ias.eventManagerRun.domain.models.ValueObjects;

import com.fasterxml.jackson.annotation.JsonValue;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.util.Objects;

@Embeddable
public class EventName {

    @Column(name = "name", nullable = false)
    private String name;

    public EventName() {
    }

    public EventName(String name) {
        if(!name.matches("^[A-Za-z0-9 ]{2,}")) throw new IllegalArgumentException("Event name invalid");
        this.name = name;
    }

    @JsonValue
    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EventName eventName = (EventName) o;
        return Objects.equals(name, eventName.name);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name);
    }

    @Override
    public String toString() {
        return "EventName{" +
                "name='" + name + '\'' +
                '}';
    }
}
