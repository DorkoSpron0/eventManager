package com.ias.eventManagerRun.infrastructure.driven_adapter.mysqlJpa.DBO;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Builder
@NoArgsConstructor
@Getter
@Entity(name = "event")
@Table(uniqueConstraints = @UniqueConstraint(columnNames = "name"))
public class EventDBO {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String name;

    private String description;

    @Column(name = "place", nullable = false)
    private String place;

    @Column(name = "date", nullable = false)
    private LocalDate date;

    @ManyToMany
    @JoinTable(name = "event_user", joinColumns = @JoinColumn(name = "event_id"), inverseJoinColumns = @JoinColumn(name = "user_id"))
    Set<UserDBO> userSet = new HashSet<>();

    public EventDBO(UUID id, String name, String description, String place, LocalDate date, Set<UserDBO> userSet) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.place = place;
        this.date = date;
        this.userSet = userSet;
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
