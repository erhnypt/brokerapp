package com.brokerapp.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.brokerapp.model.Asset;
import com.brokerapp.model.Customer;
import com.brokerapp.model.CustomerAsset;

/**
 * CustomerAsset entity'si için veritabanı işlemlerini sağlayan repository arayüzü.
 */
@Repository
public interface CustomerAssetRepository extends JpaRepository<CustomerAsset, Long> {

    /**
     * Müşteriye ait tüm varlıkları bulur
     *
     * @param customer Aranacak müşteri
     * @return Müşteriye ait varlık listesi
     */
    List<CustomerAsset> findByCustomer(Customer customer);

    /**
     * Müşteriye ait ve belirli bir varlık için kaydı bulur
     *
     * @param customer Müşteri
     * @param asset Varlık
     * @return Bulunan müşteri varlığı
     */
    Optional<CustomerAsset> findByCustomerAndAsset(Customer customer, Asset asset);

    /**
     * Müşteriye ait TRY varlığını bulur
     *
     * @param customer Müşteri
     * @param assetName Varlık adı (TRY)
     * @return TRY varlığı
     */
    Optional<CustomerAsset> findByCustomerAndAsset_AssetName(Customer customer, String assetName);
    
    /**
     * Müşteri ID'sine göre varlıkları bulur
     *
     * @param customerId Müşteri ID'si
     * @return Varlık listesi
     */
    List<CustomerAsset> findByCustomerId(Long customerId);
    
    /**
     * Müşteri ID'si ve varlık ID'sine göre varlığı bulur
     *
     * @param customerId Müşteri ID'si
     * @param assetId Varlık ID'si
     * @return Bulunan müşteri varlığı
     */
    Optional<CustomerAsset> findByCustomerIdAndAssetId(Long customerId, Long assetId);
} 