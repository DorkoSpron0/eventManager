package com.ias.eventManagerRun.infrastructure.mappers;

import com.ias.eventManagerRun.domain.models.ValueObjects.EventDescription;
import com.ias.eventManagerRun.domain.models.ValueObjects.EventName;
import com.ias.eventManagerRun.domain.models.ValueObjects.Password;
import com.ias.eventManagerRun.domain.models.ValueObjects.Username;
import com.ias.eventManagerRun.domain.usecases.UserUseCases;
import com.ias.eventManagerRun.infrastructure.driven_adapter.mysqlJpa.DBO.EventDBO;
import com.ias.eventManagerRun.infrastructure.driven_adapter.mysqlJpa.DBO.UserDBO;
import com.ias.eventManagerRun.infrastructure.entry_points.DTO.EventDTO;
import com.ias.eventManagerRun.infrastructure.entry_points.DTO.UserDTO;
import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@AllArgsConstructor
public class UserMapper {

    public static UserDBO userDTOToDBO(UserDTO user){
        return new UserDBO(
                null,
                new Username(user.getUsername()),
                new Password(user.getPassword()),
                user.getEventDTOS() != null ? user.getEventDTOS().stream()
                        .map(eventDTO -> new EventDBO(
                                        eventDTO.getDate(),
                                        new EventDescription(eventDTO.getDescription()),
                                        null,
                                        new EventName(eventDTO.getName()),
                                        eventDTO.getPlace()
                                )
                        ).collect(Collectors.toSet()) : new HashSet<>()
        );
    }


    public static UserDTO userDBOToDTO(UserDBO dbo){
        return new UserDTO(
                dbo.getId(),
                dbo.getUsername().getUsername(),
                dbo.getPassword().getPassword(),
                dbo.getEventDBOS() != null ? dbo.getEventDBOS().stream()
                        .map(eventDBO -> new EventDTO(
                                        eventDBO.getId(),
                                        eventDBO.getDate(),
                                        eventDBO.getDescription().getDescription(),
                                        eventDBO.getName().getName(),
                                        eventDBO.getPlace()
                                )
                        ).collect(Collectors.toSet()) : new HashSet<>()
        );
    }

    public static List<UserDTO> listUserDToFromUserUseCasesWithEventsWithoutUser(UserUseCases userUseCases){
        return Optional.ofNullable(userUseCases.getAllUsers())
                .orElse(new ArrayList<>())
                .stream()
                .map(userDBO -> new UserDTO(
                                userDBO.getId(),
                                userDBO.getUsername().getUsername(),
                                userDBO.getPassword().getPassword(),
                                userDBO.getEventDBOS() != null ? userDBO.getEventDBOS().stream()
                                        .map(eventDBO -> new EventDTO(
                                                        eventDBO.getId(),
                                                        eventDBO.getDate(),
                                                        eventDBO.getDescription().getDescription(),
                                                        eventDBO.getName().getName(),
                                                        eventDBO.getPlace()
                                                )
                                        ).collect(Collectors.toSet()) : new HashSet<>()
                        )
                ).toList();
    }
}
