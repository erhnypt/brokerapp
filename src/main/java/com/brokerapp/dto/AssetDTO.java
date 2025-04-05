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
 * Varlık için veri transfer nesnesi.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AssetDTO {

    private Long id;

    @NotBlank(message = "Varlık adı boş olamaz")
    @Size(min = 2, max = 10, message = "Varlık adı 2-10 karakter arası olmalıdır")
    private String assetName;

    @NotNull(message = "Toplam arz boş olamaz")
    @Min(value = 0, message = "Toplam arz negatif olamaz")
    private Double totalSupply;

    @NotNull(message = "Fiyat boş olamaz")
    @Min(value = 0, message = "Fiyat negatif olamaz")
    private Double price;

    @NotNull(message = "Aktiflik durumu boş olamaz")
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
}
