package com.ias.eventManagerRun.infrastructure.entry_points;

import com.ias.eventManagerRun.domain.models.EventModel;
import com.ias.eventManagerRun.domain.usecases.EventUseCases;
import com.ias.eventManagerRun.infrastructure.entry_points.DTO.request.registerUserToEventDTO;
import com.ias.eventManagerRun.infrastructure.entry_points.DTO.request.EventDTO;
import com.ias.eventManagerRun.infrastructure.mappers.EventMapper;
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

    private EventUseCases eventUseCases;

    @GetMapping
    public ResponseEntity<?> getAllEvents(){
        return ResponseEntity.status(HttpStatus.OK).body(this.eventUseCases.getAllEvents().get().stream().map(EventMapper.eventModelToResponse));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getEventById(@PathVariable UUID id) {
        Optional<EventModel> optionalEvent = eventUseCases.getEventById().apply(id);

        if (optionalEvent.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Evento no encontrado");
        }

        EventModel founded = optionalEvent.get();

        return ResponseEntity.ok(EventMapper.eventModelToResponse.apply(founded));
    }

    @PostMapping
    public ResponseEntity<?> registerEvent(@Valid @RequestBody EventDTO event){

        EventModel model = EventMapper.eventDTORequestToModel.apply(event);
        Optional<EventModel> eventOpt = this.eventUseCases.registerEvent().apply(model);

        if(eventOpt.isEmpty()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Credenciales incorrectas");
        }

        EventModel saved = eventOpt.get();

        return ResponseEntity.status(HttpStatus.CREATED).body(EventMapper.eventModelToResponse.apply(saved));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateEvent(@PathVariable UUID id,@Valid @RequestBody EventDTO event){
        EventModel model = EventMapper.eventDTORequestToModel.apply(event);
        Optional<EventModel> eventOpt = this.eventUseCases.updateEventById().apply(id, model);

        if(eventOpt.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Evento no encontrado");
        }

        EventModel updated = eventOpt.get();

        return ResponseEntity.status(HttpStatus.OK).body(EventMapper.eventModelToResponse.apply(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteEvent(@PathVariable UUID id){
        Optional<String> eventOpt = this.eventUseCases.removeEvent().apply(id);

        if(eventOpt.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Event not found");
        }

        return ResponseEntity.status(HttpStatus.OK).body("Event removed successfully");
    }

    @PostMapping("/{id}/register")
    public ResponseEntity<?> registerUserToEvent(@PathVariable UUID id, @Valid @RequestBody registerUserToEventDTO user){
        Optional<String> stringOpt = this.eventUseCases.registerUserToEvent().apply(user.userId(), id);

        if(stringOpt.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Credenciales incorrectas");
        }

        return ResponseEntity.status(HttpStatus.OK).body(stringOpt.get());
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<?> getAllEventForUser(@PathVariable UUID id){
        Optional<Set<EventModel>> eventsOpt = this.eventUseCases.getAllEventByUserId().apply(id);

        if(eventsOpt.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        Set<EventModel> events = eventsOpt.get();

        return ResponseEntity.status(HttpStatus.OK).body(events.stream().map(EventMapper.eventModelToResponse));
    }
}
