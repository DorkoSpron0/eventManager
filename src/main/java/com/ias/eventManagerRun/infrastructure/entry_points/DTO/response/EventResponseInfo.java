package com.ias.eventManagerRun.infrastructure.entry_points.DTO.response;

import com.ias.eventManagerRun.infrastructure.entry_points.DTO.request.UserDTO;

import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

public record EventResponseInfo(UUID id,
                                String name,
                                String description,
                                String place,
                                LocalDate date,
                                Set<UserResponseInfo> users) {
}
