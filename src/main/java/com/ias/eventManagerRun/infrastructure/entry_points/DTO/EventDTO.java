package com.ias.eventManagerRun.infrastructure.entry_points.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class EventDTO {

    private UUID id;

    @NotBlank
    @NotNull
    private String name;

    @NotBlank
    @NotNull
    private String description;
    private String place;

    private LocalDate date;

    private List<UserDTO> users = new ArrayList<>();

    public EventDTO(UUID id, LocalDate date, String description, String name, String place) {
        this.id = id;
        this.date = date;
        this.description = description;
        this.name = name;
        this.place = place;
    }

}
