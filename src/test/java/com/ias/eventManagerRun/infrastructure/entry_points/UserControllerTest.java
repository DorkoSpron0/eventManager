package com.ias.eventManagerRun.infrastructure.entry_points;

import com.ias.eventManagerRun.app.config.JwtService;
import com.ias.eventManagerRun.domain.models.UserModel;
import com.ias.eventManagerRun.domain.models.ValueObjects.Password;
import com.ias.eventManagerRun.domain.models.ValueObjects.Username;
import com.ias.eventManagerRun.domain.usecases.UserUseCases;
import com.ias.eventManagerRun.infrastructure.entry_points.DTO.UserDTO;
import com.ias.eventManagerRun.infrastructure.mappers.UserMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@Import(UserControllerTest.MockConfig.class)
@AutoConfigureMockMvc(addFilters = false) // 👈 Esto desactiva los filtros de seguridad
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserUseCases userUseCases;

    @TestConfiguration
    static class MockConfig {
        @Bean
        public UserUseCases userUseCases() {
            return Mockito.mock(UserUseCases.class);
        }

        @Bean
        public JwtService jwtService() {
            return Mockito.mock(JwtService.class);
        }
    }

    @Test
    void testGetAllUser() throws Exception {
        UUID id = UUID.randomUUID();
        UserModel model = new UserModel(id, new Username("Dorko"), new Password("pass123"));

        // Simula retorno del mock
        when(userUseCases.getAllUsers()).thenReturn(List.of(model));

        // Ejecuta y captura respuesta
        mockMvc.perform(get("/iasapi/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].username").value("Dorko"))
                .andExpect(jsonPath("$[0].password").value("pass123"));
    }

    @Test
    public void testGetAllUserException() throws Exception {
        //Given

        // WHen
        when(this.userUseCases.getAllUsers()).thenThrow(new RuntimeException());

        // Then
        mockMvc.perform(get("/iasapi/users"))
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void testRegisterUser() throws Exception {
        //Given
        UserModel give = new UserModel(UUID.randomUUID(), new Username("Nicky"), new Password("Password123"), null);

        // When
        when(this.userUseCases.registerUser(any(UserModel.class))).thenReturn(give);

        // Then
        mockMvc.perform(post("/iasapi/users")
                        .contentType("application/json")
                        .content("""
                                    {
                                    "username": "NickyFF",
                                    "password": "Hola123@"
                                    }
                                """)
        ).andExpect(status().isCreated())
                .andExpect(jsonPath("$.username").value("Nicky"))
                .andExpect(jsonPath("$.password").value("Password123"));
    }

    @Test
    public void testRegisterUserException() throws Exception {
        //Given
        UserModel give = new UserModel(UUID.randomUUID(), new Username("Nicky"), new Password("Password123"), null);

        // When
        when(this.userUseCases.registerUser(any(UserModel.class))).thenThrow(new RuntimeException());

        // Then
        mockMvc.perform(post("/iasapi/users")
                        .contentType("application/json")
                        .content("""
                                    {
                                    "username": "NickyFF",
                                    "password": "Hola123@"
                                    }
                                """)
                )
                .andExpect(status().isInternalServerError());
    }
}
