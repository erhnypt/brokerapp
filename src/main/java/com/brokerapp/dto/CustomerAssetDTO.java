package com.brokerapp.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Müşteri varlığı için veri transfer nesnesi.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerAssetDTO {

    private Long id;

    @NotNull(message = "Müşteri ID boş olamaz")
    private Long customerId;

    @NotNull(message = "Varlık ID boş olamaz")
    private Long assetId;

    @NotNull(message = "Miktar boş olamaz")
    @Min(value = 0, message = "Miktar negatif olamaz")
    private Double size;

    @NotNull(message = "Kullanılabilir miktar boş olamaz")
    @Min(value = 0, message = "Kullanılabilir miktar negatif olamaz")
    private Double usableSize;

    @NotNull(message = "Aktiflik durumu boş olamaz")
    private Boolean active;

    // Getters
    public Long getId() {
        return id;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public Long getAssetId() {
        return assetId;
    }

    public Double getSize() {
        return size;
    }

    public Double getUsableSize() {
        return usableSize;
    }

    public Boolean getActive() {
        return active;
    }

    // Setters
    public void setId(Long id) {
        this.id = id;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public void setAssetId(Long assetId) {
        this.assetId = assetId;
    }

    public void setSize(Double size) {
        this.size = size;
    }

    public void setUsableSize(Double usableSize) {
        this.usableSize = usableSize;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}
