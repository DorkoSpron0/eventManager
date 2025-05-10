package com.ias.eventManagerRun.infrastructure;

import com.ias.eventManagerRun.infrastructure.driven_adapter.mysqlJpa.DBO.UserDBO;
import com.ias.eventManagerRun.infrastructure.driven_adapter.mysqlJpa.IUserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@Sql("classpath:UserData.sql")
@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private IUserRepository userRepository;

    @Test
    void testPassTest(){
        assertNotNull(userRepository);
    }

    @Test
    void testFindByUsername(){
        // Given
        String username = "Nicky";

        // When
        Optional<UserDBO> user = this.userRepository.findByUsername_Username(username);
        System.out.println(user);

        // Then
        assertNotNull(user);
        assertTrue(user.isPresent());
        assertEquals(username, user.get().getUsername().getUsername());
    }

    @Test
    void testFindByUsernameNotFound(){
        // Given
        String username = "Alexander";

        // When
        Optional<UserDBO> user = this.userRepository.findByUsername_Username(username);

        // Then
        assertNotNull(user);
        assertTrue(user.isEmpty());
    }
}
