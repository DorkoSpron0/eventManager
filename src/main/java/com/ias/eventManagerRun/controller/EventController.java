package com.ias.eventManagerRun.controller;

import com.ias.eventManagerRun.controller.DTO.registerUserToEventDTO;
import com.ias.eventManagerRun.domain.models.Event;
import com.ias.eventManagerRun.services.EventService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@RestController
@RequestMapping("/iasapi/events")
@AllArgsConstructor
public class EventController {

    private EventService eventService;

    @GetMapping("")
    public List<Event> getAllEvents(){
        return eventService.getAllEvents();
    }

    @GetMapping("/{id}")
    public Event getEventById(@PathVariable UUID id){
        return eventService.getEventById(id);
    }

    @PostMapping
    public Event registerEvent(@RequestBody Event event){
        return eventService.registerEvent(event);
    }

    @PutMapping("/{id}")
    public Event updateEvent(@PathVariable UUID id, @RequestBody Event event){
        return eventService.updateEventById(id, event);
    }

    @DeleteMapping("/{id}")
    public String deleteEvent(@PathVariable UUID id){
        return eventService.removeEvent(id);
    }

    @PostMapping("/{id}/register")
    public String registerUserToEvent(@PathVariable UUID id, @RequestBody registerUserToEventDTO user){
        return eventService.registerUserToEvent(id, user.getUserId());
    }

    @GetMapping("/user/{id}")
    public Set<Event> getAllEventForUser(@PathVariable UUID id){
        return eventService.getAllEventByUser(id);
    }
}
