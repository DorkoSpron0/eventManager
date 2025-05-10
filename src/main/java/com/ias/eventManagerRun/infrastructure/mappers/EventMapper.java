package com.ias.eventManagerRun.infrastructure.mappers;

import com.ias.eventManagerRun.domain.models.EventModel;
import com.ias.eventManagerRun.domain.models.UserModel;
import com.ias.eventManagerRun.domain.models.ValueObjects.EventDescription;
import com.ias.eventManagerRun.domain.models.ValueObjects.EventName;
import com.ias.eventManagerRun.domain.models.ValueObjects.Password;
import com.ias.eventManagerRun.domain.models.ValueObjects.Username;
import com.ias.eventManagerRun.infrastructure.driven_adapter.mysqlJpa.DBO.EventDBO;
import com.ias.eventManagerRun.infrastructure.driven_adapter.mysqlJpa.DBO.UserDBO;
import com.ias.eventManagerRun.infrastructure.entry_points.DTO.request.EventDTO;
import com.ias.eventManagerRun.infrastructure.entry_points.DTO.response.EventResponseInfo;
import com.ias.eventManagerRun.infrastructure.entry_points.DTO.response.UserResponseInfo;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class EventMapper {
    public static final Function<EventModel, EventDBO> eventModelToDBO = (EventModel model) -> new EventDBO(
            model.id(),
            model.name().value(),
            model.description().value(),
            model.place(),
            model.date(),
            Set.copyOf(
                    Optional.ofNullable(model.userModels())
                            .orElse(Set.of())
                            .stream()
                            .map(user -> new UserDBO(
                                    user.id(),
                                    user.username().value(),
                                    user.password().value(),
                                    Set.of()
                            ))
                            .collect(Collectors.toSet())
            )
    );

    public static final Function<EventDBO, EventModel> eventDBOToModel = (EventDBO dbo) -> new EventModel(
            dbo.getId(),
            new EventName(dbo.getName()),
            new EventDescription(dbo.getDescription()),
            dbo.getPlace(),
            dbo.getDate(),
            Set.copyOf(
                    Optional.ofNullable(dbo.getUserSet())
                            .orElse(Set.of())
                            .stream()
                            .map(user -> new UserModel(
                                    user.getId(),
                                    new Username(user.getUsername()),
                                    new Password(user.getPassword()),
                                    Set.of()
                            ))
                            .collect(Collectors.toSet())
            )
    );

    public static final Function<EventModel, EventResponseInfo> eventModelToResponse = (EventModel model) -> new EventResponseInfo(
            model.id(),
            model.name().value(),
            model.description().value(),
            model.place(),
            model.date(),
            Set.copyOf(
                    Optional.ofNullable(model.userModels())
                            .orElse(Set.of())
                            .stream()
                            .map(user -> new UserResponseInfo(
                                    user.id(),
                                    user.username().value(),
                                    user.password().value(),
                                    Set.of()
                            ))
                            .collect(Collectors.toSet())
            )
    );

    public static final Function<EventDTO, EventModel> eventDTORequestToModel = (EventDTO dto) -> new EventModel(
            dto.id(),
            new EventName(dto.name()),
            new EventDescription(dto.description()),
            dto.place(),
            dto.date(),
            Set.copyOf(
                    Optional.ofNullable(dto.users())
                            .orElse(Set.of())
                            .stream()
                            .map(user -> new UserModel(
                                    user.id(),
                                    new Username(user.username()),
                                    new Password(user.password()),
                                    Set.of()
                            ))
                            .collect(Collectors.toSet())
            )
    );
}
