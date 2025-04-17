package com.ias.eventManagerRun.infrastructure.driven_adapter.mysqlJpa.adapters;

import com.ias.eventManagerRun.UserDataProvider;
import com.ias.eventManagerRun.app.config.JwtService;
import com.ias.eventManagerRun.domain.models.UserModel;
import com.ias.eventManagerRun.infrastructure.driven_adapter.mysqlJpa.DBO.UserDBO;
import com.ias.eventManagerRun.infrastructure.driven_adapter.mysqlJpa.IUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;

public class IUserRepositoryAdapterTest {

    @Mock
    private IUserRepository userRepository;

    @Mock
    private JwtService jwtService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private IUserRepositoryAdapter userRepositoryAdapter;

    @BeforeEach
    void init(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllUsers(){

        // When
        when(userRepository.findAll()).thenReturn(UserDataProvider.getAllUsers());
        List< UserModel> users = this.userRepositoryAdapter.getAllUsers();

        // then
        assertNotNull(users);
        assertFalse(users.isEmpty());

        assertEquals(6, users.size());
        assertEquals("Dorko", users.getFirst().getUsername().getUsername());
    }

    @Test
    public void testRegisterUser(){
        // Give
        UserModel userModel = UserDataProvider.getUserModel();

        // When
        when(userRepository.save( any(UserDBO.class) )).thenReturn(UserDataProvider.getUserDBO());
        UserModel  userSaved = this.userRepositoryAdapter.registerUser(userModel);

        // then
        assertNotNull(userSaved);
        assertEquals("Nicky", userSaved.getUsername().getUsername());
        assertEquals("Password123", userSaved.getPassword().getPassword());
    }

    @Test
    public void testFindById(){
        // Given
        UUID userId = UUID.randomUUID();

        // When
        when(userRepository.findById( any(UUID.class) )).thenReturn(Optional.of(UserDataProvider.getUserDBO()));
        UserModel eventFounded = this.userRepositoryAdapter.findById(userId);

        //Then
        assertNotNull(eventFounded);
        assertEquals("Nicky", eventFounded.getUsername().getUsername());
        assertEquals("Password123", eventFounded.getPassword().getPassword());
    }

    @Test
    public void testFindByIdException(){
        // Given
        UUID userId = UUID.randomUUID();

        assertThrows(IllegalArgumentException.class, () -> {
           this.userRepositoryAdapter.findById(userId);
        });
    }

    @Test
    public void testLoginUser(){
        //Given
        UserModel model = UserDataProvider.getUserModel();
        // When
        when(this.userRepository.findByUsername_Username( anyString() )).thenReturn(Optional.of(UserDataProvider.getUserDBO()));
        when(this.jwtService.generateToken( anyString() )).thenReturn(UserDataProvider.getToken());
        when(this.passwordEncoder.matches(anyString(), anyString())).thenReturn(true);

        String token = this.userRepositoryAdapter.loginUser(model);

        // Then
        assertNotNull(token);
        assertFalse(token.isEmpty());

        assertEquals("eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJOaWNreUZGIiwiaWF0IjoxNzQ0ODMyNTUxLCJleHAiOjE3NDQ4MzYxNTF9.RLen7-V-WxczuFDWJRipSkTdRXXSHivqbAvh-g1rfH4", token);
    }

    @Test
    public void testLoginUserUserNotFound(){
        // Given
        UserModel model = UserDataProvider.getUserModel();

        assertThrows(UsernameNotFoundException.class, () -> {
            this.userRepositoryAdapter.loginUser(model);
        });
    }

    @Test
    public void testLoginPasswordDoesntMatch(){
        //Given
        UserModel model = UserDataProvider.getUserModel();
        // When
        when(this.userRepository.findByUsername_Username( anyString() )).thenReturn(Optional.of(UserDataProvider.getUserDBO()));
        when(this.passwordEncoder.matches(anyString(), anyString())).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () -> {
            this.userRepositoryAdapter.loginUser(model);
        });
    }

    @Test
    public void testFindByUsername(){
        // Given
        String username = "Nicky";

        // When
        when(this.userRepository.findByUsername_Username( anyString() )).thenReturn(Optional.of(UserDataProvider.getUserDBO()));
        UserModel model = this.userRepositoryAdapter.findByUsername(username);
        // Then
        assertNotNull(model);
        assertEquals("Nicky", model.getUsername().getUsername());
        assertEquals("Password123", model.getPassword().getPassword());
    }

    @Test
    public void testFindByUsernameNotFound(){
        // Given
        String username = "Nicky";

        // Then
       assertThrows(UsernameNotFoundException.class, () -> this.userRepositoryAdapter.findByUsername(username));
    }
}
