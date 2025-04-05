package com.brokerapp.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.brokerapp.model.User;

/**
 * User entity'si için veritabanı işlemlerini sağlayan repository arayüzü.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    /**
     * Kullanıcı adına göre kullanıcı arar
     * 
     * @param username Aranacak kullanıcı adı
     * @return Kullanıcı varsa Optional içinde döner
     */
    Optional<User> findByUsername(String username);
    
    /**
     * Belirli bir kullanıcı adının mevcut olup olmadığını kontrol eder
     * 
     * @param username Kontrol edilecek kullanıcı adı
     * @return Kullanıcı adı mevcutsa true döner
     */
    boolean existsByUsername(String username);
} 