package com.ias.eventManagerRun.domain.models.ValueObjects;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.util.Objects;

@Embeddable
public class Password {

    @Column(name = "password", nullable = false)
    private String password;

    public Password() {
    }

    public Password(String password) {
        if(!password.matches("^[a-zA-Z ]{2,}")) throw new IllegalArgumentException("Password invalid");
        this.password = password;
    }

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
