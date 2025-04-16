package com.ias.eventManagerRun.infrastructure.mappers;

import com.ias.eventManagerRun.domain.models.EventModel;
import com.ias.eventManagerRun.domain.models.ValueObjects.EventDescription;
import com.ias.eventManagerRun.domain.models.ValueObjects.EventName;
import com.ias.eventManagerRun.domain.models.ValueObjects.Password;
import com.ias.eventManagerRun.domain.models.ValueObjects.Username;
import com.ias.eventManagerRun.infrastructure.driven_adapter.mysqlJpa.DBO.EventDBO;
import com.ias.eventManagerRun.infrastructure.driven_adapter.mysqlJpa.DBO.UserDBO;
import com.ias.eventManagerRun.infrastructure.entry_points.DTO.EventDTO;
import com.ias.eventManagerRun.infrastructure.entry_points.DTO.UserDTO;

import java.util.*;
import java.util.stream.Collectors;

public class EventMapper {

    public static EventModel eventDBOToModel(EventDBO dbo) {
        return new EventModel(
                dbo.getId(),
                dbo.getName(),
                dbo.getDescription(),
                dbo.getDate(),
                dbo.getPlace(),
                dbo.getUserSet() != null ? dbo.getUserSet().stream()
                        .map(UserMapper::userDBOToModel)
                        .collect(Collectors.toSet()) : new HashSet<>()
        );
    }

    public static EventDBO eventModelToDBO(EventModel model) {
        return new EventDBO(
                model.getId(),
                model.getName(),
                model.getDescription(),
                model.getPlace(),
                model.getDate(),
                model.getUserSet() != null ? model.getUserSet().stream()
                        .map(UserMapper::userModelToDBO).collect(Collectors.toSet()) : new HashSet<>()
        );
    }

    public static EventDTO eventModelTODTO(EventModel model) {
        return new EventDTO(
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
    }

    public static EventDBO eventDTOToDBO(EventDTO dto) {
        return new EventDBO(
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
    }

    public static EventDTO eventDBOToDTO(EventDBO dbo){
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
                        )).toList() : new ArrayList<>()
        );
    }
}
