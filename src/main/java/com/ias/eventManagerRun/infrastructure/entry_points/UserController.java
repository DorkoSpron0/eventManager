package com.ias.eventManagerRun.infrastructure.entry_points;

import com.ias.eventManagerRun.domain.models.ValueObjects.EventDescription;
import com.ias.eventManagerRun.domain.models.ValueObjects.EventName;
import com.ias.eventManagerRun.domain.models.ValueObjects.Password;
import com.ias.eventManagerRun.domain.models.ValueObjects.Username;
import com.ias.eventManagerRun.domain.usecases.UserUseCases;
import com.ias.eventManagerRun.infrastructure.driven_adapter.mysqlJpa.DBO.EventDBO;
import com.ias.eventManagerRun.infrastructure.driven_adapter.mysqlJpa.DBO.UserDBO;
import com.ias.eventManagerRun.infrastructure.entry_points.DTO.EventDTO;
import com.ias.eventManagerRun.infrastructure.entry_points.DTO.UserDTO;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/iasapi/users")
@AllArgsConstructor
public class UserController {

    private UserUseCases userUseCases;

    @GetMapping
    public ResponseEntity<?> getAllUser(){
        try{
            //List<UserDBO> userList = userUseCases.getAllUsers();

            List<UserDTO> dtos = Optional.ofNullable(userUseCases.getAllUsers())
                    .orElse(new ArrayList<>())
                    .stream()
                    .map(userDBO -> new UserDTO(
                            userDBO.getId(),
                            userDBO.getUsername().getUsername(),
                            userDBO.getPassword().getPassword(),
                            userDBO.getEventDBOS() != null ? userDBO.getEventDBOS().stream()
                                    .map(eventDBO -> new EventDTO(
                                            eventDBO.getId(),
                                            eventDBO.getDate(),
                                            eventDBO.getDescription().getDescription(),
                                            eventDBO.getName().getName(),
                                            eventDBO.getPlace()
                                    )
                                ).collect(Collectors.toSet()) : new HashSet<>()
                            )
                    ).toList();

            return ResponseEntity.status(HttpStatus.OK).body(dtos);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<?> registerUser(@Valid @RequestBody UserDTO user){
        try{
            UserDBO userDBO = new UserDBO(
                    null,
                    new Username(user.getUsername()),
                    new Password(user.getPassword()),
                    user.getEventDTOS() != null ? user.getEventDTOS().stream()
                            .map(eventDTO -> new EventDBO(
                                    eventDTO.getDate(),
                                    new EventDescription(eventDTO.getDescription()),
                                    null,
                                    new EventName(eventDTO.getName()),
                                    eventDTO.getPlace()
                            )
                        ).collect(Collectors.toSet()) : new HashSet<>()
            );

            UserDBO dbo = userUseCases.registerUser(userDBO);

            UserDTO dto = new UserDTO(
                    dbo.getId(),
                    dbo.getUsername().getUsername(),
                    dbo.getPassword().getPassword(),
                    dbo.getEventDBOS() != null ? dbo.getEventDBOS().stream()
                            .map(eventDBO -> new EventDTO(
                                    eventDBO.getId(),
                                    eventDBO.getDate(),
                                    eventDBO.getDescription().getDescription(),
                                    eventDBO.getName().getName(),
                                    eventDBO.getPlace()
                            )
                        ).collect(Collectors.toSet()) : new HashSet<>()
            );


            return ResponseEntity.status(HttpStatus.CREATED).body(dto);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
