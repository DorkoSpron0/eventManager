package com.ias.eventManagerRun.infrastructure.mappers;

import com.ias.eventManagerRun.domain.models.EventModel;
import com.ias.eventManagerRun.domain.models.UserModel;
import com.ias.eventManagerRun.domain.models.ValueObjects.Password;
import com.ias.eventManagerRun.domain.models.ValueObjects.Username;
import com.ias.eventManagerRun.infrastructure.driven_adapter.mysqlJpa.DBO.EventDBO;
import com.ias.eventManagerRun.infrastructure.driven_adapter.mysqlJpa.DBO.UserDBO;
import com.ias.eventManagerRun.infrastructure.entry_points.DTO.UserDTO;
import lombok.AllArgsConstructor;

import java.util.HashSet;
import java.util.stream.Collectors;


@AllArgsConstructor
public class UserMapper {

    public static UserDBO userModelToDBO(UserModel model) {
        return new UserDBO(
                model.getId(),
                model.getUsername(),
                model.getPassword(),
                model.getEventDBOS() != null ? model.getEventDBOS().stream()
                        .map(eventModel -> new EventDBO(
                                eventModel.getDate(),
                                eventModel.getDescription(),
                                eventModel.getId(),
                                eventModel.getName(),
                                eventModel.getPlace()
                        )).collect(Collectors.toSet()) : new HashSet<>()
        );
    }

    public static UserModel userDBOToModel(UserDBO dbo) {
        return new UserModel(
                dbo.getId(),
                dbo.getUsername(),
                dbo.getPassword(),
                dbo.getEventDBOS() != null ? dbo.getEventDBOS().stream() // Se puede mejorar con Optional
                        .map(eventDBO -> new EventModel(
                                eventDBO.getId(),
                                eventDBO.getName(),
                                eventDBO.getDescription(),
                                eventDBO.getDate(),
                                eventDBO.getPlace()
                        )).collect(Collectors.toSet()) : new HashSet<>()
        );
    }

    public static UserDTO userModelToDTO(UserModel model){
        return  new UserDTO(
                model.getId(),
                model.getUsername().getUsername(),
                model.getPassword().getPassword(),
                model.getEventDBOS() != null ? model.getEventDBOS().stream()
                        .map(EventMapper::eventModelTODTO)
                        .collect(Collectors.toSet()) : new HashSet<>()
        );
    }
    public static UserDBO userDTOTODBO(UserDTO dto){
        return new UserDBO(
                dto.getId(),
                new Username(dto.getUsername()),
                new Password(dto.getPassword()),
                dto.getEventDTOS() != null ? dto.getEventDTOS().stream()
                        .map(EventMapper::eventDTOToDBO)
                        .collect(Collectors.toSet()) : new HashSet<>()
        );
    }

    public static UserDTO userDBoToDTO(UserDBO dbo){
        return new UserDTO(
                dbo.getId(),
                dbo.getUsername().getUsername(),
                dbo.getPassword().getPassword(),
                dbo.getEventDBOS() != null ? dbo.getEventDBOS().stream()
                        .map(EventMapper::eventDBOToDTO)
                        .collect(Collectors.toSet()) : new HashSet<>()
        );
    }
}
