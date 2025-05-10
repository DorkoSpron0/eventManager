package com.ias.eventManagerRun.infrastructure.entry_points.DTO.response;

import com.ias.eventManagerRun.infrastructure.entry_points.DTO.request.EventDTO;

import java.util.Set;
import java.util.UUID;

public record UserResponseInfo(UUID id,
                               String username,
                               String password,
                               Set<EventResponseInfo> events) {
}
