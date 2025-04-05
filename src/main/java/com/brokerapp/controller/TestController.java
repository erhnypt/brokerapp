package com.brokerapp.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.brokerapp.repository.AdminRepository;

/**
 * Test işlemleri için controller
 */
@RestController
@RequestMapping("/api/test")
public class TestController {

    private static final Logger log = LoggerFactory.getLogger(TestController.class);

    @Autowired
    private AdminRepository adminRepository;

    /**
     * Sistem durumunu kontrol etmek için kullanılır
     */
    @GetMapping("/status")
    public ResponseEntity<Map<String, Object>> status() {
        Map<String, Object> response = new HashMap<>();

        // System status
        response.put("status", "up");
        response.put("timestamp", System.currentTimeMillis());

        // Admin check
        long adminCount = adminRepository.count();
        response.put("adminCount", adminCount);

        // Detailed admin data
        List<Map<String, Object>> admins = adminRepository.findAll().stream()
                .map(admin -> {
                    Map<String, Object> adminMap = new HashMap<>();
                    adminMap.put("id", admin.getId());
                    adminMap.put("username", admin.getUsername());
                    // Password not included for security
                    adminMap.put("active", admin.isActive());
                    return adminMap;
                })
                .toList();

        response.put("admins", admins);

        return ResponseEntity.ok(response);
    }

    /**
     * Exception fırlatarak hata yönetimini test eder
     */
    @GetMapping("/error")
    public ResponseEntity<Map<String, Object>> testError() {
        throw new RuntimeException("Test exception");
    }
}
