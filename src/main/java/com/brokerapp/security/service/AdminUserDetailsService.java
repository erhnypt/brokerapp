package com.brokerapp.security.service;

import com.brokerapp.model.Admin;
import com.brokerapp.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Admin kullanıcılar için UserDetailsService implementasyonu
 */
@Service
public class AdminUserDetailsService implements UserDetailsService {

    @Autowired
    private AdminRepository adminRepository;

    /**
     * Kullanıcı adına göre admin bilgilerini yükler
     *
     * @param username yüklenecek admin kullanıcı adı
     * @return admin için UserDetails nesnesi
     * @throws UsernameNotFoundException admin bulunamazsa fırlatılır
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Admin admin = adminRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Admin kullanıcısı bulunamadı: " + username));
        return new AdminUserDetailsImpl(admin);
    }
}
