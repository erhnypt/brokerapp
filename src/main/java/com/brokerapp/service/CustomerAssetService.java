package com.brokerapp.service;

import java.util.List;
import java.util.Optional;

import com.brokerapp.dto.CustomerAssetDTO;
import com.brokerapp.model.CustomerAsset;

/**
 * Müşteri varlık işlemleri için servis arayüzü
 */
public interface CustomerAssetService {

    /**
     * Yeni bir müşteri varlığı oluşturur veya varsa günceller
     *
     * @param customerAssetDTO Oluşturulacak/güncellenecek müşteri varlığı
     * bilgileri
     * @return Oluşturulan/güncellenen müşteri varlığı
     */
    CustomerAsset createOrUpdateCustomerAsset(CustomerAssetDTO customerAssetDTO);

    /**
     * Belirtilen ID'ye sahip müşteri varlığını getirir
     *
     * @param id Müşteri varlığı ID'si
     * @return Müşteri varlığı bulunursa Optional içinde döner
     */
    Optional<CustomerAsset> getCustomerAssetById(Long id);

    /**
     * Müşteri ID'sine göre müşteri varlıklarını getirir
     *
     * @param customerId Müşteri ID'si
     * @return Müşteri varlıkları listesi
     */
    List<CustomerAsset> getCustomerAssetsByCustomerId(Long customerId);

    /**
     * Müşteri ID'si ve varlık ID'sine göre müşteri varlığını getirir
     *
     * @param customerId Müşteri ID'si
     * @param assetId Varlık ID'si
     * @return Müşteri varlığı bulunursa Optional içinde döner
     */
    Optional<CustomerAsset> getCustomerAssetByCustomerIdAndAssetId(Long customerId, Long assetId);

    /**
     * Müşteri ID'sine ve varlık adına göre müşteri varlığını getirir
     *
     * @param customerId Müşteri ID'si
     * @param assetName Varlık adı
     * @return Müşteri varlığı bulunursa Optional içinde döner
     */
    Optional<CustomerAsset> getCustomerAssetByCustomerIdAndAssetName(Long customerId, String assetName);

    /**
     * Belirtilen müşteri varlığını günceller
     *
     * @param id Güncellenecek müşteri varlığı ID'si
     * @param customerAssetDTO Yeni müşteri varlığı bilgileri
     * @return Güncellenen müşteri varlığı
     */
    CustomerAsset updateCustomerAsset(Long id, CustomerAssetDTO customerAssetDTO);

    /**
     * Belirtilen müşteri varlığını siler
     *
     * @param id Silinecek müşteri varlığı ID'si
     */
    void deleteCustomerAsset(Long id);

    /**
     * Müşteri varlığının miktarını artırır
     *
     * @param customerId Müşteri ID'si
     * @param assetId Varlık ID'si
     * @param amount Artırılacak miktar
     * @return Güncellenen müşteri varlığı
     */
    CustomerAsset increaseCustomerAssetAmount(Long customerId, Long assetId, Double amount);

    /**
     * Müşteri varlığının miktarını azaltır
     *
     * @param customerId Müşteri ID'si
     * @param assetId Varlık ID'si
     * @param amount Azaltılacak miktar
     * @return Güncellenen müşteri varlığı
     */
    CustomerAsset decreaseCustomerAssetAmount(Long customerId, Long assetId, Double amount);

    /**
     * Müşteri varlığının kullanılabilir miktarını artırır
     *
     * @param customerId Müşteri ID'si
     * @param assetId Varlık ID'si
     * @param amount Artırılacak kullanılabilir miktar
     * @return Güncellenen müşteri varlığı
     */
    CustomerAsset increaseCustomerAssetUsableAmount(Long customerId, Long assetId, Double amount);

    /**
     * Müşteri varlığının kullanılabilir miktarını azaltır
     *
     * @param customerId Müşteri ID'si
     * @param assetId Varlık ID'si
     * @param amount Azaltılacak kullanılabilir miktar
     * @return Güncellenen müşteri varlığı
     */
    CustomerAsset decreaseCustomerAssetUsableAmount(Long customerId, Long assetId, Double amount);
}
