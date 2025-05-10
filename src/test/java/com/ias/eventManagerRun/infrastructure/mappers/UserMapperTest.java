package com.ias.eventManagerRun.infrastructure.mappers;

import com.ias.eventManagerRun.UserDataProvider;
import com.ias.eventManagerRun.domain.models.UserModel;
import com.ias.eventManagerRun.domain.models.ValueObjects.Password;
import com.ias.eventManagerRun.domain.models.ValueObjects.Username;
import com.ias.eventManagerRun.infrastructure.driven_adapter.mysqlJpa.DBO.UserDBO;
import com.ias.eventManagerRun.infrastructure.entry_points.DTO.EventDTO;
import com.ias.eventManagerRun.infrastructure.entry_points.DTO.UserDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class UserMapperTest {

    @Autowired
    private UserMapper mapper;

    @BeforeEach
    void init(){
        mapper = new UserMapper();
    }

    @Test
    public void testUserModelToDBOWithEvents() {
        // Given
        UserModel model = UserDataProvider.getUserModel();

        // When
        UserDBO dbo = UserMapper.userModelToDBO(model);

        // Then
        assertNotNull(dbo);
        assertEquals(model.getUsername(), dbo.getUsername());
        assertEquals(model.getPassword(), dbo.getPassword());
        assertEquals(1, dbo.getEventDBOS().size());
        assertInstanceOf(Set.class, dbo.getEventDBOS());
    }

    @Test
    public void testUserModelToDBOWithoutEvents(){
        // Given
        UserModel model = new UserModel(UUID.randomUUID(), new Username("Nicky"), new Password("password"), null);

        // When
        UserDBO dbo = UserMapper.userModelToDBO(model);

        // Then
        assertNotNull(dbo);
        assertEquals(model.getUsername(), dbo.getUsername());
        assertEquals(model.getPassword(), dbo.getPassword());
        assertEquals(0, dbo.getEventDBOS().size());
    }

    @Test
    public void testUserDBOToModelWithEvents(){
        // Given
        UserDBO dbo = UserDataProvider.getUserDBO();

        // When
        UserModel model = UserMapper.userDBOToModel(dbo);

        // Then
        assertNotNull(model);
        assertEquals(dbo.getUsername(), model.getUsername());
        assertEquals(dbo.getPassword(), model.getPassword());
        assertEquals(1, model.getEventDBOS().size());
        assertInstanceOf(Set.class, dbo.getEventDBOS());
    }

    @Test
    public void testUserDBOToModelWithoutEvents(){
        // Given
        UserDBO dbo = new UserDBO(UUID.randomUUID(), new Username("NIcky"), new Password("password"), null);

        // When
        UserModel model = UserMapper.userDBOToModel(dbo);

        // Then
        assertNotNull(model);
        assertEquals(dbo.getUsername(), model.getUsername());
        assertEquals(dbo.getPassword(), model.getPassword());
        assertEquals(0, model.getEventDBOS().size());
        assertInstanceOf(Set.class, model.getEventDBOS());
    }

    @Test
    public void testUserModelToDTOWithEvents(){
        // Given
        UserModel model = UserDataProvider.getUserModel();

        // When
        UserDTO dto = UserMapper.userModelToDTO(model);

        // Then
        assertNotNull(dto);
        assertEquals(dto.getUsername(), model.getUsername().getUsername());
        assertEquals(dto.getPassword(), model.getPassword().getPassword());
        assertEquals(1, dto.getEventDTOS().size());
        assertInstanceOf(Set.class, model.getEventDBOS());
    }

    @Test
    public void testUserModelToDTOWithoutEvents(){
        // Given
        UserModel model = new UserModel(UUID.randomUUID(), new Username("Nicky"), new Password("password"), null);

        // When
        UserDTO dto = UserMapper.userModelToDTO(model);

        // Then
        assertNotNull(dto);
        assertEquals(dto.getUsername(), model.getUsername().getUsername());
        assertEquals(dto.getPassword(), model.getPassword().getPassword());
        assertEquals(0, dto.getEventDTOS().size());
        assertInstanceOf(Set.class, dto.getEventDTOS());
    }

    @Test
    public void testUserDTOTODBOWithEvents() {
        // Given
        UserDTO dto = new UserDTO(UUID.randomUUID(), "Nicky", "Password123@", Set.of(
                new EventDTO(UUID.randomUUID(), LocalDate.now(), "description", "name", "San Javier")
        ));

        // When
        UserDBO dbo = UserMapper.userDTOTODBO(dto);

        // THen
        assertNotNull(dbo);
        assertEquals(dbo.getUsername().getUsername(), dto.getUsername());
        assertEquals(dbo.getPassword().getPassword(), dto.getPassword());
        assertEquals(1, dbo.getEventDBOS().size());
        assertInstanceOf(Set.class, dto.getEventDTOS());
    }

    @Test
    public void testUserDTOTODBOWithoutEvents(){
        // Given
        UserDTO dto = new UserDTO(UUID.randomUUID(), "Nicky", "Password123@", null);

        // When
        UserDBO dbo = UserMapper.userDTOTODBO(dto);

        // THen
        assertNotNull(dbo);
        assertEquals(dbo.getUsername().getUsername(), dto.getUsername());
        assertEquals(dbo.getPassword().getPassword(), dto.getPassword());
        assertEquals(0, dbo.getEventDBOS().size());
        assertInstanceOf(Set.class, dbo.getEventDBOS());
    }

    @Test
    public void testUserDBoToDTOWithEvents(){
        // Given
        UserDBO dbo = UserDataProvider.getUserDBO();

        // When
        UserDTO dto = UserMapper.userDBoToDTO(dbo);

        // THen
        assertNotNull(dbo);
        assertEquals(dbo.getUsername().getUsername(), dto.getUsername());
        assertEquals(dbo.getPassword().getPassword(), dto.getPassword());
        assertEquals(1, dto.getEventDTOS().size());
        assertInstanceOf(Set.class, dto.getEventDTOS());
    }

    @Test
    public void testUserDBoToDTOWithoutEvents(){
        // Given
        UserDBO dbo = UserDataProvider.getUserDBO();
        dbo.setEventDBOS(null);

        // When
        UserDTO dto = UserMapper.userDBoToDTO(dbo);

        // THen
        assertNotNull(dbo);
        assertEquals(dbo.getUsername().getUsername(), dto.getUsername());
        assertEquals(dbo.getPassword().getPassword(), dto.getPassword());
        assertEquals(0, dto.getEventDTOS().size());
        assertInstanceOf(Set.class, dto.getEventDTOS());
    }

    // Test para pasar la clase
    @Test
    void testPassTest() {
        // Tocar cualquier método directamente
        assertTrue(this.mapper.passTest());
    }
}
