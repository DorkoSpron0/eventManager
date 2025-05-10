package com.ias.eventManagerRun.domain.models;

import com.ias.eventManagerRun.domain.models.ValueObjects.EventDescription;
import com.ias.eventManagerRun.domain.models.ValueObjects.EventName;
import com.ias.eventManagerRun.infrastructure.driven_adapter.mysqlJpa.DBO.UserDBO;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public record EventModel(UUID id, EventName name, EventDescription description, String place, LocalDate date, Set<UserModel> userModels) {
    public EventModel{
        userModels = Set.copyOf(userModels);
    }
}
