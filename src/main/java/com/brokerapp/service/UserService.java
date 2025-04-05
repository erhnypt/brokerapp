package com.brokerapp.service;

import java.util.List;
import java.util.Optional;

import com.brokerapp.dto.UserDTO;
import com.brokerapp.model.User;

/**
 * Kullanıcı işlemleri için servis arayüzü
 */
public interface UserService {

    /**
     * Yeni bir kullanıcı oluşturur
     *
     * @param userDTO Oluşturulacak kullanıcı bilgileri
     * @return Oluşturulan kullanıcı
     */
    User createUser(UserDTO userDTO);

    /**
     * Belirtilen ID'ye sahip kullanıcıyı getirir
     *
     * @param id Kullanıcı ID'si
     * @return Kullanıcı bulunursa Optional içinde döner
     */
    Optional<User> getUserById(Long id);

    /**
     * Kullanıcı adına göre kullanıcı arar
     *
     * @param username Aranacak kullanıcı adı
     * @return Kullanıcı bulunursa Optional içinde döner
     */
    Optional<User> getUserByUsername(String username);

    /**
     * Tüm kullanıcıları listeler
     *
     * @return Kullanıcı listesi
     */
    List<User> getAllUsers();

    /**
     * Belirtilen ID'ye sahip kullanıcıyı günceller
     *
     * @param id Güncellenecek kullanıcı ID'si
     * @param userDTO Yeni kullanıcı bilgileri
     * @return Güncellenen kullanıcı
     */
    User updateUser(Long id, UserDTO userDTO);

    /**
     * Belirtilen ID'ye sahip kullanıcıyı siler
     *
     * @param id Silinecek kullanıcı ID'si
     */
    void deleteUser(Long id);

    /**
     * Belirtilen kullanıcı adının kullanılabilir olup olmadığını kontrol eder
     *
     * @param username Kontrol edilecek kullanıcı adı
     * @return Kullanıcı adı kullanılabilirse true döner
     */
    boolean isUsernameAvailable(String username);
}
