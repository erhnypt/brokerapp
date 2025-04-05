package com.brokerapp.security.jwt;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.test.util.ReflectionTestUtils;

import com.brokerapp.model.Admin;
import com.brokerapp.model.Customer;
import com.brokerapp.security.services.AdminDetailsImpl;
import com.brokerapp.security.services.CustomerDetailsImpl;

/**
 * JwtUtils için unit testler
 */
class JwtUtilsTest {

    @Mock
    private Authentication authentication;

    @InjectMocks
    private JwtUtils jwtUtils;

    private AdminDetailsImpl adminDetails;
    private CustomerDetailsImpl customerDetails;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        ReflectionTestUtils.setField(jwtUtils, "jwtSecret", "testSecretKeyForJwtAuthenticationVeryLongAndSecure");
        ReflectionTestUtils.setField(jwtUtils, "jwtExpirationMs", 86400000);

        // Admin kullanıcı
        Admin admin = Admin.builder()
                .id(1L)
                .username("admin")
                .password("adminpass")
                .active(true)
                .build();
        adminDetails = AdminDetailsImpl.build(admin);

        // Müşteri kullanıcı
        Customer customer = Customer.builder()
                .id(1L)
                .username("musteri1")
                .password("musterisifre")
                .tryBalance(10000.0)
                .tryUsableBalance(10000.0)
                .active(true)
                .build();
        customerDetails = CustomerDetailsImpl.build(customer);
    }

    @Test
    @DisplayName("Admin için JWT token oluşturma ve doğrulama başarılı olmalı")
    void generateAndValidateJwtTokenForAdmin_ShouldReturnTrue() {
        // Arrange
        when(authentication.getPrincipal()).thenReturn(adminDetails);

        // Act
        String token = jwtUtils.generateJwtToken(authentication);
        boolean isValid = jwtUtils.validateJwtToken(token);
        String extractedUsername = jwtUtils.getUserNameFromJwtToken(token);
        String role = jwtUtils.getRoleFromJwtToken(token);

        // Assert
        assertNotNull(token);
        assertTrue(isValid);
        assertEquals("admin", extractedUsername);
        assertEquals("ROLE_ADMIN", role);
    }

    @Test
    @DisplayName("Müşteri için JWT token oluşturma ve doğrulama başarılı olmalı")
    void generateAndValidateJwtTokenForCustomer_ShouldReturnTrue() {
        // Arrange
        when(authentication.getPrincipal()).thenReturn(customerDetails);

        // Act
        String token = jwtUtils.generateJwtToken(authentication);
        boolean isValid = jwtUtils.validateJwtToken(token);
        String extractedUsername = jwtUtils.getUserNameFromJwtToken(token);
        String role = jwtUtils.getRoleFromJwtToken(token);

        // Assert
        assertNotNull(token);
        assertTrue(isValid);
        assertEquals("musteri1", extractedUsername);
        assertEquals("ROLE_CUSTOMER", role);
    }

    @Test
    @DisplayName("Geçersiz JWT token doğrulanmamalı")
    void validateJwtToken_WithInvalidToken_ShouldReturnFalse() {
        // Act & Assert
        assertFalse(jwtUtils.validateJwtToken("invalidToken"));
    }
}
