package com.ias.eventManagerRun.infrastructure.driven_adapter.mysqlJpa.DBO;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ias.eventManagerRun.domain.models.ValueObjects.Password;
import com.ias.eventManagerRun.domain.models.ValueObjects.Username;
import jakarta.persistence.*;
import lombok.*;

import java.util.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity(name = "user")
@Table(uniqueConstraints = @UniqueConstraint(columnNames = "username"))
public class UserDBO {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
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
