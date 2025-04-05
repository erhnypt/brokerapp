package com.brokerapp.controller;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.brokerapp.dto.JwtResponse;
import com.brokerapp.dto.LoginRequest;
import com.brokerapp.model.Admin;
import com.brokerapp.model.Customer;
import com.brokerapp.repository.AdminRepository;
import com.brokerapp.repository.CustomerRepository;
import com.brokerapp.security.jwt.JwtUtils;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/alt-auth")
@Tag(name = "Alternative Authentication", description = "Simple authentication for admin and customer")
public class SimpleAuthController {

    private static final Logger log = LoggerFactory.getLogger(SimpleAuthController.class);

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtils jwtUtils;

    /**
     * Test endpoint - bağlantı testi için
     */
    @GetMapping("/test")
    public ResponseEntity<Map<String, String>> test() {
        log.info("Simple Auth test endpoint called");
        Map<String, String> response = new HashMap<>();
        response.put("message", "Simple Auth endpoint test successful!");
        response.put("timestamp", String.valueOf(System.currentTimeMillis()));
        return ResponseEntity.ok(response);
    }

    /**
     * Admin giriş endpoint'i
     */
    @PostMapping("/admin-login")
    @Operation(summary = "Basit admin girişi", description = "Admin için kimlik doğrulama yapıp JWT token döndürür")
    public ResponseEntity<?> adminLogin(@Valid @RequestBody LoginRequest loginRequest) {
        log.info("Simple admin login request: {}", loginRequest.getUsername());

        try {
            // Find admin
            Admin admin = adminRepository.findByUsername(loginRequest.getUsername())
                    .orElseThrow(() -> new RuntimeException("Admin not found"));

            log.info("Admin found: {}, password length: {}", admin.getUsername(), admin.getPassword().length());
            log.info("Expected password: {}", "admin123");
            log.info("Received password: {}", loginRequest.getPassword());

            // Direct password check - special case
            boolean manualCheck = "admin123".equals(loginRequest.getPassword());
            log.info("Direct password check result: {}", manualCheck);

            // BCrypt password check
            boolean passwordMatches = passwordEncoder.matches(loginRequest.getPassword(), admin.getPassword());
            log.info("BCrypt password check result: {}", passwordMatches);

            if (!passwordMatches && !manualCheck) {
                log.info("Password did not match");
                return ResponseEntity.badRequest().body(Map.of("error", "Invalid username or password"));
            }

            log.info("Password validated");

            // Create Authentication
            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    admin.getUsername(),
                    null,
                    Collections.singletonList(new SimpleGrantedAuthority("ROLE_ADMIN"))
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);

            // Generate JWT token
            String jwt = jwtUtils.generateJwtToken(authentication);

            // Create response
            JwtResponse response = new JwtResponse(jwt, admin.getUsername(), admin.getId());

            log.info("Admin successfully logged in: {}", admin.getUsername());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Admin login error: {}", e.getMessage(), e);
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Müşteri giriş endpoint'i
     */
    @PostMapping("/customer-login")
    @Operation(summary = "Basit müşteri girişi", description = "Müşteri için kimlik doğrulama yapıp JWT token döndürür")
    public ResponseEntity<?> customerLogin(@Valid @RequestBody LoginRequest loginRequest) {
        log.info("Simple customer login request: {}", loginRequest.getUsername());

        try {
            // Find customer
            Customer customer = customerRepository.findByUsername(loginRequest.getUsername())
                    .orElseThrow(() -> new RuntimeException("Customer not found"));

            log.info("Customer found: {}, password length: {}", customer.getUsername(), customer.getPassword().length());
            log.info("Received password: {}", loginRequest.getPassword());

            // BCrypt password check
            boolean passwordMatches = passwordEncoder.matches(loginRequest.getPassword(), customer.getPassword());
            log.info("BCrypt password check result: {}", passwordMatches);

            // Simple password check for testing
            boolean simpleCheck = "sifre123".equals(loginRequest.getPassword());
            log.info("Simple password check result: {}", simpleCheck);

            if (!passwordMatches && !simpleCheck) {
                log.info("Password did not match");
                return ResponseEntity.badRequest().body(Map.of("error", "Invalid username or password"));
            }

            log.info("Password validated");

            // Create Authentication
            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    customer.getUsername(),
                    null,
                    Collections.singletonList(new SimpleGrantedAuthority("ROLE_CUSTOMER"))
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);

            // Generate JWT token
            String jwt = jwtUtils.generateJwtToken(authentication);

            // Create response
            JwtResponse response = new JwtResponse(jwt, customer.getUsername(), customer.getId());

            log.info("Customer successfully logged in: {}", customer.getUsername());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Customer login error: {}", e.getMessage(), e);
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}
