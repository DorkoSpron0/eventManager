package com.ias.eventManagerRun.controller.DTO;

import com.ias.eventManagerRun.domain.models.Event;
import com.ias.eventManagerRun.domain.models.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

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

    @NotBlank
    @NotNull
    private LocalDate date;

    Set<User> userSet = new HashSet<>();

    public Event toDomain(){
        return new Event(this.name, this.description, this.place, this.date, this.userSet);
    }
}
