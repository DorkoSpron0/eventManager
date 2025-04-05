package com.ias.eventManagerRun.infrastructure.mappers;

import com.ias.eventManagerRun.domain.models.EventModel;
import com.ias.eventManagerRun.domain.models.UserModel;
import com.ias.eventManagerRun.domain.models.ValueObjects.EventDescription;
import com.ias.eventManagerRun.domain.models.ValueObjects.EventName;
import com.ias.eventManagerRun.domain.models.ValueObjects.Password;
import com.ias.eventManagerRun.domain.models.ValueObjects.Username;
import com.ias.eventManagerRun.domain.usecases.UserUseCases;
import com.ias.eventManagerRun.infrastructure.driven_adapter.mysqlJpa.DBO.EventDBO;
import com.ias.eventManagerRun.infrastructure.driven_adapter.mysqlJpa.DBO.UserDBO;
import com.ias.eventManagerRun.infrastructure.driven_adapter.mysqlJpa.IUserRepository;
import com.ias.eventManagerRun.infrastructure.entry_points.DTO.EventDTO;
import com.ias.eventManagerRun.infrastructure.entry_points.DTO.UserDTO;
import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
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
                .map(UserMapper.userModelToDTO).toList();
    }

    public static UserModel userDBOToModel(UserDBO dbo){
        return new UserModel(
                dbo.getId(),
                dbo.getUsername(),
                dbo.getPassword(),
                dbo.getEventDBOS() != null ? dbo.getEventDBOS().stream()
                        .map(eventDBO -> new EventModel(
                                eventDBO.getId(),
                                eventDBO.getName(),
                                eventDBO.getDescription(),
                                eventDBO.getDate(),
                                eventDBO.getPlace()
                        )).collect(Collectors.toSet()) : new HashSet<>()
        );
    }

    public static UserDBO userModelToDBO(UserModel model){
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
                        )
                ).collect(Collectors.toSet()) : new HashSet<>()
        );
    }

    public static final Function<UserDTO, UserDBO> functionUserDTOToDBO = UserMapper::userDTOToDBO;

    public static final Function<UserDBO, UserModel> functionToModel = UserMapper::userDBOToModel;

    public static final Function<UserDBO, UserDTO> functionUserDBOToDTO = UserMapper::userDBOToDTO;

    public static final Function<UserModel, UserDBO> functionUserModelToDBO = UserMapper::userModelToDBO;

    public static final Function<UserDBO, UserModel> functionUserDBOToModel = UserMapper::userDBOToModel;

    public static final Function<UserModel, UserDTO> userModelToDTO = (UserModel model) -> {
        return new UserDTO(
                model.getId(),
                model.getUsername().getUsername(),
                model.getPassword().getPassword(),
                model.getEventDBOS() != null ? model.getEventDBOS().stream()
                        .map(eventDBO -> new EventDTO(
                                        eventDBO.getId(),
                                        eventDBO.getDate(),
                                        eventDBO.getDescription().getDescription(),
                                        eventDBO.getName().getName(),
                                        eventDBO.getPlace()
                                )
                        ).collect(Collectors.toSet()) : new HashSet<>()
        );
    };

}
