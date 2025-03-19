package com.ias.eventManagerRun.infrastructure.driven_adapter.mysqlJpa.adapters;


import com.ias.eventManagerRun.domain.models.EventModel;
import com.ias.eventManagerRun.infrastructure.driven_adapter.mysqlJpa.DBO.EventDBO;
import com.ias.eventManagerRun.infrastructure.driven_adapter.mysqlJpa.DBO.UserDBO;
import com.ias.eventManagerRun.domain.usecases.EventUseCases;
import com.ias.eventManagerRun.infrastructure.driven_adapter.mysqlJpa.IEventRepository;
import com.ias.eventManagerRun.infrastructure.mappers.EventMapper;
import com.ias.eventManagerRun.infrastructure.mappers.UserMapper;
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
        return eventRepository.findAll().stream().map(EventMapper::eventDBOToModel).toList();
    }

    @Override
    public EventModel registerEvent(EventModel event) {
        return EventMapper.eventDBOToModel(eventRepository.save(EventMapper.eventModelToDBO(event)));
    }

    @Override
    public EventModel getEventById(UUID id) {
        return EventMapper.eventDBOToModel(eventRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Event not found")));
    }

    @Override
    public EventModel updateEventById(UUID id, EventModel EventDBO) {
        EventDBO eventDBOFounded = EventMapper.eventModelToDBO(getEventById(id));

        // Actualizamos solo los campos del evento
        eventDBOFounded.setName(EventDBO.getName());
        eventDBOFounded.setPlace(EventDBO.getPlace());
        eventDBOFounded.setDate(EventDBO.getDate());
        eventDBOFounded.setDescription(EventDBO.getDescription());

        eventDBOFounded.setUserSet(eventDBOFounded.getUserSet());

        return EventMapper.eventDBOToModel(eventRepository.save(eventDBOFounded));
    }


    @Override
    public String registerUserToEvent(UUID event_id, UUID user_id) {
        EventDBO eventDBOFounded = EventMapper.eventModelToDBO(getEventById(event_id));

        System.out.println(eventDBOFounded.toString());
        UserDBO userDBOFounded = UserMapper.userModelToDBO(userService.findById(user_id));

        eventDBOFounded.getUserSet().add(userDBOFounded);

        eventRepository.save(eventDBOFounded);

        return "User with id " + user_id + " added to event with id " + event_id + " successfully";
    }

    @Override
    public Set<EventModel> getAllEventByUser(UUID user_id) {
        UserDBO userDBOFounded = UserMapper.userModelToDBO(userService.findById(user_id));

        return userDBOFounded.getEventDBOS().stream().map(EventMapper::eventDBOToModel).collect(Collectors.toSet());
    }

    @Override
    public String removeEvent(UUID id) {
        eventRepository.delete(EventMapper.eventModelToDBO(getEventById(id)));

        return "Event with id " + id + " removes successfully";
    }
}
