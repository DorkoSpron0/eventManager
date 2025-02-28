package com.ias.eventManagerRun.infrastructure.entry_points.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class registerUserToEventDTO {
    private UUID userId;
}
