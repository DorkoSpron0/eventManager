package com.ias.eventManagerRun.domain.models;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class EventModel {

    private UUID id;
    private String name;
    private String description;
    private String place;
    private LocalDate date;

    private Set<UserModel> userModels = new HashSet<>();

    public EventModel() {
    }

    public EventModel(LocalDate date, String description, UUID id, String name, String place, Set<UserModel> userModels) {
        this.date = date;
        this.description = description;
        this.id = id;
        this.name = name;
        this.place = place;
        this.userModels = userModels;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public Set<UserModel> getUserModels() {
        return userModels;
    }

    public void setUserModels(Set<UserModel> userModels) {
        this.userModels = userModels;
    }

    @Override
    public String toString() {
        return "EventModel{" +
                "date=" + date +
                ", id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", place='" + place + '\'' +
                '}';
    }
}
