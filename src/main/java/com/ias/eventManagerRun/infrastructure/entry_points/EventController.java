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
        return ResponseEntity.status(HttpStatus.OK).body(this.eventUseCases.getAllEvents().stream().map(EventMapper.eventModelToResponse));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getEventById(@PathVariable UUID id) {
        EventModel optionalEvent = eventUseCases.getEventById().apply(id);

        return ResponseEntity.ok(EventMapper.eventModelToResponse.apply(optionalEvent));
    }

    @PostMapping
    public ResponseEntity<?> registerEvent(@Valid @RequestBody EventDTO event){

        EventModel model = EventMapper.eventDTORequestToModel.apply(event);
        EventModel eventOpt = this.eventUseCases.registerEvent().apply(model);

        return ResponseEntity.status(HttpStatus.CREATED).body(EventMapper.eventModelToResponse.apply(eventOpt));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateEvent(@PathVariable UUID id,@Valid @RequestBody EventDTO event){
        EventModel model = EventMapper.eventDTORequestToModel.apply(event);
        EventModel eventOpt = this.eventUseCases.updateEventById().apply(id, model);

        return ResponseEntity.status(HttpStatus.OK).body(EventMapper.eventModelToResponse.apply(eventOpt));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteEvent(@PathVariable UUID id){
        String eventOpt = this.eventUseCases.removeEvent().apply(id);

        return ResponseEntity.status(HttpStatus.OK).body(eventOpt);
    }

    @PostMapping("/{id}/register")
    public ResponseEntity<?> registerUserToEvent(@PathVariable UUID id, @Valid @RequestBody registerUserToEventDTO user){
        String stringOpt = this.eventUseCases.registerUserToEvent().apply(user.userId(), id);

        return ResponseEntity.status(HttpStatus.OK).body(stringOpt);
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<?> getAllEventForUser(@PathVariable UUID id){
        Set<EventModel> eventsOpt = this.eventUseCases.getAllEventByUserId().apply(id);

        return ResponseEntity.status(HttpStatus.OK).body(eventsOpt.stream().map(EventMapper.eventModelToResponse));
    }
}
