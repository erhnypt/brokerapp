package com.brokerapp.service;

import java.util.List;
import java.util.Optional;

import com.brokerapp.dto.CustomerDTO;
import com.brokerapp.model.Customer;

/**
 * Müşteri işlemleri için servis arayüzü
 */
public interface CustomerService {

    /**
     * Yeni bir müşteri oluşturur
     *
     * @param customerDTO Oluşturulacak müşteri bilgileri
     * @return Oluşturulan müşteri
     */
    Customer createCustomer(CustomerDTO customerDTO);

    /**
     * Belirtilen ID'ye sahip müşteriyi getirir
     *
     * @param id Müşteri ID'si
     * @return Müşteri bulunursa Optional içinde döner
     */
    Optional<Customer> getCustomerById(Long id);

    /**
     * Kullanıcı adına göre müşteri arar
     *
     * @param username Aranacak kullanıcı adı
     * @return Müşteri bulunursa Optional içinde döner
     */
    Optional<Customer> getCustomerByUsername(String username);

    /**
     * Tüm müşterileri listeler
     *
     * @return Müşteri listesi
     */
    List<Customer> getAllCustomers();

    /**
     * Belirtilen ID'ye sahip müşteriyi günceller
     *
     * @param id Güncellenecek müşteri ID'si
     * @param customerDTO Yeni müşteri bilgileri
     * @return Güncellenen müşteri
     */
    Customer updateCustomer(Long id, CustomerDTO customerDTO);

    /**
     * Belirtilen ID'ye sahip müşteriyi siler
     *
     * @param id Silinecek müşteri ID'si
     */
    void deleteCustomer(Long id);

    /**
     * Belirtilen kullanıcı adının kullanılabilir olup olmadığını kontrol eder
     *
     * @param username Kontrol edilecek kullanıcı adı
     * @return Kullanıcı adı kullanılabilirse true döner
     */
    boolean isUsernameAvailable(String username);
}
