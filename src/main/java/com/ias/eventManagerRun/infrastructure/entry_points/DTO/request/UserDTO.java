package com.ias.eventManagerRun.infrastructure.entry_points.DTO.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.Set;
import java.util.UUID;

public record UserDTO(UUID id,
                      String username,
                      @NotEmpty(message = "Password cant be blank") @NotNull(message = "Password cant be null")String password,
                      Set<EventDTO> events) {
}
