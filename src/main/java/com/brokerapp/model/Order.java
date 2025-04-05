package com.brokerapp.model;

import java.time.LocalDateTime;

import com.brokerapp.enums.OrderStatus;
import com.brokerapp.enums.OrderType;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * İşlem (Order) entity sınıfı
 */
@Entity
@Table(name = "orders")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Order {

    /**
     * Benzersiz işlem tanımlayıcısı
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * İşlem sahibi müşteri
     */
    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    /**
     * İşlemin yapıldığı varlık
     */
    @ManyToOne
    @JoinColumn(name = "asset_id", nullable = false)
    private Asset asset;

    /**
     * İşlem yönü (ALIM/SATIM)
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "order_side", nullable = false)
    private OrderType side;

    /**
     * İşlem miktarı
     */
    @Column(nullable = false)
    private Double size;

    /**
     * İşlem fiyatı
     */
    @Column(nullable = false)
    private Double price;

    /**
     * İşlem durumu (BEKLEMEDE/EŞLEŞTİ/İPTAL EDİLDİ)
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status;

    /**
     * Oluşturma tarihi
     */
    @Column(name = "create_date", nullable = false, updatable = false)
    private LocalDateTime createDate;

    /**
     * Son güncelleme tarihi
     */
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    /**
     * İşlem değeri hesaplaması (miktar * fiyat)
     *
     * @return işlem değeri
     */
    @Transient
    public Double getTotalValue() {
        return size * price;
    }

    /**
     * Oluşturma öncesi yapılacak işlemler
     */
    @PrePersist
    protected void onCreate() {
        createDate = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    /**
     * Güncelleme öncesi yapılacak işlemler
     */
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    // Getters
    public Long getId() {
        return id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public Asset getAsset() {
        return asset;
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

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public void setAsset(Asset asset) {
        this.asset = asset;
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

    // Builder class
    public static OrderBuilder builder() {
        return new OrderBuilder();
    }

    public static class OrderBuilder {

        private Long id;
        private Customer customer;
        private Asset asset;
        private OrderType side;
        private Double size;
        private Double price;
        private OrderStatus status;
        private LocalDateTime createDate;
        private LocalDateTime updatedAt;

        public OrderBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public OrderBuilder customer(Customer customer) {
            this.customer = customer;
            return this;
        }

        public OrderBuilder asset(Asset asset) {
            this.asset = asset;
            return this;
        }

        public OrderBuilder side(OrderType side) {
            this.side = side;
            return this;
        }

        public OrderBuilder size(Double size) {
            this.size = size;
            return this;
        }

        public OrderBuilder price(Double price) {
            this.price = price;
            return this;
        }

        public OrderBuilder status(OrderStatus status) {
            this.status = status;
            return this;
        }

        public OrderBuilder createDate(LocalDateTime createDate) {
            this.createDate = createDate;
            return this;
        }

        public OrderBuilder updatedAt(LocalDateTime updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        public Order build() {
            Order order = new Order();
            order.id = this.id;
            order.customer = this.customer;
            order.asset = this.asset;
            order.side = this.side;
            order.size = this.size;
            order.price = this.price;
            order.status = this.status;
            order.createDate = this.createDate;
            order.updatedAt = this.updatedAt;
            return order;
        }
    }
}
