package com.ias.eventManagerRun.infrastructure.driven_adapter.mysqlJpa.adapters;

import com.ias.eventManagerRun.EventDataProvider;
import com.ias.eventManagerRun.domain.models.EventModel;
import com.ias.eventManagerRun.domain.models.UserModel;
import com.ias.eventManagerRun.domain.models.ValueObjects.EventDescription;
import com.ias.eventManagerRun.domain.models.ValueObjects.EventName;
import com.ias.eventManagerRun.domain.models.ValueObjects.Password;
import com.ias.eventManagerRun.domain.models.ValueObjects.Username;
import com.ias.eventManagerRun.infrastructure.driven_adapter.mysqlJpa.DBO.EventDBO;
import com.ias.eventManagerRun.infrastructure.driven_adapter.mysqlJpa.DBO.UserDBO;
import com.ias.eventManagerRun.infrastructure.driven_adapter.mysqlJpa.IEventRepository;
import com.ias.eventManagerRun.infrastructure.driven_adapter.mysqlJpa.IUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class IEventRepositoryAdapterTest {

    @Mock
    private IEventRepository eventRepository;

    @Mock
    private IUserRepositoryAdapter userRepositoryAdapter;

    @InjectMocks
    private IEventRepositoryAdapter eventRepositoryAdapter;

    @BeforeEach
    void init(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllEvents(){
        // Given

        // When
        when(eventRepository.findAll()).thenReturn(EventDataProvider.getAllEvents());

        List<EventModel> eventModels = this.eventRepositoryAdapter.getAllEvents();
        // Then
        assertNotNull(eventModels);
        assertFalse(eventModels.isEmpty());
        assertEquals(3, eventModels.size());

        assertEquals("Nombre1", eventModels.getFirst().getName().getName());
        assertEquals("Description1", eventModels.getFirst().getDescription().getDescription());
        assertEquals("San javier", eventModels.getFirst().getPlace());
    }

    @Test
    public void testRegisterEvent(){
        // Given
        UUID eventId = UUID.randomUUID();
        EventModel eventModel = new EventModel(
                eventId,
                new EventName("Event new"),
                new EventDescription("descriptionWow"),
                LocalDate.now(),
                "San Javier",
                new HashSet<>()
        );

        EventDBO savedDbo = new EventDBO(eventId, new EventName("Event new"), new EventDescription("descriptionWow"), "San Javier", LocalDate.now(), new HashSet<>());

        when(eventRepository.save(any(EventDBO.class))).thenReturn(savedDbo);

        // When
        EventModel result = eventRepositoryAdapter.registerEvent(eventModel);

        // Then
        assertNotNull(result);
        assertEquals("Event new", result.getName().getName());
    }

    @Test
    public void testGetEventById(){
        // Given
        UUID id = UUID.randomUUID();

        // When
        when(eventRepository.findById( any(UUID.class) )).thenReturn(
                Optional.of(new EventDBO(id, new EventName("Event new"), new EventDescription("descriptionWow"), "San Javier", LocalDate.now(), new HashSet<>()))
        );
        EventModel eventModel = this.eventRepositoryAdapter.getEventById(id);

        // Then
        assertNotNull(eventModel);
        assertEquals("Event new", eventModel.getName().getName());
    }

    @Test
    public void testGetEventByIdException(){
        // Given
        UUID id = UUID.randomUUID();

        // Then
        assertThrows(IllegalArgumentException.class, () -> {
            this.eventRepositoryAdapter.getEventById(any(UUID.class));
        });
    }

    @Test
    public void testUpdateEventById() {
        // Given
        UUID eventId = UUID.randomUUID();
        EventModel eventModel = new EventModel(eventId, new EventName("Event new"), new EventDescription("descriptionWow"), LocalDate.now(), "San Javier", new HashSet<>());

        // When
        when(this.eventRepository.findById( any(UUID.class) )).thenReturn(
                Optional.of(new EventDBO(eventId, new EventName("Eveeeeeeeent"), new EventDescription("descriptionWow2"), "Manrique", LocalDate.now(), new HashSet<>()))
        );

        when(this.eventRepository.save( any(EventDBO.class) )).thenReturn(new EventDBO(eventId, new EventName("Event new"), new EventDescription("descriptionWow"), "San Javier", LocalDate.now(), new HashSet<>()));

        EventModel eventUpdated = this.eventRepositoryAdapter.updateEventById(eventId, eventModel);

        // Then
        assertNotNull(eventUpdated);
        assertEquals(eventId, eventUpdated.getId());
        assertEquals("Event new", eventUpdated.getName().getName());
        assertEquals("descriptionWow", eventUpdated.getDescription().getDescription());
    }

    @Test
    public void testUpdateEventByIdException(){
        // Given
        UUID eventId = UUID.randomUUID();
        EventModel eventModel = new EventModel(eventId, new EventName("Event new"), new EventDescription("descriptionWow"), LocalDate.now(), "San Javier", new HashSet<>());
        // Then
        assertThrows(IllegalArgumentException.class, () -> {
           this.eventRepositoryAdapter.updateEventById(eventId, eventModel);
        });
    }

    @Test
    public void testRegisterUserToEvent(){
        // Given
        UUID eventId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();

        // When
        when(eventRepository.findById( any(UUID.class) )).thenReturn(
                Optional.of(new EventDBO(eventId, new EventName("Eveeeeeeeent"), new EventDescription("descriptionWow2"), "Manrique", LocalDate.now(), new HashSet<>()))
        );
        when(userRepositoryAdapter.findById( any(UUID.class) )).thenReturn(
               new UserModel(userId, new Username("Dorko"), new Password("Password"), new HashSet<>())
        );

        String resultado = this.eventRepositoryAdapter.registerUserToEvent(eventId, userId);

        // Then
        assertNotNull(resultado);
        assertFalse(resultado.isEmpty());
        assertEquals("User with id " + userId + " added to event with id " + eventId + " successfully", resultado);
    }

    @Test
    public void testRegisterUserToEventUserNotFound(){
        // Given
        UUID eventId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();

        // When
        when(eventRepository.findById( any(UUID.class) )).thenReturn(
                Optional.of(new EventDBO(eventId, new EventName("Eveeeeeeeent"), new EventDescription("descriptionWow2"), "Manrique", LocalDate.now(), new HashSet<>()))
        );
        when(userRepositoryAdapter.findById(userId)).thenThrow(new IllegalArgumentException("User not found")); // SE TIENE QUE MOCKEAR EL COMPÓRTAMIENTO, EL MOCK SOLO SIEMPRE DEVUELVE NULL

        assertThrows(IllegalArgumentException.class, () -> {
            this.eventRepositoryAdapter.registerUserToEvent(eventId, userId);
        });
    }

    @Test
    public void testRegisterUserToEventEventNotFound(){
        // Given
        UUID eventId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();

        // When
        when(eventRepository.findById( any(UUID.class) )).thenThrow( // SE TIENE QUE MOCKEAR EL COMPÓRTAMIENTO, EL MOCK SOLO SIEMPRE DEVUELVE NULL
                new IllegalArgumentException("Event not found")
        );

        when(userRepositoryAdapter.findById( any(UUID.class) )).thenReturn(
                new UserModel(userId, new Username("Dorko"), new Password("Password"), new HashSet<>())
        );

        assertThrows(IllegalArgumentException.class, () -> {
            this.eventRepositoryAdapter.registerUserToEvent(eventId, userId);
        });
    }

    @Test
    public void testGetAllEventByUser(){
        // Given
        UUID userId = UUID.randomUUID();

        // When
        when(userRepositoryAdapter.findById(any(UUID.class))).thenReturn(
          new UserModel(userId, new Username("nicky"), new Password("password"), new HashSet<>(Set.of(
                  new EventModel(UUID.randomUUID(), new EventName("Evebnt"), new EventDescription("wow"), LocalDate.now(), "San javier")
          )))
        );

        Set<EventModel> eventModels = this.eventRepositoryAdapter.getAllEventByUser(userId);

        assertNotNull(eventModels);
        assertEquals(1, eventModels.size());
        assertTrue(eventModels.stream().anyMatch(eventModel -> eventModel.getName().getName().equals("Evebnt")));
    }

    @Test
    public void testRemoveEvent(){
        // Given
        UUID eventId = UUID.randomUUID();

        // When
        when(eventRepository.findById(any(UUID.class))).thenReturn(
                Optional.of(new EventDBO(UUID.randomUUID(), new EventName("Event"), new EventDescription("wow"),"San javier", LocalDate.now(), new HashSet<>()))
        );

        String resultado = this.eventRepositoryAdapter.removeEvent(eventId);

        // Then
        assertNotNull(resultado);
        assertEquals("Event with id " + eventId + " removed successfully", resultado);
    }

    @Test
    public void testRemoveEventException(){
        // Given
        UUID eventId = UUID.randomUUID();

        // When
        when(eventRepository.findById(any(UUID.class))).thenThrow(new IllegalArgumentException("Event Not found"));

        // Then
        assertThrows(IllegalArgumentException.class, () -> {
           eventRepository.findById(eventId);
        });
    }
}
