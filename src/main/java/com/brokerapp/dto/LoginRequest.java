package com.brokerapp.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.NoArgsConstructor;

/**
 * Admin giriş isteği için DTO sınıfı
 */
@NoArgsConstructor
public class LoginRequest {

    /**
     * Admin kullanıcı adı
     */
    @NotBlank(message = "Kullanıcı adı boş olamaz")
    @Size(min = 3, max = 50, message = "Kullanıcı adı 3-50 karakter arasında olmalıdır")
    private String username;

    /**
     * Admin şifresi
     */
    @NotBlank(message = "Şifre boş olamaz")
    @Size(min = 6, max = 120, message = "Şifre en az 6 karakter olmalıdır")
    private String password;

    /**
     * Parametreli constructor
     *
     * @param username kullanıcı adı
     * @param password şifre
     */
    public LoginRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }

    /**
     * Kullanıcı adını döndürür
     *
     * @return kullanıcı adı
     */
    public String getUsername() {
        return username;
    }

    /**
     * Kullanıcı adını ayarlar
     *
     * @param username kullanıcı adı
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Şifreyi döndürür
     *
     * @return şifre
     */
    public String getPassword() {
        return password;
    }

    /**
     * Şifreyi ayarlar
     *
     * @param password şifre
     */
    public void setPassword(String password) {
        this.password = password;
    }
}
