package com.brokerapp.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Müşteri için veri transfer nesnesi.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerDTO {

    private Long id;

    @NotBlank(message = "Kullanıcı adı boş olamaz")
    @Size(min = 3, max = 50, message = "Kullanıcı adı 3-50 karakter arası olmalıdır")
    private String username;

    @NotBlank(message = "Şifre boş olamaz")
    @Size(min = 6, message = "Şifre en az 6 karakter olmalıdır")
    private String password;

    @NotNull(message = "TRY bakiyesi boş olamaz")
    @Min(value = 0, message = "TRY bakiyesi negatif olamaz")
    private Double tryBalance;

    @NotNull(message = "Kullanılabilir TRY bakiyesi boş olamaz")
    @Min(value = 0, message = "Kullanılabilir TRY bakiyesi negatif olamaz")
    private Double tryUsableBalance;

    @NotNull(message = "Aktiflik durumu boş olamaz")
    private Boolean active;

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

    public Double getTryBalance() {
        return tryBalance;
    }

    public Double getTryUsableBalance() {
        return tryUsableBalance;
    }

    public Boolean getActive() {
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

    public void setTryBalance(Double tryBalance) {
        this.tryBalance = tryBalance;
    }

    public void setTryUsableBalance(Double tryUsableBalance) {
        this.tryUsableBalance = tryUsableBalance;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}
