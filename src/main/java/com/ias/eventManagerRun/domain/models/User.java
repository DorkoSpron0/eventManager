package com.ias.eventManagerRun.domain.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

    @Column(name = "username", unique = true)
    private String username;
    private String password;

    @ManyToMany(mappedBy = "userSet")
    @JsonIgnore
    Set<Event> events = new HashSet<>();

    public User(Set<Event> events, String username, String password) {
        this.events = events;
        this.username = username;
        this.password = password;
    }
}
