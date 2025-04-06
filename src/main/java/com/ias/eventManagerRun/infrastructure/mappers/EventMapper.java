package com.ias.eventManagerRun.infrastructure.mappers;

import com.ias.eventManagerRun.domain.models.EventModel;
import com.ias.eventManagerRun.domain.models.UserModel;
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
import java.util.function.Function;
import java.util.stream.Collectors;

public class EventMapper {

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

    public static EventModel eventDBOToModel(EventDBO dbo){
        return new EventModel(
                dbo.getId(),
                dbo.getName(),
                dbo.getDescription(),
                dbo.getDate(),
                dbo.getPlace(),
                dbo.getUserSet() != null ? dbo.getUserSet().stream()
                        .map(userDBO -> new UserModel(
                                userDBO.getId(),
                                userDBO.getUsername(),
                                userDBO.getPassword())
                        ).collect(Collectors.toSet()
                ) : new HashSet<>()
        );
    }

    public static final Function<EventDBO, EventModel> functionDBOToModel = (EventDBO dbo) ->
            new EventModel(
                dbo.getId(),
                dbo.getName(),
                dbo.getDescription(),
                dbo.getDate(),
                dbo.getPlace(),
                dbo.getUserSet() != null ? dbo.getUserSet().stream()
                        .map(UserMapper.functionUserDBOToModel).collect(Collectors.toSet()) : new HashSet<>()
        );

    public static final Function<EventModel, EventDBO> functionModelToDBO = (EventModel model) -> new EventDBO(
            model.getId(),
            model.getName(),
            model.getDescription(),
            model.getPlace(),
            model.getDate(),
            model.getUserSet() != null ? model.getUserSet().stream()
                    .map(UserMapper.functionUserModelToDBO).collect(Collectors.toSet()) : new HashSet<>()
    );

    public static final Function<EventModel, EventDTO> functionModelToDTO = (EventModel model) ->
            new EventDTO(
                    model.getId(),
                    model.getName().getName(),
                    model.getDescription().getDescription(),
                    model.getPlace(),
                    model.getDate(),
                    model.getUserSet() != null ? model.getUserSet().stream()
                            .map(userModel -> new UserDTO(
                                    userModel.getId(),
                                    userModel.getUsername().getUsername(),
                                    userModel.getPassword().getPassword()
                            )).toList() : new ArrayList<>()
            );

    public static final Function<EventDTO, EventDBO> functionDTOToDBO = (EventDTO dto) ->
        new EventDBO(
                dto.getId(),
                new EventName(dto.getName()),
                new EventDescription(dto.getDescription()),
                dto.getPlace(),
                dto.getDate(),
                dto.getUsers() != null ? dto.getUsers().stream()
                        .map(userDTO -> new UserDBO(
                                userDTO.getId(),
                                new Username(userDTO.getUsername()),
                                new Password(userDTO.getPassword())
                        )).collect(Collectors.toSet()) : new HashSet<>()
        );

    public static final Function<EventDBO, EventDTO> functionDBOToDTO = (EventDBO dbo) ->
            new EventDTO(
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
                            )).toList() : new ArrayList<>()
            );
}
