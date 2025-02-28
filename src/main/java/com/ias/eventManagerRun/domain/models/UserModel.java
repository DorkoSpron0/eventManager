package com.ias.eventManagerRun.domain.models;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class UserModel {

    private UUID id;
    private String username;
    private String password;
    Set<EventModel> events = new HashSet<>();

    public UserModel() {
    }

    public UserModel(Set<EventModel> events, UUID id, String password, String username) {
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
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
