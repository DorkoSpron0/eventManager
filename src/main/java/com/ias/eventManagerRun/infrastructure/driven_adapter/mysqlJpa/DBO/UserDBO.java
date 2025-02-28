package com.ias.eventManagerRun.infrastructure.driven_adapter.mysqlJpa.DBO;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ias.eventManagerRun.domain.models.EventModel;
import com.ias.eventManagerRun.domain.models.UserModel;
import com.ias.eventManagerRun.infrastructure.entry_points.DTO.UserDTO;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity(name = "user")
public class UserDBO {

    @Id
    private UUID id;

    @Column(name = "username", unique = true, nullable = false)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @ManyToMany(mappedBy = "userSet", cascade = CascadeType.ALL)
    @JsonIgnore
    Set<EventDBO> eventDBOS = new HashSet<>();

    public UserDBO(Set<EventDBO> eventDBOS, String username, String password, UUID id) {
        this.eventDBOS = eventDBOS;
        this.username = username;
        this.password = password;
        this.id = id;
    }

    public static UserDBO fromDomain(UserModel userModel)
    {
        Set<EventDBO> _eventDBOS = userModel.getEvents().stream()
                .map(EventDBO::fromDomain)
                .collect(Collectors.toSet());

        return new UserDBO(_eventDBOS, userModel.getUsername(), userModel.getPassword(), userModel.getId());
    }

    public UserModel toDomain(){
        return new UserModel(
                eventDBOS.stream()
                        .map(eventDBO -> new EventModel(
                                eventDBO.getDate(),
                                eventDBO.getDescription(),
                                eventDBO.getId(),
                                eventDBO.getName(),
                                eventDBO.getPlace(),
                                null)
                        ).collect(Collectors.toSet()),
                id,
                password,
                username
        );
    }

    @Override
    public String toString() {
        return "UserDBO{" +
                "eventDBOS=" + eventDBOS +
                ", id=" + id +
                ", password='" + password + '\'' +
                ", username='" + username + '\'' +
                '}';
    }
}
