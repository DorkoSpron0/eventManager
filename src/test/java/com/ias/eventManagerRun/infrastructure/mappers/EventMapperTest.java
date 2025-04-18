package com.ias.eventManagerRun.infrastructure.mappers;

import com.ias.eventManagerRun.EventDataProvider;
import com.ias.eventManagerRun.domain.models.EventModel;
import com.ias.eventManagerRun.infrastructure.driven_adapter.mysqlJpa.DBO.EventDBO;
import com.ias.eventManagerRun.infrastructure.entry_points.DTO.EventDTO;
import com.ias.eventManagerRun.infrastructure.entry_points.DTO.UserDTO;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class EventMapperTest {

    @Test
    public void testEventDBOToModelWithUsers(){
        //Given
        EventDBO given = EventDataProvider.getEventDBO();

        // When
        EventModel result = EventMapper.eventDBOToModel(given);

        // Then
        assertNotNull(result);
        assertEquals(given.getId(), result.getId());
        assertEquals(given.getName(), result.getName());
        assertEquals(given.getDescription(), result.getDescription());
        assertEquals(given.getPlace(), result.getPlace());

        assertEquals(1, result.getUserSet().size());
    }

    @Test
    public void testEventDBOToModelWithoutUsers(){
        //Given
        EventDBO given = EventDataProvider.getEventDBO();
        given.setUserSet(null);
        // When
        EventModel result = EventMapper.eventDBOToModel(given);

        // Then
        assertNotNull(result);
        assertEquals(given.getId(), result.getId());
        assertEquals(given.getName(), result.getName());
        assertEquals(given.getDescription(), result.getDescription());
        assertEquals(given.getPlace(), result.getPlace());

        assertEquals(0, result.getUserSet().size());
    }

    @Test
    public void testEventModelToDBOWithUsers(){
        // Given
        EventModel given = EventDataProvider.getEventModel();

        // When
        EventDBO result = EventMapper.eventModelToDBO(given);

        // Then
        assertNotNull(result);
        assertEquals(given.getId(), result.getId());
        assertEquals(given.getName(), result.getName());
        assertEquals(given.getDescription(), result.getDescription());
        assertEquals(given.getPlace(), result.getPlace());

        assertEquals(1, result.getUserSet().size());
    }

    @Test
    public void testEventModelToDBOWithoutUsers(){
        // Given
        EventModel given = EventDataProvider.getEventModel();
        given.setUserSet(null);

        // When
        EventDBO result = EventMapper.eventModelToDBO(given);

        // Then
        assertNotNull(result);
        assertEquals(given.getId(), result.getId());
        assertEquals(given.getName(), result.getName());
        assertEquals(given.getDescription(), result.getDescription());
        assertEquals(given.getPlace(), result.getPlace());

        assertEquals(0, result.getUserSet().size());
        assertInstanceOf(Set.class, result.getUserSet());
    }

    @Test
    public void testEventModelTODTOWithUsers(){
        // Given
        EventModel given = EventDataProvider.getEventModel();

        // When
        EventDTO result = EventMapper.eventModelTODTO(given);

        // Then
        assertNotNull(result);
        assertEquals(given.getId(), result.getId());
        assertEquals(given.getName().getName(), result.getName());
        assertEquals(given.getDescription().getDescription(), result.getDescription());
        assertEquals(given.getPlace(), result.getPlace());

        assertEquals(1, result.getUsers().size());
        assertInstanceOf(List.class, result.getUsers());
    }

    @Test
    public void testEventModelTODTOWithoutUsers(){
        // Given
        EventModel given = EventDataProvider.getEventModel();
        given.setUserSet(null);
        // When
        EventDTO result = EventMapper.eventModelTODTO(given);

        // Then
        assertNotNull(result);
        assertEquals(given.getId(), result.getId());
        assertEquals(given.getName().getName(), result.getName());
        assertEquals(given.getDescription().getDescription(), result.getDescription());
        assertEquals(given.getPlace(), result.getPlace());

        assertEquals(0, result.getUsers().size());
        assertInstanceOf(List.class, result.getUsers());
    }

    @Test
    public void testEventDTOToDBOWithUsers(){
        // Given
        EventDTO given = new EventDTO(UUID.randomUUID(), "name", "description", "San javier", LocalDate.now(), List.of(
                new UserDTO(UUID.randomUUID(), "Nicky", "Password123@", null)
        ));

        // When
        EventDBO result = EventMapper.eventDTOToDBO(given);

        // Then
        assertNotNull(result);
        assertEquals(given.getId(), result.getId());
        assertEquals(given.getName(), result.getName().getName());
        assertEquals(given.getDescription(), result.getDescription().getDescription());
        assertEquals(given.getPlace(), result.getPlace());

        assertEquals(1, result.getUserSet().size());
        assertInstanceOf(Set.class, result.getUserSet());
    }

    @Test
    public void testEventDTOToDBOWithoutUsers(){
        // Given
        EventDTO given = new EventDTO(UUID.randomUUID(), "name", "description", "San javier", LocalDate.now(), null);

        // When
        EventDBO result = EventMapper.eventDTOToDBO(given);

        // Then
        assertNotNull(result);
        assertEquals(given.getId(), result.getId());
        assertEquals(given.getName(), result.getName().getName());
        assertEquals(given.getDescription(), result.getDescription().getDescription());
        assertEquals(given.getPlace(), result.getPlace());

        assertEquals(0, result.getUserSet().size());
        assertInstanceOf(Set.class, result.getUserSet());
    }

    @Test
    public void testEventDBOToDTOWithUsers(){
        // Given
        EventDBO given = EventDataProvider.getEventDBO();

        // When
        EventDTO result = EventMapper.eventDBOToDTO(given);

        // Then
        assertNotNull(result);
        assertEquals(given.getId(), result.getId());
        assertEquals(given.getName().getName(), result.getName());
        assertEquals(given.getDescription().getDescription(), result.getDescription());
        assertEquals(given.getPlace(), result.getPlace());

        assertEquals(1, result.getUsers().size());
        assertInstanceOf(List.class, result.getUsers());
    }

    @Test
    public void testEventDBOToDTOWithoutUsers(){
        // Given
        EventDBO given = EventDataProvider.getEventDBO();
        given.setUserSet(null);
        // When
        EventDTO result = EventMapper.eventDBOToDTO(given);

        // Then
        assertNotNull(result);
        assertEquals(given.getId(), result.getId());
        assertEquals(given.getName().getName(), result.getName());
        assertEquals(given.getDescription().getDescription(), result.getDescription());
        assertEquals(given.getPlace(), result.getPlace());

        assertEquals(0, result.getUsers().size());
        assertInstanceOf(List.class, result.getUsers());
    }
}
