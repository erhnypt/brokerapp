package com.brokerapp.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * User varlık sınıfı. Standart kullanıcı bilgilerini saklar.
 */
@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    /**
     * Kullanıcı ID'si, otomatik oluşturulur
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Kullanıcı adı, benzersiz olmalıdır
     */
    @Column(nullable = false, unique = true)
    private String username;

    /**
     * Kullanıcı şifresi (şifrelenmiş olarak saklanır)
     */
    @Column(nullable = false)
    private String password;

    /**
     * Kullanıcı hesabının aktif olup olmadığı
     */
    @Column(nullable = false)
    private boolean active;

    // Getters
    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public boolean isActive() {
        return active;
    }

    // Setters
    public void setId(Long id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    // Builder class
    public static UserBuilder builder() {
        return new UserBuilder();
    }

    public static class UserBuilder {

        private Long id;
        private String username;
        private String password;
        private boolean active;

        public UserBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public UserBuilder username(String username) {
            this.username = username;
            return this;
        }

        public UserBuilder password(String password) {
            this.password = password;
            return this;
        }

        public UserBuilder active(boolean active) {
            this.active = active;
            return this;
        }

        public User build() {
            User user = new User();
            user.id = this.id;
            user.username = this.username;
            user.password = this.password;
            user.active = this.active;
            return user;
        }
    }
}
