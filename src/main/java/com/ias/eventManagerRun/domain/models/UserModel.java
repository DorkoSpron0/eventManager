package com.ias.eventManagerRun.domain.models;

import com.ias.eventManagerRun.domain.models.ValueObjects.Username;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class UserModel {

    private UUID id;
    private Username username;
    private String password;
    Set<EventModel> events = new HashSet<>();

    public UserModel() {
    }

    public UserModel(Set<EventModel> events, UUID id, String password, Username username) {
        this.events = events;
        this.id = id;
        this.password = password;
        this.username = username;
    }

    public Set<EventModel> getEvents() {
        return events;
    }

    public void setEvents(Set<EventModel> events) {
        this.events = events;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
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
                "events=" + events +
                ", id=" + id +
                ", username=" + username +
                ", password='" + password + '\'' +
                '}';
    }
}
