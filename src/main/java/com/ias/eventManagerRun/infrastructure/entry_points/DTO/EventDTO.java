package com.ias.eventManagerRun.infrastructure.entry_points.DTO;

import com.ias.eventManagerRun.domain.models.EventModel;
import com.ias.eventManagerRun.domain.models.ValueObjects.EventDescription;
import com.ias.eventManagerRun.domain.models.ValueObjects.EventName;
import com.ias.eventManagerRun.infrastructure.driven_adapter.mysqlJpa.DBO.EventDBO;
import com.ias.eventManagerRun.infrastructure.driven_adapter.mysqlJpa.DBO.UserDBO;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class EventDTO {

    @NotBlank
    @NotNull
    private String name;

    @NotBlank
    @NotNull
    private String description;
    private String place;

    private LocalDate date;

    public EventDTO(LocalDate date, String description, String name, String place) {
        this.date = date;
        this.description = description;
        this.name = name;
        this.place = place;
    }

    public static EventDTO fromDomain(EventModel eventModel){
        return new EventDTO(eventModel.getDate(), eventModel.getDescription().getDescription(), eventModel.getName().getName(), eventModel.getPlace());
    }

    public EventModel toDBO(){
        return new EventModel(date, new EventDescription(description), UUID.randomUUID(), new EventName(name), place, new HashSet<>());
    }

}
