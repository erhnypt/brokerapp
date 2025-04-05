package com.brokerapp.service;

import com.brokerapp.model.Admin;
import com.brokerapp.repository.AdminRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Admin işlemleri servisi
 */
@Service
public class AdminService {

    private static final Logger log = LoggerFactory.getLogger(AdminService.class);

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Value("${admin.username}")
    private String adminUsername;

    @Value("${admin.password}")
    private String adminPassword;

    // Email default value
    private String adminEmail = "admin@brokerapp.com";

    /**
     * Kullanıcı adına göre admin arama
     *
     * @param username aranan admin kullanıcı adı
     * @return bulunan Admin nesnesi veya boş Optional
     */
    public Optional<Admin> findByUsername(String username) {
        return adminRepository.findByUsername(username);
    }

    /**
     * Yeni admin ekler
     *
     * @param admin eklenecek admin
     * @return eklenen admin
     */
    public Admin save(Admin admin) {
        admin.setPassword(passwordEncoder.encode(admin.getPassword()));
        return adminRepository.save(admin);
    }

    /**
     * İlk admin hesabını oluşturur (veritabanı boşsa) Bilgiler
     * application.properties'ten okunur
     */
    public void createDefaultAdminIfNotExists() {
        if (adminRepository.count() == 0) {
            log.info("Creating default admin... Username: {}", adminUsername);
            Admin admin = Admin.builder()
                    .username(adminUsername)
                    .password(passwordEncoder.encode(adminPassword))
                    .email(adminEmail)
                    .active(true)
                    .build();
            adminRepository.save(admin);
            log.info("Default admin created successfully");
        } else {
            log.info("Default admin already exists");
        }
    }
}
