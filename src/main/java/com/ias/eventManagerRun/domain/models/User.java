package com.ias.eventManagerRun.domain.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ias.eventManagerRun.domain.models.ValueObjects.Username;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Embedded
    @Column(name = "username", unique = true, nullable = false)
    private Username username;

    @Column(name = "password", nullable = false)
    private String password;

    @ManyToMany(mappedBy = "userSet")
    @JsonIgnore
    Set<Event> events = new HashSet<>();

    public User(Set<Event> events, Username username, String password) {
        this.events = events;
        this.username = username;
        this.password = password;
    }
}
