package com.brokerapp.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Admin varlık sınıfı. Sistem yöneticisi bilgilerini saklar.
 */
@Entity
@Table(name = "admins")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Admin {

    /**
     * Admin ID'si, otomatik oluşturulur
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Admin kullanıcı adı
     */
    @Column(nullable = false, unique = true)
    private String username;

    /**
     * Admin şifresi (şifrelenmiş olarak saklanır)
     */
    @Column(nullable = false)
    private String password;

    /**
     * Admin e-posta adresi, isteğe bağlı
     */
    @Column
    private String email;

    /**
     * Admin hesabının aktif olup olmadığı
     */
    @Column(nullable = false)
    private boolean active;

    /**
     * Şifreyi ayarlar
     *
     * @param password şifre
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Email adresini ayarlar
     *
     * @param email email adresi
     */
    public void setEmail(String email) {
        this.email = email;
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
     * Kullanıcı adını döndürür
     */
    public String getUsername() {
        return username;
    }

    /**
     * Şifreyi döndürür
     */
    public String getPassword() {
        return password;
    }

    /**
     * Hesabın aktif olup olmadığını döndürür
     */
    public boolean isActive() {
        return active;
    }

    /**
     * ID'yi döndürür
     */
    public Long getId() {
        return id;
    }

    /**
     * Builder pattern ile Admin oluşturur
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Admin builder sınıfı
     */
    public static class Builder {

        private final Admin admin = new Admin();

        public Builder id(Long id) {
            admin.id = id;
            return this;
        }

        public Builder username(String username) {
            admin.username = username;
            return this;
        }

        public Builder password(String password) {
            admin.password = password;
            return this;
        }

        public Builder email(String email) {
            admin.email = email;
            return this;
        }

        public Builder active(boolean active) {
            admin.active = active;
            return this;
        }

        public Admin build() {
            return admin;
        }
    }
}
