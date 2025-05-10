package com.ias.eventManagerRun.infrastructure.entry_points;

import com.ias.eventManagerRun.EventDataProvider;
import com.ias.eventManagerRun.app.config.JwtService;
import com.ias.eventManagerRun.domain.models.EventModel;
import com.ias.eventManagerRun.domain.models.ValueObjects.EventDescription;
import com.ias.eventManagerRun.domain.models.ValueObjects.EventName;
import com.ias.eventManagerRun.infrastructure.driven_adapter.mysqlJpa.adapters.IEventRepositoryAdapter;
import com.ias.eventManagerRun.infrastructure.driven_adapter.mysqlJpa.adapters.IUserRepositoryAdapter;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(EventController.class)
@AutoConfigureMockMvc(addFilters = false) // 👈 Esto desactiva los filtros de seguridad
public class EventControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private IEventRepositoryAdapter adapter;

    @TestConfiguration
    static class MockConfig {
        @Bean
        public IEventRepositoryAdapter adapter() {
            return Mockito.mock(IEventRepositoryAdapter.class);
        }

        @Bean
        public IUserRepositoryAdapter userAdapter() {
            return Mockito.mock(IUserRepositoryAdapter.class);
        }

        @Bean
        public JwtService jwtService() {
            return Mockito.mock(JwtService.class);
        }

        @Bean
        public PasswordEncoder passwordEncoder() {
            return Mockito.mock(PasswordEncoder.class);
        }
    }

    @AfterEach
    void resetMocks() {
        Mockito.reset(adapter); // ⚠️ resetea el estado del mock después de cada test
    }

    @Test
    public void testGetAllEvents() throws Exception {
        // Given

        // WHen
        when(this.adapter.getAllEvents()).thenReturn(
                List.of(
                        new EventModel(UUID.randomUUID(), new EventName("event"), new EventDescription("description"), LocalDate.now(), "San Javier")
                )
        );

        // Then
        mockMvc.perform(get("/iasapi/events"))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetAllEventsException() throws Exception {
        // Given

        // WHen
        when(this.adapter.getAllEvents()).thenThrow(new RuntimeException());

        // Then
        mockMvc.perform(get("/iasapi/events"))
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void testGetEventById() throws Exception {
        // Given

        UUID eventId = UUID.randomUUID();
        // When
        when(this.adapter.getEventById(any(UUID.class))).thenReturn(
                EventDataProvider.getEventModel()
        );

        // Then
        mockMvc.perform(get("/iasapi/events/{id}", eventId))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetEventByIdNotFound() throws Exception {
        // Given

        UUID eventId = UUID.randomUUID();
        // When
        when(this.adapter.getEventById(any(UUID.class))).thenThrow(new IllegalArgumentException());

        // Then
        mockMvc.perform(get("/iasapi/events/{id}", eventId))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testGetEventByIdException() throws Exception {
        // Given

        UUID eventId = UUID.randomUUID();
        // When
        when(this.adapter.getEventById(any(UUID.class))).thenThrow(new RuntimeException());

        // Then
        mockMvc.perform(get("/iasapi/events/{id}", eventId))
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void testRegisterEvent() throws Exception {
        // Given

        // When
        when(this.adapter.registerEvent(any(EventModel.class))).thenReturn(EventDataProvider.getEventModel());

        // Then
        mockMvc.perform(post("/iasapi/events")
                        .contentType("application/json")
                        .content("""
                                {
                                  "name": "Test01",
                                  "description": "INCREIBLE",
                                  "place": "MI CASA",
                                  "date": "2025-03-01"
                                }
                                """)
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Test01"))
                .andExpect(jsonPath("$.description").value("INCREIBLE"));
    }

    @Test
    public void testRegisterEventException() throws Exception {
        // Given

        // When
        when(this.adapter.registerEvent(any(EventModel.class))).thenThrow(new RuntimeException());

        // Then
        mockMvc.perform(post("/iasapi/events")
                        .contentType("application/json")
                        .content("""
                                {
                                  "name": "Test01",
                                  "description": "INCREIBLE",
                                  "place": "MI CASA",
                                  "date": "2025-03-01"
                                }
                                """)
                )
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void testUpdateEvent() throws Exception {
        //Given
        UUID eventId = UUID.randomUUID();

        // When
        when(this.adapter.updateEventById(any(UUID.class), any(EventModel.class))).thenReturn(
                new EventModel(eventId, new EventName("Change EventName"), new EventDescription("No importa"), LocalDate.of(2025, 2, 12), "San javier", null)
        );

        // Then
        mockMvc.perform(put("/iasapi/events/{id}", eventId)
                        .contentType("application/json")
                        .content("""
                                {
                                  "name": "Change EventName",
                                  "description": "No importa",
                                  "place": "San javier",
                                  "date": "2025-02-12"
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Change EventName"))
                .andExpect(jsonPath("$.description").value("No importa"))
                .andExpect(jsonPath("$.id").value(eventId.toString()));
    }

    @Test
    public void testUpdateEventNotFound() throws Exception {
        //Given
        UUID eventId = UUID.randomUUID();

        // When
        when(this.adapter.updateEventById(any(UUID.class), any(EventModel.class))).thenThrow(new IllegalArgumentException());

        // Then
        mockMvc.perform(put("/iasapi/events/{id}", eventId)
                        .contentType("application/json")
                        .content("""
                                {
                                  "name": "Change EventName",
                                  "description": "No importa",
                                  "place": "San javier",
                                  "date": "2025-02-12"
                                }
                                """))
                .andExpect(status().isNotFound());
    }
    @Test
    public void testUpdateEventException() throws Exception {
        //Given
        UUID eventId = UUID.randomUUID();

        // When
        when(this.adapter.updateEventById(any(UUID.class), any(EventModel.class))).thenThrow(new RuntimeException());

        // Then
        mockMvc.perform(put("/iasapi/events/{id}", eventId)
                        .contentType("application/json")
                        .content("""
                                {
                                  "name": "Change EventName",
                                  "description": "No importa",
                                  "place": "San javier",
                                  "date": "2025-02-12"
                                }
                                """))
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void testDeleteEvent() throws Exception {
        // Given
        UUID eventId = UUID.randomUUID();

        // When
        when(this.adapter.removeEvent(any(UUID.class))).thenReturn("Event with id " + eventId + " removed successfully");

        // Then
        mockMvc.perform(delete("/iasapi/events/{id}", eventId))
                .andExpect(status().isOk())
                .andExpect(content().string("Event with id " + eventId + " removed successfully"));
    }

    @Test
    public void testDeleteEventNotFound() throws Exception {
        // Given
        UUID eventId = UUID.randomUUID();

        // When
        when(this.adapter.removeEvent(any(UUID.class))).thenThrow(new IllegalArgumentException());

        // Then
        mockMvc.perform(delete("/iasapi/events/{id}", eventId))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testDeleteEventException() throws Exception {
        // Given
        UUID eventId = UUID.randomUUID();

        // When
        when(this.adapter.removeEvent(any(UUID.class))).thenThrow(new RuntimeException());

        // Then
        mockMvc.perform(delete("/iasapi/events/{id}", eventId))
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void testRegisterUserToEvent() throws Exception {
        // Given
        UUID userId = UUID.randomUUID();
        UUID eventId = UUID.randomUUID();

        // When
        when(this.adapter.registerUserToEvent(any(UUID.class), any(UUID.class))).thenReturn("User with id " + userId + " added to event with id " + eventId + " successfully");

        // Then
        mockMvc.perform(post("/iasapi/events/{id}/register", eventId)
                .contentType("application/json")
                .content(String.format("""
                        {
                        "userId": "%s"
                        }
                        """, userId.toString())))
                .andExpect(status().isCreated())
                .andExpect(content().string("User with id " + userId + " added to event with id " + eventId + " successfully"));
    }

    @Test
    public void testRegisterUserToEventNotFound() throws Exception {
        // Given
        UUID userId = UUID.randomUUID();
        UUID eventId = UUID.randomUUID();

        // When
        when(this.adapter.registerUserToEvent(any(UUID.class), any(UUID.class))).thenThrow(new IllegalArgumentException());

        // Then
        mockMvc.perform(post("/iasapi/events/{id}/register", eventId)
                        .contentType("application/json")
                        .content(String.format("""
                        {
                        "userId": "%s"
                        }
                        """, userId.toString())))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testRegisterUserToEventException() throws Exception {
        // Given
        UUID userId = UUID.randomUUID();
        UUID eventId = UUID.randomUUID();

        // When
        when(this.adapter.registerUserToEvent(any(UUID.class), any(UUID.class))).thenThrow(new RuntimeException());

        // Then
        mockMvc.perform(post("/iasapi/events/{id}/register", eventId)
                        .contentType("application/json")
                        .content(String.format("""
                        {
                        "userId": "%s"
                        }
                        """, userId.toString())))
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void testGetAllEventForUser() throws Exception {
        // Given
        UUID userId = UUID.randomUUID();

        // When
        when(this.adapter.getAllEventByUser(any(UUID.class))).thenReturn(
                Set.of(
                        new EventModel(UUID.randomUUID(), new EventName("Name"), new EventDescription("Description"), LocalDate.now(), "San Javier"),
                        new EventModel(UUID.randomUUID(), new EventName("Name2"), new EventDescription("Description2"), LocalDate.now(), "San Javier2")
                )
        );

        // Then
        mockMvc.perform(get("/iasapi/events/user/{id}", userId))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetAllEventForUserNotFound() throws Exception {
        // Given
        UUID userId = UUID.randomUUID();

        // When
        when(this.adapter.getAllEventByUser(any(UUID.class))).thenThrow(new IllegalArgumentException());

        // Then
        mockMvc.perform(get("/iasapi/events/user/{id}", userId))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testGetAllEventForUserException() throws Exception {
        // Given
        UUID userId = UUID.randomUUID();

        // When
        when(this.adapter.getAllEventByUser(any(UUID.class))).thenThrow(new RuntimeException());

        // Then
        mockMvc.perform(get("/iasapi/events/user/{id}", userId))
                .andExpect(status().isInternalServerError());
    }
}
