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
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class IEventRepositoryAdapter implements EventUseCases {

    private IEventRepository eventRepository;
    private IUserRepositoryAdapter userService;

    // En una compuesta no hace falta pasarle un parametro T
    private final Function<EventModel, EventModel> saveModel =
            EventMapper.functionModelToDBO
                    .andThen(eventDBO -> eventRepository.save(eventDBO))
                    .andThen(eventDBO -> EventMapper.eventDBOToModel(eventDBO));

    // NO ES COMPUESTA
    private final Function<UUID, EventModel> findById =
            (UUID id) -> EventMapper.functionDBOToModel.apply(
                    eventRepository.findById(id)
                            .orElseThrow(() -> new IllegalArgumentException("Event not found"))
            );


    private final BiFunction<EventDBO, EventModel, EventDBO> updateEvent =
            (EventDBO eventDBOFounded, EventModel model) -> {
                eventDBOFounded.setName(model.getName());
                eventDBOFounded.setPlace(model.getPlace());
                eventDBOFounded.setDate(model.getDate());
                eventDBOFounded.setDescription(model.getDescription());

                eventDBOFounded.setUserSet(eventDBOFounded.getUserSet());
                return eventDBOFounded;
            };

    @Override
    public List<EventModel> getAllEvents() {
        return eventRepository.findAll()
                .stream()
                .map(EventMapper.functionDBOToModel)
                .toList();
    }

    @Override
    public EventModel registerEvent(EventModel event) {
        return saveModel.apply(event);
    }

    @Override
    public EventModel getEventById(UUID id) {
        return findById.apply(id);
    }

    @Override
    public EventModel updateEventById(UUID id, EventModel eventModel) {
        EventDBO eventDBOFounded = EventMapper.functionModelToDBO.apply(findById.apply(id));

        // Actualizamos solo los campos del evento
        eventDBOFounded = updateEvent.apply(eventDBOFounded, eventModel);

        return EventMapper.functionDBOToModel.apply(eventRepository.save(eventDBOFounded));
    }

    @Override
    public String registerUserToEvent(UUID event_id, UUID user_id) {
        EventDBO eventDBOFounded = EventMapper.functionModelToDBO.apply(getEventById(event_id));
        UserDBO userDBOFounded = UserMapper.functionUserModelToDBO.apply(userService.findById(user_id));

        eventDBOFounded.getUserSet().add(userDBOFounded);

        eventRepository.save(eventDBOFounded);

        return "User with id " + user_id + " added to event with id " + event_id + " successfully";
    }

    @Override
    public Set<EventModel> getAllEventByUser(UUID user_id) {
        UserDBO userDBOFounded = UserMapper.functionUserModelToDBO.apply(userService.findById(user_id));

        return userDBOFounded.getEventDBOS().stream().map(EventMapper.functionDBOToModel).collect(Collectors.toSet());
    }

    @Override
    public String removeEvent(UUID id) {
        eventRepository.delete(EventMapper.functionModelToDBO.apply(getEventById(id)));

        return "Event with id " + id + " removes successfully";
    }
}
