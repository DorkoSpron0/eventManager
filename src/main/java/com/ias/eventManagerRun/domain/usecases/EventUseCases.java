package com.ias.eventManagerRun.domain.usecases;

import com.ias.eventManagerRun.infrastructure.driven_adapter.mysqlJpa.DBO.EventDBO;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface EventUseCases {
    List<EventDBO> getAllEvents();
    EventDBO registerEvent(EventDBO event);
    EventDBO getEventById(UUID id);
    EventDBO updateEventById(UUID id, EventDBO event);
    String registerUserToEvent(UUID event_id, UUID user_id);
    Set<EventDBO> getAllEventByUser(UUID user_id);
    String removeEvent(UUID id);
}
