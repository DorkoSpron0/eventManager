package com.ias.eventManagerRun.domain.usecases;

import com.ias.eventManagerRun.domain.models.EventModel;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

public interface EventUseCases {

    Supplier<List<EventModel>> getAllEvents();

    Function<EventModel, Optional<EventModel>> registerEvent();

    Function<UUID, Optional<EventModel>> getEventById();

    BiFunction<UUID, EventModel, Optional<EventModel>> updateEventById();

    BiFunction<UUID, UUID, Optional<String>> registerUserToEvent();

    Function<UUID, Optional<Set<EventModel>>> getAllEventByUserId();

    Function<UUID, Optional<String>> removeEvent();
}
