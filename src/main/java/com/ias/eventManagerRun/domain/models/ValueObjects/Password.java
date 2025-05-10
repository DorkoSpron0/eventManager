package com.ias.eventManagerRun.domain.models.ValueObjects;

public record Password(String value) {

    public Password {
        //  || !value.matches("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{6,}$")
        if(value == null || value.isEmpty()) {
            throw new IllegalArgumentException("Password isn't valid");
        }
    }
}
