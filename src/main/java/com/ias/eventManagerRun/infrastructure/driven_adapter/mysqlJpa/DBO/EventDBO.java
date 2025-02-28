package com.ias.eventManagerRun.infrastructure.driven_adapter.mysqlJpa.DBO;

import com.ias.eventManagerRun.domain.models.EventModel;
import com.ias.eventManagerRun.domain.models.UserModel;
import jakarta.persistence.*;
import jdk.jfr.Event;
import lombok.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity(name = "event")
public class EventDBO {

    @Id
    private UUID id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "place", nullable = false)
    private String place;

    @Column(name = "date", nullable = false)
    private LocalDate date;

    @ManyToMany
    @JoinTable(name = "event_user", joinColumns = @JoinColumn(name = "event_id"), inverseJoinColumns = @JoinColumn(name = "user_id"))
    Set<UserDBO> userSet = new HashSet<>();

    public EventDBO(LocalDate date, String description, UUID id, String name, String place) {
        this.date = date;
        this.description = description;
        this.id = id;
        this.name = name;
        this.place = place;
    }

    public EventDBO(String name, String description, String place, LocalDate date, Set<UserDBO> userDBOSet) {
        this.name = name;
        this.description = description;
        this.place = place;
        this.date = date;
        this.userSet = userDBOSet;
    }


    public static EventDBO fromDomain(EventModel eventModel){
        return new EventDBO(eventModel.getDate(), eventModel.getDescription(), eventModel.getId(), eventModel.getName(), eventModel.getPlace());
    }

    public EventModel toDomain(){
        return new EventModel(
                date,
                description,
                id,
                name,
                place,
                userSet.stream()
                        .map(userDBO -> new UserModel(null, userDBO.getId(), userDBO.getPassword(), userDBO.getUsername()))
                        .collect(Collectors.toSet())
        );
    }

    @Override
    public String toString() {
        return "EventDBO{" +
                "date=" + date +
                ", id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", place='" + place + '\'' +
                ", userSet=" + userSet +
                '}';
    }
}
