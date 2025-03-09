package com.ias.eventManagerRun.infrastructure.driven_adapter.mysqlJpa.adapters;


import com.ias.eventManagerRun.infrastructure.driven_adapter.mysqlJpa.DBO.EventDBO;
import com.ias.eventManagerRun.infrastructure.driven_adapter.mysqlJpa.DBO.UserDBO;
import com.ias.eventManagerRun.domain.usecases.EventUseCases;
import com.ias.eventManagerRun.infrastructure.driven_adapter.mysqlJpa.IEventRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class IEventRepositoryAdapter implements EventUseCases {

    private IEventRepository eventRepository;
    private IUserRepositoryAdapter userService;

    @Override
    public List<EventDBO> getAllEvents() {
        return eventRepository.findAll();
    }

    @Override
    public EventDBO registerEvent(EventDBO event) {
        return eventRepository.save(event);
    }

    @Override
    public EventDBO getEventById(UUID id) {
        return eventRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Event not found"));
    }

    @Override
    public EventDBO updateEventById(UUID id, EventDBO EventDBO) {
        EventDBO eventDBOFounded = getEventById(id);

        // Actualizamos solo los campos del evento
        eventDBOFounded.setName(EventDBO.getName());
        eventDBOFounded.setPlace(EventDBO.getPlace());
        eventDBOFounded.setDate(EventDBO.getDate());
        eventDBOFounded.setDescription(EventDBO.getDescription());

        eventDBOFounded.setUserSet(eventDBOFounded.getUserSet());

        return eventRepository.save(eventDBOFounded);
    }


    @Override
    public String registerUserToEvent(UUID event_id, UUID user_id) {
        EventDBO eventDBOFounded = getEventById(event_id);

        System.out.println(eventDBOFounded.toString());
        UserDBO userDBOFounded = userService.findById(user_id);

        eventDBOFounded.getUserSet().add(userDBOFounded);

        eventRepository.save(eventDBOFounded);

        return "User with id " + user_id + " added to event with id " + event_id + " successfully";
    }

    @Override
    public Set<EventDBO> getAllEventByUser(UUID user_id) {
        UserDBO userDBOFounded = userService.findById(user_id);

        return userDBOFounded.getEventDBOS();
    }

    @Override
    public String removeEvent(UUID id) {
        eventRepository.delete(getEventById(id));

        return "Event with id " + id + " removes successfully";
    }
}
