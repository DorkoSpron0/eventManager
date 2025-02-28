package com.ias.eventManagerRun.infrastructure.driven_adapter.mysqlJpa.adapters;


import com.ias.eventManagerRun.domain.models.EventModel;
import com.ias.eventManagerRun.infrastructure.driven_adapter.mysqlJpa.DBO.EventDBO;
import com.ias.eventManagerRun.infrastructure.driven_adapter.mysqlJpa.DBO.UserDBO;
import com.ias.eventManagerRun.domain.usecases.EventUseCases;
import com.ias.eventManagerRun.infrastructure.driven_adapter.mysqlJpa.IEventRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

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
    public List<EventModel> getAllEvents() {
        return eventRepository.findAll().stream().map(EventDBO::toDomain).toList();
    }

    @Override
    public EventModel registerEvent(EventModel eventModel) {
        EventDBO eventDBO = EventDBO.fromDomain(eventModel);

        System.out.println(eventDBO.toString());

        return eventRepository.save(eventDBO).toDomain();
    }

    @Override
    public EventModel getEventById(UUID id) {
        return eventRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Event not found")).toDomain();
    }

    @Override
    public EventModel updateEventById(UUID id, EventModel eventModel) {
        EventDBO eventDBOFounded = EventDBO.fromDomain(getEventById(id));
        eventDBOFounded.setName(eventModel.getName());
        eventDBOFounded.setPlace(eventModel.getPlace());
        eventDBOFounded.setDate(eventModel.getDate());
        eventDBOFounded.setDescription(eventModel.getDescription());

        eventDBOFounded.setUserSet(eventModel.getUserModels().stream().map(UserDBO::fromDomain).collect(Collectors.toSet()));

        return eventRepository.save(eventDBOFounded).toDomain();
    }

    @Override
    public String registerUserToEvent(UUID event_id, UUID user_id) {
        EventDBO eventDBOFounded = EventDBO.fromDomain(getEventById(event_id));

        System.out.println(eventDBOFounded.toString());
        UserDBO userDBOFounded = UserDBO.fromDomain(userService.findById(user_id));

        eventDBOFounded.getUserSet().add(userDBOFounded);

        eventRepository.save(eventDBOFounded);

        return "User with id " + user_id + " added to event with id " + event_id + " successfully";
    }

    @Override
    public Set<EventModel> getAllEventByUser(UUID user_id) {
        UserDBO userDBOFounded = UserDBO.fromDomain(userService.findById(user_id));

        return userDBOFounded.getEventDBOS().stream().map(EventDBO::toDomain).collect(Collectors.toSet());
    }

    @Override
    public String removeEvent(UUID id) {
        eventRepository.delete(EventDBO.fromDomain(getEventById(id)));

        return "Event with id " + id + " removes successfully";
    }
}
