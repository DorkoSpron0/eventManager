package com.ias.eventManagerRun.domain.models;

import com.ias.eventManagerRun.domain.models.ValueObjects.EventDescription;
import com.ias.eventManagerRun.domain.models.ValueObjects.EventName;
import com.ias.eventManagerRun.infrastructure.driven_adapter.mysqlJpa.DBO.UserDBO;

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
    Set<UserModel> userSet = new HashSet<>();

    public EventModel() {
    }

    public EventModel(UUID id, EventName name, EventDescription description, LocalDate date, String place) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.date = date;
        this.place = place;
    }

    public EventModel(UUID id, EventName name, EventDescription description, LocalDate date, String place, Set<UserModel> userSet) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.date = date;
        this.place = place;
        this.userSet = userSet;
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

    public Set<UserModel> getUserSet() {
        return userSet;
    }

    public void setUserSet(Set<UserModel> userSet) {
        this.userSet = userSet;
    }

    @Override
    public String toString() {
        return "EventModel{" +
                "date=" + date +
                ", id=" + id +
                ", name=" + name +
                ", description=" + description +
                ", place='" + place + '\'' +
                ", userSet=" + userSet +
                '}';
    }
}
