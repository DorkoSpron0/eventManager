package com.ias.eventManagerRun.domain.models.ValueObjects;

import com.fasterxml.jackson.annotation.JsonValue;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import org.springframework.security.core.parameters.P;

import java.util.Objects;

@Embeddable
public class Password {

    @Column(name = "password", nullable = false)
    private String password;

    public Password() {
    }

    public Password(String password) {
        this.password = password;
    }

    @JsonValue
    public String getPassword() {
        return password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Password password1 = (Password) o;
        return Objects.equals(password, password1.password);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(password);
    }

    @Override
    public String toString() {
        return "Password{" +
                "password='" + password + '\'' +
                '}';
    }
}
