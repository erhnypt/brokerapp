package com.brokerapp.controller;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import com.brokerapp.dto.JwtResponse;
import com.brokerapp.dto.LoginRequest;
import com.brokerapp.model.Admin;
import com.brokerapp.security.jwt.JwtUtils;
import com.brokerapp.service.AdminService;

/**
 * AuthController için unit testler
 */
class AuthControllerTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtUtils jwtUtils;

    @Mock
    private AdminService adminService;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private AuthController authController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Admin giriş işlemi başarılı olmalı")
    void authenticateUser_ShouldReturnJwtResponse() {
        // Arrange
        LoginRequest loginRequest = new LoginRequest("admin", "admin123");

        Admin admin = Admin.builder()
                .id(1L)
                .username("admin")
                .password("encodedPassword")
                .email("admin@brokerapp.com")
                .active(true)
                .build();

        String expectedToken = "mockJwtToken";

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(jwtUtils.generateJwtToken(authentication)).thenReturn(expectedToken);
        when(adminService.findByUsername("admin")).thenReturn(Optional.of(admin));

        // Act
        ResponseEntity<?> response = authController.authenticateUser(loginRequest);

        // Assert
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        JwtResponse jwtResponse = (JwtResponse) response.getBody();
        assertNotNull(jwtResponse);
        assertEquals(expectedToken, jwtResponse.getToken());
        assertEquals("admin", jwtResponse.getUsername());
        assertEquals(1L, jwtResponse.getId());

        // Verify
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtUtils).generateJwtToken(authentication);
        verify(adminService).findByUsername("admin");
    }
}
