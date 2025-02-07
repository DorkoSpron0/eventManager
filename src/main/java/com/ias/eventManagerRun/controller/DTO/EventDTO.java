package com.ias.eventManagerRun.controller.DTO;

import com.ias.eventManagerRun.domain.models.Event;
import com.ias.eventManagerRun.domain.models.User;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
public class EventDTO {

    private String name;
    private String description;
    private String place;
    private LocalDate date;

    Set<User> userSet = new HashSet<>();

    public Event toDomain(){
        return new Event(this.name, this.description, this.place, this.date, this.userSet);
    }
}
