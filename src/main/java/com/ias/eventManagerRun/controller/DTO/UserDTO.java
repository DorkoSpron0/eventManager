package com.ias.eventManagerRun.controller.DTO;

import com.ias.eventManagerRun.domain.models.Event;
import com.ias.eventManagerRun.domain.models.User;
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

    private String username;
    private String password;
    Set<Event> events = new HashSet<>();

    public User toDomain(){
        return new User(this.getEvents(), this.getUsername(), this.getPassword());
    }
}
