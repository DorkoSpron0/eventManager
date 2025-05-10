package com.ias.eventManagerRun.infrastructure.entry_points.DTO.request;

import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

public record EventDTO(UUID id, String name, String description, String place, LocalDate date, Set<UserDTO> users) {

}
