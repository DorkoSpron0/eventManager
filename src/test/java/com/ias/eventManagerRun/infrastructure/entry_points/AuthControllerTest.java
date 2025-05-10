package com.ias.eventManagerRun.infrastructure.entry_points;

import com.ias.eventManagerRun.UserDataProvider;
import com.ias.eventManagerRun.app.config.JwtService;
import com.ias.eventManagerRun.domain.models.UserModel;
import com.ias.eventManagerRun.infrastructure.driven_adapter.mysqlJpa.DBO.UserDBO;
import com.ias.eventManagerRun.infrastructure.driven_adapter.mysqlJpa.adapters.IUserRepositoryAdapter;
import com.ias.eventManagerRun.infrastructure.entry_points.DTO.UserDTO;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false) // 👈 Esto desactiva los filtros de seguridad
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private IUserRepositoryAdapter adapter;

    @TestConfiguration
    static class MockConfig {
        @Bean
        public IUserRepositoryAdapter userRepositoryAdapter() {
            return Mockito.mock(IUserRepositoryAdapter.class);
        }

        @Bean
        public JwtService jwtService() {
            return Mockito.mock(JwtService.class);
        }

        @Bean
        public PasswordEncoder passwordEncoder(){
            return Mockito.mock(PasswordEncoder.class);
        }

    }

    @Test
    public void testLoginUser() throws Exception {
        // Given
        UserDBO give = UserDataProvider.getUserDBO();

        // When
        when(this.adapter.loginUser(any(UserModel.class))).thenReturn("valid-jwt-token");

        // THen
        mockMvc.perform(post("/iasapi/auth/login")
                .contentType("application/json")
                .content("""
                        {
                          "username": "NickyFF",
                          "password": "Hola123@"
                        }
                        """)
        )
        .andExpect(status().isOk());
    }

    @Test
    public void testLoginUserException() throws Exception {
        // Given
        UserDBO give = UserDataProvider.getUserDBO();

        // When
        when(this.adapter.loginUser(any(UserModel.class))).thenThrow(new RuntimeException());

        // THen
        mockMvc.perform(post("/iasapi/auth/login")
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
