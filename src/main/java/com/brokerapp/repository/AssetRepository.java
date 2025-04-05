package com.brokerapp.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.brokerapp.model.Asset;

/**
 * Asset entity'si için veritabanı işlemlerini sağlayan repository arayüzü.
 */
@Repository
public interface AssetRepository extends JpaRepository<Asset, Long> {

    /**
     * Varlık adına göre varlık arar
     *
     * @param assetName Aranacak varlık adı
     * @return Varlık varsa Optional içinde döner
     */
    Optional<Asset> findByAssetName(String assetName);

    /**
     * Belirli bir varlık adının mevcut olup olmadığını kontrol eder
     *
     * @param assetName Kontrol edilecek varlık adı
     * @return Varlık adı mevcutsa true döner
     */
    boolean existsByAssetName(String assetName);

    /**
     * Aktif varlıkları adına göre arar
     *
     * @param assetName Varlık adı
     * @param active Aktiflik durumu
     * @return Varlık
     */
    Optional<Asset> findByAssetNameAndActive(String assetName, Boolean active);
}
