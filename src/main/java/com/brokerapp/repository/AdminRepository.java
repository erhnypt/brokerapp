package com.brokerapp.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.brokerapp.model.Admin;

/**
 * Admin varlığı için JPA repository
 */
@Repository
public interface AdminRepository extends JpaRepository<Admin, Long> {
    
    /**
     * Kullanıcı adına göre admin arama
     * 
     * @param username aranacak kullanıcı adı
     * @return bulunan admin veya boş optional
     */
    Optional<Admin> findByUsername(String username);
    
    /**
     * Kullanıcı adının mevcut olup olmadığını kontrol eder
     * 
     * @param username kontrol edilecek kullanıcı adı
     * @return varsa true, yoksa false
     */
    boolean existsByUsername(String username);
    
    /**
     * E-posta adresinin mevcut olup olmadığını kontrol eder
     * 
     * @param email kontrol edilecek e-posta adresi
     * @return varsa true, yoksa false
     */
    boolean existsByEmail(String email);
} 