package com.brokerapp.dto;

import java.time.LocalDateTime;

import com.brokerapp.enums.OrderStatus;
import com.brokerapp.enums.OrderType;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * İşlem (Order) oluşturmak için kullanılan DTO sınıfı
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderDTO {

    private Long id;

    /**
     * Müşteri ID'si
     */
    @NotNull(message = "Müşteri ID zorunludur")
    private Long customerId;

    /**
     * Varlık ID'si
     */
    @NotNull(message = "Varlık ID zorunludur")
    private Long assetId;

    /**
     * İşlem yönü (ALIM veya SATIM)
     */
    @NotNull(message = "İşlem yönü zorunludur")
    private OrderType side;

    /**
     * İşlem miktarı
     */
    @NotNull(message = "Miktar zorunludur")
    @Positive(message = "Miktar pozitif olmalıdır")
    private Double size;

    /**
     * İşlem fiyatı
     */
    @NotNull(message = "Fiyat zorunludur")
    @Positive(message = "Fiyat pozitif olmalıdır")
    private Double price;

    private OrderStatus status = OrderStatus.PENDING;

    private LocalDateTime createDate;

    private LocalDateTime updatedAt;

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

    public OrderType getSide() {
        return side;
    }

    public Double getSize() {
        return size;
    }

    public Double getPrice() {
        return price;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
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

    public void setSide(OrderType side) {
        this.side = side;
    }

    public void setSize(Double size) {
        this.size = size;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
