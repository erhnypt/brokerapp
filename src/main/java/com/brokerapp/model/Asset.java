package com.brokerapp.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Asset varlık sınıfı. Borsadaki hisse senedi bilgilerini saklar. Bir Asset,
 * sistemde tanımlı genel bir varlık/hisse senedidir.
 */
@Entity
@Table(name = "assets")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Asset {

    /**
     * Varlık ID'si, otomatik oluşturulur
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Hisse senedi/varlık adı
     */
    @Column(nullable = false, unique = true)
    private String assetName;

    /**
     * Toplam miktar
     */
    @Column(nullable = false)
    private Double totalSupply;

    /**
     * Birim fiyatı
     */
    @Column(nullable = false)
    private Double price;

    /**
     * Varlık aktif mi?
     */
    @Column(nullable = false)
    private Boolean active;

    // Getters
    public Long getId() {
        return id;
    }

    public String getAssetName() {
        return assetName;
    }

    public Double getTotalSupply() {
        return totalSupply;
    }

    public Double getPrice() {
        return price;
    }

    public Boolean getActive() {
        return active;
    }

    // Setters
    public void setId(Long id) {
        this.id = id;
    }

    public void setAssetName(String assetName) {
        this.assetName = assetName;
    }

    public void setTotalSupply(Double totalSupply) {
        this.totalSupply = totalSupply;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    // Builder class
    public static AssetBuilder builder() {
        return new AssetBuilder();
    }

    public static class AssetBuilder {

        private Long id;
        private String assetName;
        private Double totalSupply;
        private Double price;
        private Boolean active;

        public AssetBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public AssetBuilder assetName(String assetName) {
            this.assetName = assetName;
            return this;
        }

        public AssetBuilder totalSupply(Double totalSupply) {
            this.totalSupply = totalSupply;
            return this;
        }

        public AssetBuilder price(Double price) {
            this.price = price;
            return this;
        }

        public AssetBuilder active(Boolean active) {
            this.active = active;
            return this;
        }

        public Asset build() {
            Asset asset = new Asset();
            asset.id = this.id;
            asset.assetName = this.assetName;
            asset.totalSupply = this.totalSupply;
            asset.price = this.price;
            asset.active = this.active;
            return asset;
        }
    }
}
