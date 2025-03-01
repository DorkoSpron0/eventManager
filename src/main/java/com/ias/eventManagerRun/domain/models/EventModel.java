package com.ias.eventManagerRun.domain.models;

import com.ias.eventManagerRun.domain.models.ValueObjects.EventDescription;
import com.ias.eventManagerRun.domain.models.ValueObjects.EventName;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class EventModel {

    private UUID id;
    private EventName name;
    private EventDescription description;
    private String place;
    private LocalDate date;

    private Set<UserModel> userModels = new HashSet<>();

    public EventModel() {
    }

    public EventModel(LocalDate date, EventDescription description, UUID id, EventName name, String place, Set<UserModel> userModels) {
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

    public EventDescription getDescription() {
        return description;
    }

    public void setDescription(EventDescription description) {
        this.description = description;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public EventName getName() {
        return name;
    }

    public void setName(EventName name) {
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
