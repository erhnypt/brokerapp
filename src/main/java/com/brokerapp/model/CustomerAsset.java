package com.brokerapp.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * CustomerAsset sınıfı. Müşterilerin sahip olduğu varlıkları saklar.
 */
@Entity
@Table(name = "customer_assets", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"customer_id", "asset_id"})
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerAsset {

    /**
     * CustomerAsset ID'si, otomatik oluşturulur
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Varlık sahibi müşteri
     */
    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    /**
     * Sahip olunan varlık
     */
    @ManyToOne
    @JoinColumn(name = "asset_id", nullable = false)
    private Asset asset;

    /**
     * Toplam sahip olunan miktar
     */
    @Column(nullable = false)
    private Double size;

    /**
     * Kullanılabilir miktar (işlemde olmayan)
     */
    @Column(nullable = false)
    private Double usableSize;

    /**
     * Aktif mi?
     */
    @Column(nullable = false)
    private Boolean active;

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

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public void setAsset(Asset asset) {
        this.asset = asset;
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

    // Builder class
    public static CustomerAssetBuilder builder() {
        return new CustomerAssetBuilder();
    }

    public static class CustomerAssetBuilder {

        private Long id;
        private Customer customer;
        private Asset asset;
        private Double size;
        private Double usableSize;
        private Boolean active;

        public CustomerAssetBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public CustomerAssetBuilder customer(Customer customer) {
            this.customer = customer;
            return this;
        }

        public CustomerAssetBuilder asset(Asset asset) {
            this.asset = asset;
            return this;
        }

        public CustomerAssetBuilder size(Double size) {
            this.size = size;
            return this;
        }

        public CustomerAssetBuilder usableSize(Double usableSize) {
            this.usableSize = usableSize;
            return this;
        }

        public CustomerAssetBuilder active(Boolean active) {
            this.active = active;
            return this;
        }

        public CustomerAsset build() {
            CustomerAsset customerAsset = new CustomerAsset();
            customerAsset.id = this.id;
            customerAsset.customer = this.customer;
            customerAsset.asset = this.asset;
            customerAsset.size = this.size;
            customerAsset.usableSize = this.usableSize;
            customerAsset.active = this.active;
            return customerAsset;
        }
    }
}
