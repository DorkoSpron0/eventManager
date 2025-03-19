package com.ias.eventManagerRun.domain.models;

import com.ias.eventManagerRun.domain.models.ValueObjects.Password;
import com.ias.eventManagerRun.domain.models.ValueObjects.Username;
import com.ias.eventManagerRun.infrastructure.driven_adapter.mysqlJpa.DBO.EventDBO;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class UserModel {

    private UUID id;
    private Username username;
    private Password password;
    Set<EventModel> eventDBOS = new HashSet<>();

    public UserModel() {
    }

    public UserModel(UUID id, Username username, Password password) {
        this.id = id;
        this.username = username;
        this.password = password;
    }

    public UserModel(UUID id, Username username, Password password, Set<EventModel> eventDBOS) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.eventDBOS = eventDBOS;
    }

    public Set<EventModel> getEventDBOS() {
        return eventDBOS;
    }

    public void setEventDBOS(Set<EventModel> eventDBOS) {
        this.eventDBOS = eventDBOS;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Password getPassword() {
        return password;
    }

    public void setPassword(Password password) {
        this.password = password;
    }

    public Username getUsername() {
        return username;
    }

    public void setUsername(Username username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return "UserModel{" +
                "id=" + id +
                ", username=" + username +
                ", password=" + password +
                ", eventDBOS=" + eventDBOS +
                '}';
    }
}
