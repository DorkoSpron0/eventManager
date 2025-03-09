package com.ias.eventManagerRun.infrastructure.entry_points;

import com.ias.eventManagerRun.domain.models.ValueObjects.EventDescription;
import com.ias.eventManagerRun.domain.models.ValueObjects.EventName;
import com.ias.eventManagerRun.domain.models.ValueObjects.Password;
import com.ias.eventManagerRun.domain.models.ValueObjects.Username;
import com.ias.eventManagerRun.infrastructure.driven_adapter.mysqlJpa.DBO.EventDBO;
import com.ias.eventManagerRun.infrastructure.driven_adapter.mysqlJpa.DBO.UserDBO;
import com.ias.eventManagerRun.infrastructure.driven_adapter.mysqlJpa.adapters.IEventRepositoryAdapter;
import com.ias.eventManagerRun.infrastructure.entry_points.DTO.EventDTO;
import com.ias.eventManagerRun.infrastructure.entry_points.DTO.UserDTO;
import com.ias.eventManagerRun.infrastructure.entry_points.DTO.registerUserToEventDTO;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/iasapi/events")
@AllArgsConstructor
public class EventController {

    private IEventRepositoryAdapter eventService;

    @GetMapping("")
    public ResponseEntity<?> getAllEvents(){
        try{
            List<EventDBO> eventSet = eventService.getAllEvents();

            List<EventDTO> eventDTOS = Optional.ofNullable(eventService.getAllEvents())
                    .orElse(new ArrayList<>())
                    .stream()
                    .map(eventDBO -> new EventDTO(
                            eventDBO.getId(),
                                    eventDBO.getName().getName(),
                                    eventDBO.getDescription().getDescription(),
                                    eventDBO.getPlace(),
                                    eventDBO.getDate(),
                                    eventDBO.getUserSet() != null ? eventDBO.getUserSet().stream()
                                            .map(userDBO -> new UserDTO(userDBO.getId(), userDBO.getUsername().getUsername(), userDBO.getPassword().getPassword())).toList() : new ArrayList<>()
                            )
                    ).toList();

            return ResponseEntity.status(HttpStatus.OK).body(eventDTOS);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getEventById(@PathVariable UUID id){
        try{
            EventDBO dbo = eventService.getEventById(id);

            EventDTO dtos = new EventDTO(
                    dbo.getId(),
                    dbo.getName().getName(),
                    dbo.getDescription().getDescription(),
                    dbo.getPlace(),
                    dbo.getDate(),
                    dbo.getUserSet() != null ? dbo.getUserSet().stream()
                            .map(userDBO -> new UserDTO(
                                    userDBO.getId(),
                                    userDBO.getUsername().getUsername(),
                                    userDBO.getPassword().getPassword()
                            )
                        ).toList(): new ArrayList<>()
            );

            return ResponseEntity.status(HttpStatus.OK).body(dtos);
        }catch (IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<?> registerEvent(@Valid @RequestBody EventDTO event){
        try{

            /* Al momento de registrar un evento no se le pasan usuarios */
            EventDBO dbo = new EventDBO(
                    event.getDate(),
                    new EventDescription(event.getDescription()),
                    null,
                    new EventName(event.getName()),
                    event.getPlace()
            );



            EventDBO eventDBO = eventService.registerEvent(dbo);
            event.setId(eventDBO.getId());

            return ResponseEntity.status(HttpStatus.CREATED).body(event);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateEvent(@PathVariable UUID id,@Valid @RequestBody EventDTO event){
        try{

            EventDBO dbo = new EventDBO(
                    event.getId(),
                    new EventName(event.getName()),
                    new EventDescription(event.getDescription()),
                    event.getPlace(),
                    event.getDate(),
                    event.getUsers() != null ? event.getUsers().stream()
                            .map(userDTO -> new UserDBO(
                                    userDTO.getId(),
                                    new Username(userDTO.getUsername()),
                                    new Password(userDTO.getPassword()),
                                    null
                            )
                        ).collect(Collectors.toSet()) : new HashSet<>()
            );

            // TODO - RETURN AN DTO
            return ResponseEntity.status(HttpStatus.CREATED).body(eventService.updateEventById(id, dbo));
        }catch (IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteEvent(@PathVariable UUID id){
        try{
            return ResponseEntity.status(HttpStatus.OK).body(eventService.removeEvent(id));
        }catch (IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PostMapping("/{id}/register")
    public ResponseEntity<?> registerUserToEvent(@PathVariable UUID id, @Valid @RequestBody registerUserToEventDTO user){
        try{
            return ResponseEntity.status(HttpStatus.CREATED).body(eventService.registerUserToEvent(id, user.getUserId()));
        }catch (IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<?> getAllEventForUser(@PathVariable UUID id){
        try{

            /* RETURN DTO */
            Set<EventDTO> dtos = Optional.ofNullable(eventService.getAllEventByUser(id))
                    .orElse(new HashSet<>())
                    .stream()
                    .map(eventDBO -> new EventDTO(
                            eventDBO.getId(),
                            eventDBO.getName().getName(),
                            eventDBO.getDescription().getDescription(),
                            eventDBO.getPlace(),
                            eventDBO.getDate(),
                            eventDBO.getUserSet() != null ? eventDBO.getUserSet().stream()
                                    .map(userDBO -> new UserDTO(
                                            userDBO.getId(),
                                            userDBO.getUsername().getUsername(),
                                            userDBO.getPassword().getPassword()
                                    )
                                ).toList() : new ArrayList<>()
                    )
                ).collect(Collectors.toSet());

            return ResponseEntity.status(HttpStatus.OK).body(dtos);
        }catch (IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
