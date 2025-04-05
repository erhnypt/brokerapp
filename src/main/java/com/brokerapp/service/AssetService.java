package com.brokerapp.service;

import java.util.List;
import java.util.Optional;

import com.brokerapp.dto.AssetDTO;
import com.brokerapp.model.Asset;

/**
 * Varlık işlemleri için servis arayüzü
 */
public interface AssetService {

    /**
     * Yeni bir varlık oluşturur
     *
     * @param assetDTO Oluşturulacak varlık bilgileri
     * @return Oluşturulan varlık
     */
    Asset createAsset(AssetDTO assetDTO);

    /**
     * Belirtilen ID'ye sahip varlığı getirir
     *
     * @param id Varlık ID'si
     * @return Varlık bulunursa Optional içinde döner
     */
    Optional<Asset> getAssetById(Long id);

    /**
     * Varlık adına göre varlık arar
     *
     * @param assetName Aranacak varlık adı
     * @return Varlık bulunursa Optional içinde döner
     */
    Optional<Asset> getAssetByName(String assetName);

    /**
     * Tüm varlıkları listeler
     *
     * @return Varlıklar listesi
     */
    List<Asset> getAllAssets();

    /**
     * Belirtilen ID'ye sahip varlığı günceller
     *
     * @param id Güncellenecek varlık ID'si
     * @param assetDTO Yeni varlık bilgileri
     * @return Güncellenen varlık
     */
    Asset updateAsset(Long id, AssetDTO assetDTO);

    /**
     * Belirtilen ID'ye sahip varlığı siler
     *
     * @param id Silinecek varlık ID'si
     */
    void deleteAsset(Long id);

    /**
     * Belirtilen varlık adının kullanılabilir olup olmadığını kontrol eder
     *
     * @param assetName Kontrol edilecek varlık adı
     * @return Varlık adı kullanılabilirse true döner
     */
    boolean isAssetNameAvailable(String assetName);

    /**
     * Varlık fiyatını günceller
     *
     * @param id Varlık ID'si
     * @param price Yeni fiyat
     * @return Güncellenen varlık
     */
    Asset updateAssetPrice(Long id, Double price);

    /**
     * Varlık toplam arzını günceller
     *
     * @param id Varlık ID'si
     * @param totalSupply Yeni toplam arz
     * @return Güncellenen varlık
     */
    Asset updateAssetTotalSupply(Long id, Double totalSupply);

    /**
     * Aktif varlıkları listeler
     *
     * @return Aktif varlıklar listesi
     */
    List<Asset> getActiveAssets();
}
