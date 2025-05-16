package com.ias.eventManagerRun.domain.usecases;

import com.ias.eventManagerRun.domain.models.EventModel;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.function.BiFunction;
import java.util.function.Function;

public interface EventUseCases {

    List<EventModel> getAllEvents();

    Function<EventModel, EventModel> registerEvent();

    Function<UUID, EventModel> getEventById();

    BiFunction<UUID, EventModel, EventModel> updateEventById();

    BiFunction<UUID, UUID, String> registerUserToEvent();

    Function<UUID, Set<EventModel>> getAllEventByUserId();

    Function<UUID, String> removeEvent();
}
