package com.ias.eventManagerRun.controller.DTO;

import com.ias.eventManagerRun.domain.models.Event;
import com.ias.eventManagerRun.domain.models.User;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Builder
@Getter
@Setter
@AllArgsConstructor
public class UserDTO {

    @NotEmpty(message = "Username cant be blank")
    private String username;

    @NotEmpty(message = "Password cant be blank")
    @NotNull(message = "Password cant be null")
    private String password;
    Set<Event> events = new HashSet<>();

    public User toDomain(){
        return new User(this.getEvents(), this.getUsername(), this.getPassword());
    }
}
