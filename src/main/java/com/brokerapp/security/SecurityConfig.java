package com.brokerapp.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Temel güvenlik yapılandırmalarını içeren sınıf. Dairesel bağımlılıkları
 * önlemek için WebSecurityConfig'den ayrılmıştır.
 */
@Configuration
public class SecurityConfig {

    private static final Logger logger = LoggerFactory.getLogger(SecurityConfig.class);

    /**
     * Şifre kodlayıcı bean'i
     *
     * @return BCryptPasswordEncoder
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        logger.info("BCryptPasswordEncoder oluşturuluyor");
        return new BCryptPasswordEncoder();
    }
}
