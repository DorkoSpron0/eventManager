package com.ias.eventManagerRun.domain.models;

import com.ias.eventManagerRun.domain.models.ValueObjects.Password;
import com.ias.eventManagerRun.domain.models.ValueObjects.Username;

import java.util.Set;
import java.util.UUID;

public record UserModel(UUID id, Username username, Password password, Set<EventModel> eventModel) {
    public UserModel {
        eventModel = Set.copyOf(eventModel); // -> Hace el Set inmutable
    }
}
