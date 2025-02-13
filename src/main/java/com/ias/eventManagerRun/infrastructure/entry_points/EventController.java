package com.ias.eventManagerRun.infrastructure.entry_points;

import com.ias.eventManagerRun.controller.DTO.EventDTO;
import com.ias.eventManagerRun.controller.DTO.registerUserToEventDTO;
import com.ias.eventManagerRun.infrastructure.driven_adapter.mysqlJpa.adapters.IEventRepositoryAdapter;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/iasapi/events")
@AllArgsConstructor
public class EventController {

    private IEventRepositoryAdapter eventService;

    @GetMapping("")
    public ResponseEntity<?> getAllEvents(){
        try{
            return ResponseEntity.status(HttpStatus.OK).body(eventService.getAllEvents());
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getEventById(@PathVariable UUID id){
        try{
            return ResponseEntity.status(HttpStatus.OK).body(eventService.getEventById(id));
        }catch (IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<?> registerEvent(@Valid @RequestBody EventDTO event){
        try{
            return ResponseEntity.status(HttpStatus.CREATED).body(eventService.registerEvent(event.toDomain()));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateEvent(@PathVariable UUID id,@Valid @RequestBody EventDTO event){
        try{
            return ResponseEntity.status(HttpStatus.CREATED).body(eventService.updateEventById(id, event.toDomain()));
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
            return ResponseEntity.status(HttpStatus.OK).body(eventService.getAllEventByUser(id));
        }catch (IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
