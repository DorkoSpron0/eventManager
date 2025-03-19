package com.ias.eventManagerRun.infrastructure.entry_points;

import com.ias.eventManagerRun.infrastructure.driven_adapter.mysqlJpa.DBO.EventDBO;
import com.ias.eventManagerRun.infrastructure.driven_adapter.mysqlJpa.adapters.IEventRepositoryAdapter;
import com.ias.eventManagerRun.infrastructure.entry_points.DTO.EventDTO;
import com.ias.eventManagerRun.infrastructure.entry_points.DTO.registerUserToEventDTO;
import com.ias.eventManagerRun.infrastructure.mappers.EventMapper;
import com.ias.eventManagerRun.infrastructure.mappers.UserMapper;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/iasapi/events")
@AllArgsConstructor
public class EventController {

    private IEventRepositoryAdapter eventService;

    @GetMapping("")
    public ResponseEntity<?> getAllEvents(){
        try{
            List<EventDTO> eventDTOS = EventMapper.listEventDTOFromEventServiceWithUserWithoutEvents(eventService);

            return ResponseEntity.status(HttpStatus.OK).body(eventDTOS);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getEventById(@PathVariable UUID id){
        try{
            EventDBO dbo = EventMapper.eventModelToDBO(eventService.getEventById(id));

            EventDTO dtos = EventMapper.eventBOToEventDTOWithUsersWithoutEvents(dbo);

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
            EventDBO dbo = EventMapper.eventDTOToEventDBOWithoutUsers(event);

            EventDBO eventDBO = EventMapper.eventModelToDBO(eventService.registerEvent(EventMapper.eventDBOToModel(dbo)));
            event.setId(eventDBO.getId());

            return ResponseEntity.status(HttpStatus.CREATED).body(event);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateEvent(@PathVariable UUID id,@Valid @RequestBody EventDTO event){
        try{

            EventDBO dbo = EventMapper.eventDTOToDBOWithEventsWithoutUser(event);

            // TODO - RETURN AN DTO
            return ResponseEntity.status(HttpStatus.CREATED).body(eventService.updateEventById(id, EventMapper.eventDBOToModel(dbo)));
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
            Set<EventDTO> dtos = EventMapper.setOfEventDTOFromEventServiceWithEventWithoutUsers(eventService, id);

            return ResponseEntity.status(HttpStatus.OK).body(dtos);
        }catch (IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
