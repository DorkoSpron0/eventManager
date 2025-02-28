package com.ias.eventManagerRun.domain.models.ValueObjects;

import jakarta.persistence.Embeddable;

import java.util.Objects;
import java.util.regex.Pattern;

@Embeddable // JPA NO CREA UNA TABLA EXTRA
public class Username {
    
    private static final Pattern PATTERN = Pattern.compile("^[a-zA-Z0-9_]{2,}$");

    private String username;

    public Username() {}

    public Username(String _value) {
        if (_value == null || !PATTERN.matcher(_value).matches()) {
            throw new IllegalArgumentException("Username isn't valid");
        }
        this.username = _value;
    }

    //@JsonValue  Hace que sea un STRING el valor, no como objeto
    public String getValue(){
        return this.username;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Username username = (Username) o;
        return Objects.equals(username, username.username);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(username);
    }

    @Override
    public String toString() {
        return "Username{" +
                "value='" + username + '\'' +
                '}';
    }
}
