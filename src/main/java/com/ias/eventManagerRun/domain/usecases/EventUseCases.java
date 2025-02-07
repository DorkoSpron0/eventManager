package com.ias.eventManagerRun.domain.usecases;

import com.ias.eventManagerRun.domain.models.Event;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface EventUseCases {
    List<Event> getAllEvents();
    Event registerEvent(Event event);
    Event getEventById(UUID id);
    Event updateEventById(UUID id, Event event);
    String registerUserToEvent(UUID event_id, UUID user_id);
    Set<Event> getAllEventByUser(UUID user_id);
    String removeEvent(UUID id);
}
