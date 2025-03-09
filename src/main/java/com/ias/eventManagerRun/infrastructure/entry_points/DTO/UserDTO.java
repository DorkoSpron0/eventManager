package com.ias.eventManagerRun.infrastructure.entry_points.DTO;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Builder
@Getter
@Setter
@AllArgsConstructor
public class UserDTO {

    private UUID id;
    private String username;

    @NotEmpty(message = "Password cant be blank")
    @NotNull(message = "Password cant be null")
    private String password;
    Set<EventDTO> eventDTOS = new HashSet<>();

    public UserDTO() {
    }

    public UserDTO(UUID id, String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
    }

    @Override
    public String toString() {
        return "UserDTO{" +
                "eventDTOS=" + eventDTOS +
                ", password='" + password + '\'' +
                ", username='" + username + '\'' +
                '}';
    }
}
