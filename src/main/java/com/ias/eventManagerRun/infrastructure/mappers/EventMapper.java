package com.ias.eventManagerRun.infrastructure.mappers;

import com.ias.eventManagerRun.domain.models.ValueObjects.EventDescription;
import com.ias.eventManagerRun.domain.models.ValueObjects.EventName;
import com.ias.eventManagerRun.domain.models.ValueObjects.Password;
import com.ias.eventManagerRun.domain.models.ValueObjects.Username;
import com.ias.eventManagerRun.infrastructure.driven_adapter.mysqlJpa.DBO.EventDBO;
import com.ias.eventManagerRun.infrastructure.driven_adapter.mysqlJpa.DBO.UserDBO;
import com.ias.eventManagerRun.infrastructure.driven_adapter.mysqlJpa.adapters.IEventRepositoryAdapter;
import com.ias.eventManagerRun.infrastructure.entry_points.DTO.EventDTO;
import com.ias.eventManagerRun.infrastructure.entry_points.DTO.UserDTO;

import java.util.*;
import java.util.stream.Collectors;

public class EventMapper {


    public static List<EventDTO> listEventDTOFromEventServiceWithUserWithoutEvents(IEventRepositoryAdapter eventService){
        return Optional.ofNullable(eventService.getAllEvents())
                .orElse(new ArrayList<>())
                .stream()
                .map(eventDBO -> new EventDTO(
                                eventDBO.getId(),
                                eventDBO.getName().getName(),
                                eventDBO.getDescription().getDescription(),
                                eventDBO.getPlace(),
                                eventDBO.getDate(),
                                eventDBO.getUserSet() != null ? eventDBO.getUserSet()
                                        .stream()
                                        .map(userDBO -> new UserDTO(
                                                userDBO.getId(),
                                                userDBO.getUsername().getUsername(),
                                                userDBO.getPassword().getPassword())).toList() : new ArrayList<>()
                        )
                ).toList();
    }

    public static EventDTO eventBOToEventDTOWithUsersWithoutEvents(EventDBO dbo){
        return new EventDTO(
                dbo.getId(),
                dbo.getName().getName(),
                dbo.getDescription().getDescription(),
                dbo.getPlace(),
                dbo.getDate(),
                dbo.getUserSet() != null ? dbo.getUserSet().stream()
                        .map(userDBO -> new UserDTO(
                                        userDBO.getId(),
                                        userDBO.getUsername().getUsername(),
                                        userDBO.getPassword().getPassword()
                                )
                        ).toList(): new ArrayList<>()
        );
    }

    public static EventDBO eventDTOToEventDBOWithoutUsers(EventDTO event){
        return new EventDBO(
                event.getDate(),
                new EventDescription(event.getDescription()),
                null,
                new EventName(event.getName()),
                event.getPlace()
        );
    }

    public static EventDBO eventDTOToDBOWithEventsWithoutUser(EventDTO event){
        return new EventDBO(
                event.getId(),
                new EventName(event.getName()),
                new EventDescription(event.getDescription()),
                event.getPlace(),
                event.getDate(),
                event.getUsers() != null ? event.getUsers().stream()
                        .map(userDTO -> new UserDBO(
                                        userDTO.getId(),
                                        new Username(userDTO.getUsername()),
                                        new Password(userDTO.getPassword()),
                                        null
                                )
                        ).collect(Collectors.toSet()) : new HashSet<>()
        );
    }

    public static Set<EventDTO> setOfEventDTOFromEventServiceWithEventWithoutUsers(IEventRepositoryAdapter eventService, UUID id){
        return Optional.ofNullable(eventService.getAllEventByUser(id))
                .orElse(new HashSet<>())
                .stream()
                .map(eventDBO -> new EventDTO(
                                eventDBO.getId(),
                                eventDBO.getName().getName(),
                                eventDBO.getDescription().getDescription(),
                                eventDBO.getPlace(),
                                eventDBO.getDate(),
                                eventDBO.getUserSet() != null ? eventDBO.getUserSet().stream()
                                        .map(userDBO -> new UserDTO(
                                                        userDBO.getId(),
                                                        userDBO.getUsername().getUsername(),
                                                        userDBO.getPassword().getPassword()
                                                )
                                        ).toList() : new ArrayList<>()
                        )
                ).collect(Collectors.toSet());
    }
}
