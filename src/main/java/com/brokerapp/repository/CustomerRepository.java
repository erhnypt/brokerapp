package com.brokerapp.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.brokerapp.model.Customer;

/**
 * Customer entity'si için veritabanı işlemlerini sağlayan repository arayüzü.
 */
@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    
    /**
     * Kullanıcı adına göre müşteri arar
     * 
     * @param username Aranacak kullanıcı adı
     * @return Müşteri varsa Optional içinde döner
     */
    Optional<Customer> findByUsername(String username);
    
    /**
     * Belirli bir kullanıcı adının mevcut olup olmadığını kontrol eder
     * 
     * @param username Kontrol edilecek kullanıcı adı
     * @return Kullanıcı adı mevcutsa true döner
     */
    boolean existsByUsername(String username);
} 