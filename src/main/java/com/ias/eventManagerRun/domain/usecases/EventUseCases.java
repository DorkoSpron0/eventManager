package com.ias.eventManagerRun.domain.usecases;

import com.ias.eventManagerRun.domain.models.EventModel;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface EventUseCases {
    List<EventModel> getAllEvents();
    EventModel registerEvent(EventModel event);
    EventModel getEventById(UUID id);
    EventModel updateEventById(UUID id, EventModel event);
    String registerUserToEvent(UUID event_id, UUID user_id);
    Set<EventModel> getAllEventByUser(UUID user_id);
    String removeEvent(UUID id);
}
