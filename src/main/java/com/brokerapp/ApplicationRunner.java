package com.brokerapp;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.brokerapp.model.Admin;
import com.brokerapp.repository.AdminRepository;
import com.brokerapp.service.AdminService;

/**
 * Code to run at application startup
 */
@Component
public class ApplicationRunner implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(ApplicationRunner.class);

    @Autowired
    private AdminService adminService;

    @Autowired
    private AdminRepository adminRepository;

    @Override
    public void run(String... args) {
        log.info("Application starting...");

        // Create default admin account if not exists
        adminService.createDefaultAdminIfNotExists();

        // Check how many admins are in the database
        long adminCount = adminRepository.count();
        log.info("There are {} admin accounts in the database", adminCount);

        // List all admin accounts
        List<Admin> allAdmins = adminRepository.findAll();
        for (Admin admin : allAdmins) {
            log.info("Admin found: id={}, username={}, active={}",
                    admin.getId(), admin.getUsername(), admin.isActive());
        }

        log.info("Application startup completed.");
    }
}
