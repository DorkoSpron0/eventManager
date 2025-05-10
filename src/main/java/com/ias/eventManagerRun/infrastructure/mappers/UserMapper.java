package com.ias.eventManagerRun.infrastructure.mappers;

import com.ias.eventManagerRun.domain.models.EventModel;
import com.ias.eventManagerRun.domain.models.UserModel;
import com.ias.eventManagerRun.domain.models.ValueObjects.EventDescription;
import com.ias.eventManagerRun.domain.models.ValueObjects.EventName;
import com.ias.eventManagerRun.domain.models.ValueObjects.Password;
import com.ias.eventManagerRun.domain.models.ValueObjects.Username;
import com.ias.eventManagerRun.infrastructure.driven_adapter.mysqlJpa.DBO.EventDBO;
import com.ias.eventManagerRun.infrastructure.driven_adapter.mysqlJpa.DBO.UserDBO;
import com.ias.eventManagerRun.infrastructure.entry_points.DTO.request.UserDTO;
import com.ias.eventManagerRun.infrastructure.entry_points.DTO.response.EventResponseInfo;
import com.ias.eventManagerRun.infrastructure.entry_points.DTO.response.UserResponseInfo;
import lombok.AllArgsConstructor;

import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;


@AllArgsConstructor
public class UserMapper {
    public static final Function<UserModel, UserDBO> userModelToDBO = (UserModel model) -> new UserDBO(
            model.id(),
            model.username().value(),
            model.password().value(),
            Set.copyOf(
                    Optional.ofNullable(model.eventModel())
                            .orElse(Set.of())
                            .stream()
                            .map(event -> new EventDBO(
                                    event.id(),
                                    event.name().value(),
                                    event.description().value(),
                                    event.place(),
                                    event.date(),
                                    Set.of()
                            ))
                            .collect(Collectors.toSet())
            )
    );

    public static final Function<UserDBO, UserModel> userDBOToModel = (UserDBO dbo) -> new UserModel(
            dbo.getId(),
            new Username(dbo.getUsername()),
            new Password(dbo.getPassword()),
            Set.copyOf(
                    Optional.ofNullable(dbo.getEventDBOS())
                            .orElse(Set.of())
                            .stream()
                            .map(event -> new EventModel(
                                    event.getId(),
                                    new EventName(event.getName()),
                                    new EventDescription(event.getDescription()),
                                    event.getPlace(),
                                    event.getDate(),
                                    Set.of()
                            ))
                            .collect(Collectors.toSet())
            )
    );

    public static final Function<UserModel, UserResponseInfo> userModelToResponseDTO = (UserModel model) -> {
        return new UserResponseInfo(
                model.id(),
                model.username().value(),
                model.password().value(),
                Set.copyOf(
                        Optional.ofNullable(model.eventModel())
                                .orElse(Set.of())
                                .stream()
                                .map(event -> new EventResponseInfo(
                                        event.id(),
                                        event.name().value(),
                                        event.description().value(),
                                        event.place(),
                                        event.date(),
                                        Set.of()
                                ))
                                .collect(Collectors.toSet())
                )
        );
    };

    public static final Function<UserDTO, UserModel> userDTORequireToModel = (UserDTO dto) -> new UserModel(
            dto.id(),
            new Username(dto.username()),
            new Password(dto.password()),
            Set.copyOf(
                    Optional.ofNullable(dto.events())
                            .orElse(Set.of())
                            .stream()
                            .map(event -> new EventModel(
                                    event.id(),
                                    new EventName(event.name()),
                                    new EventDescription(event.description()),
                                    event.place(),
                                    event.date(),
                                    Set.of()
                            ))
                            .collect(Collectors.toSet())
            )
    );
}
