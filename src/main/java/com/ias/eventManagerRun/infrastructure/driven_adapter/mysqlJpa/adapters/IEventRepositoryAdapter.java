package com.ias.eventManagerRun.infrastructure.driven_adapter.mysqlJpa.adapters;


import com.ias.eventManagerRun.domain.models.Event;
import com.ias.eventManagerRun.domain.models.User;
import com.ias.eventManagerRun.domain.usecases.EventUseCases;
import com.ias.eventManagerRun.infrastructure.driven_adapter.mysqlJpa.IEventRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@AllArgsConstructor
public class IEventRepositoryAdapter implements EventUseCases {

    private IEventRepository eventRepository;
    private IUserRepositoryAdapter userService;

    @Override
    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    @Override
    public Event registerEvent(Event event) {
        return eventRepository.save(event);
    }

    @Override
    public Event getEventById(UUID id) {
        return eventRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Event not found"));
    }

    @Override
    public Event updateEventById(UUID id, Event event) {
        Event eventFounded = getEventById(id);
        eventFounded.setName(event.getName());
        eventFounded.setPlace(event.getPlace());
        eventFounded.setDate(event.getDate());
        eventFounded.setDescription(event.getDescription());
        eventFounded.setUserSet(event.getUserSet()); // Actualiza todos los usuarios

        return eventRepository.save(eventFounded);
    }

    @Override
    public String registerUserToEvent(UUID event_id, UUID user_id) {
        Event eventFounded = getEventById(event_id);
        User userFounded = userService.findById(user_id);

        eventFounded.getUserSet().add(userFounded);

        eventRepository.save(eventFounded);

        return "User with id " + user_id + " added to event with id " + event_id + " successfully";
    }

    @Override
    public Set<Event> getAllEventByUser(UUID user_id) {
        User userFounded = userService.findById(user_id);

        return userFounded.getEvents();
    }

    @Override
    public String removeEvent(UUID id) {
        eventRepository.delete(getEventById(id));

        return "Event with id " + id + " removes successfully";
    }
}
