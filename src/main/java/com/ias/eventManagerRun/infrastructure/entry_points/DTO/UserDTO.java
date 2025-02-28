package com.ias.eventManagerRun.infrastructure.entry_points.DTO;

import com.ias.eventManagerRun.domain.models.UserModel;
import com.ias.eventManagerRun.domain.models.ValueObjects.Username;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Builder
@Getter
@Setter
@AllArgsConstructor
public class UserDTO {

    private String username;

    @NotEmpty(message = "Password cant be blank")
    @NotNull(message = "Password cant be null")
    private String password;
    Set<EventDTO> eventDTOS = new HashSet<>();

    public static UserDTO fromDomain(UserModel userModel){
        Set<EventDTO> _eventDTOS = userModel.getEvents().stream()
                .map(EventDTO::fromDomain)
                .collect(Collectors.toSet());

        return new UserDTO(new Username(userModel.getUsername().toString()).toString(), userModel.getPassword(), _eventDTOS);
    }

    public UserModel toDomain(){
        return new UserModel(new HashSet<>(), UUID.randomUUID(), password, new Username(username));
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
