package com.ias.eventManagerRun.controller.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class registerUserToEventDTO {
    @NotBlank(message = "userId cant be blank")
    @NotNull(message = "userId cant be null")
    private UUID userId;
}
