package com.ias.eventManagerRun.app.config;

import com.ias.eventManagerRun.UserDataProvider;
import com.ias.eventManagerRun.infrastructure.driven_adapter.mysqlJpa.IUserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.cglib.core.Local;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class JwtServiceTest {

    @Mock
    private IUserRepository userRepository;


    @InjectMocks
    private JwtService jwtService;

    private String validToken;

    private static final SecretKey SECRET_KEY = Keys.hmacShaKeyFor("my-ultra-super-key-my-ultra-super-key".getBytes());

    @BeforeEach
    void init(){
        MockitoAnnotations.openMocks(this);

        validToken = Jwts.builder()
                .signWith(SECRET_KEY)
                .subject("Nicky")
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 3600000))
                .compact();
    }

    @AfterEach
    void clean() {
        reset(userRepository);
        SecurityContextHolder.clearContext(); // Esto limpia la autenticación entre tests
    }

    @Test
    public void testGenerateToken() {
        // Given
        String username = "nicky";

        // When
        String token = this.jwtService.generateToken(username);
        Claims claims = Jwts.parser().verifyWith(SECRET_KEY).build().parseSignedClaims(token).getPayload();

        // Then
        assertNotNull(token);
        assertEquals(claims.getSubject(), "nicky");
        assertTrue(claims.getExpiration().toInstant().isAfter(Instant.now()));
    }

    @Test
    public void testExtractUsername(){
        // Given
        String token = this.validToken;
        String name = "Nicky";
        //When
        String username = this.jwtService.extractUsername(token);

        // THen
        assertNotNull(username);
        assertEquals(username, name);
    }

    @Test
    void testDoFilterInternal_WithValidToken() throws ServletException, IOException, ServletException, IOException, ServletException, IOException {
        // Setup
        String jwt = this.validToken;
        String username = "Nicky";

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain filterChain = mock(FilterChain.class);

        when(request.getHeader("Authorization")).thenReturn("Bearer " + jwt);
        when(userRepository.findByUsername_Username(username)).thenReturn(
                Optional.of(UserDataProvider.getUserDBO())
        );
        // Execute
        this.jwtService.doFilterInternal(request, response, filterChain);

        // Verify
        verify(filterChain).doFilter(request, response);
        assertNotNull(SecurityContextHolder.getContext().getAuthentication());
        assertEquals(username, SecurityContextHolder.getContext().getAuthentication().getPrincipal());
    }

    @Test
    public void testDoFilterInternalAuthHeaderNull() throws ServletException, IOException {

        // When
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain filterChain = mock(FilterChain.class);


        //Then
        when(request.getHeader("Authorization")).thenReturn(null);
        this.jwtService.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
        assertNull(SecurityContextHolder.getContext().getAuthentication()); // No crea la sesión
    }

    @Test
    public void testDoFilterInternalAuthHeaderDoesntStartsWithBearer() throws ServletException, IOException {

        // When
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain filterChain = mock(FilterChain.class);


        //Then
        when(request.getHeader("Authorization")).thenReturn("Test");
        this.jwtService.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
        assertNull(SecurityContextHolder.getContext().getAuthentication()); // No crea la sesión
    }

    @Test
    public void testDoFilterInternalUserNotFound() throws ServletException, IOException {

        // Given
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain filterChain = mock(FilterChain.class);


        // when
        when(request.getHeader("Authorization")).thenReturn("Bearer " + this.validToken);
        when(this.userRepository.findByUsername_Username(anyString())).thenReturn(Optional.empty());

        // THen
        assertThrows(UsernameNotFoundException.class, () -> {
            this.jwtService.doFilterInternal(request, response, filterChain);
        });
    }

    @Test
    public void doFilterInternalWithUsernameNull() throws ServletException, IOException {
        // Given
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain filterChain = mock(FilterChain.class);

        String tokenSinSubject = Jwts.builder()
                .claim("role", "USER")
                .signWith(SECRET_KEY)
                .compact();

        when(request.getHeader("Authorization")).thenReturn("Bearer " + tokenSinSubject);

        // SUT: el filtro que estás probando

        // When: ejecutamos el filtro
        this.jwtService.doFilterInternal(request, response, filterChain);

        // Then: verificamos que sí se llamó a filterChain.doFilter
        verify(filterChain).doFilter(request, response);
        assertNull(SecurityContextHolder.getContext().getAuthentication()); // -> Se verifica que no haya creado la "sesion"
    }

    @Test
    public void doFilterInternalWithAuthenticationAlreadyPresent() throws ServletException, IOException {
        // Given
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain filterChain = mock(FilterChain.class);

        // Simular un token válido o inválido, no importa porque ya hay auth
        when(request.getHeader("Authorization")).thenReturn("Bearer " + this.validToken);

        // Simula que ya existe autenticación en el contexto
        Authentication mockAuth = mock(Authentication.class);
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(mockAuth);
        SecurityContextHolder.setContext(context);

        // When
        this.jwtService.doFilterInternal(request, response, filterChain);

        // Then
        verify(filterChain).doFilter(request, response); // Verifica que se dejó pasar
    }


}
