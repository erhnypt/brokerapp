package com.brokerapp.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.brokerapp.dto.LoginRequest;
import com.brokerapp.model.Admin;
import com.brokerapp.repository.AdminRepository;
import com.brokerapp.security.jwt.JwtUtils;

/**
 * Basitleştirilmiş login test controller
 */
@RestController
@RequestMapping("/api/test/login")
public class LoginTestController {

    private static final Logger log = LoggerFactory.getLogger(LoginTestController.class);

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtils jwtUtils;

    /**
     * Login süreci için adım adım test
     */
    @PostMapping("/step")
    public ResponseEntity<Map<String, Object>> loginStepByStep(@RequestBody LoginRequest loginRequest) {
        Map<String, Object> response = new HashMap<>();
        Map<String, Object> steps = new HashMap<>();

        try {
            // Step 1: Check admin existence
            log.info("Step 1: Checking admin existence: {}", loginRequest.getUsername());
            Optional<Admin> adminOptional = adminRepository.findByUsername(loginRequest.getUsername());
            steps.put("step1_admin_exists", adminOptional.isPresent());

            if (adminOptional.isPresent()) {
                // Step 2: Password check
                Admin admin = adminOptional.get();
                log.info("Step 2: Checking password");

                // Show raw password and hash values
                steps.put("step2_raw_password", loginRequest.getPassword());
                steps.put("step2_stored_hash", admin.getPassword());

                // Check password correctness (don't compare directly, this is just for testing)
                boolean passwordMatch = passwordEncoder.matches(loginRequest.getPassword(), admin.getPassword());
                steps.put("step2_password_match", passwordMatch);

                if (passwordMatch) {
                    // Step 3: Authentication using Authentication Manager
                    log.info("Step 3: Calling Authentication Manager");
                    try {
                        Authentication authentication = authenticationManager.authenticate(
                                new UsernamePasswordAuthenticationToken(
                                        loginRequest.getUsername(),
                                        loginRequest.getPassword()
                                )
                        );
                        steps.put("step3_auth_manager", "success");

                        // Step 4: Update security context
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                        steps.put("step4_security_context", "updated");

                        // Step 5: Generate JWT token
                        log.info("Step 5: Generating JWT token");
                        String jwt = jwtUtils.generateJwtToken(authentication);
                        steps.put("step5_jwt_token", "generated");
                        steps.put("jwt_token", jwt);
                    } catch (Exception e) {
                        log.error("Error in Step 3: ", e);
                        steps.put("step3_auth_manager", "failed");
                        steps.put("step3_error", e.getMessage());
                    }
                }
            }

            response.put("result", "success");
            response.put("steps", steps);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error during test: ", e);
            response.put("result", "error");
            response.put("message", e.getMessage());
            response.put("steps", steps);
            return ResponseEntity.ok(response);
        }
    }

    /**
     * Veritabanındaki adminleri gösterir
     */
    @GetMapping("/admins")
    public ResponseEntity<Map<String, Object>> admins() {
        Map<String, Object> response = new HashMap<>();
        response.put("adminCount", adminRepository.count());
        response.put("admins", adminRepository.findAll());
        return ResponseEntity.ok(response);
    }
}
