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
@Setter
@Entity(name = "user")
@Table(uniqueConstraints = @UniqueConstraint(columnNames = "username"))
public class UserDBO {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Embedded
    private Username username;

    @Embedded
    private Password password;

    @ManyToMany(mappedBy = "userSet", cascade = CascadeType.ALL)
    @JsonIgnore
    Set<EventDBO> eventDBOS = new HashSet<>();

    public UserDBO(Set<EventDBO> eventDBOS, Username username, Password password, UUID id) {
        this.eventDBOS = eventDBOS;
        this.username = username;
        this.password = password;
        this.id = id;
    }

    public UserDBO(UUID id, Username username, Password password) {
        this.id = id;
        this.username = username;
        this.password = password;
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
