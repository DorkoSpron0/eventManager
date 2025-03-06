package com.ias.eventManagerRun.domain.models.ValueObjects;

import com.fasterxml.jackson.annotation.JsonValue;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.util.Objects;

@Embeddable
public class Username {

    @Column(name = "username", unique = true, nullable = false)
    private String username;

    protected Username() {}

    public Username(String username) {
        if(username.isBlank() || username.isEmpty() || !username.matches("^[A-Za-z ]{2,}")) throw new IllegalArgumentException("Username isn't valid");
        this.username = username;
    }

    @JsonValue
    public String getUsername() {
        return username;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Username username1 = (Username) o;
        return Objects.equals(username, username1.username);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(username);
    }

    @Override
    public String toString() {
        return "Username{" +
                "username='" + username + '\'' +
                '}';
    }
}
