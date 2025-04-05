package com.brokerapp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.brokerapp.model.Asset;
import com.brokerapp.model.CustomerAsset;
import com.brokerapp.service.AssetService;
import com.brokerapp.service.CustomerAssetService;

/**
 * Varlık işlemleri için REST API controller
 */
@RestController
@RequestMapping("/api/assets")
public class AssetController {

    private final AssetService assetService;
    private final CustomerAssetService customerAssetService;

    /**
     * Constructor-based dependency injection
     */
    @Autowired
    public AssetController(AssetService assetService, CustomerAssetService customerAssetService) {
        this.assetService = assetService;
        this.customerAssetService = customerAssetService;
    }

    /**
     * Tüm varlıkları listeler
     *
     * @return varlık listesi
     */
    @GetMapping
    public ResponseEntity<List<Asset>> getAllAssets() {
        List<Asset> assets = assetService.getAllAssets();
        return ResponseEntity.ok(assets);
    }

    /**
     * ID'ye göre varlık getirir
     *
     * @param id varlık ID'si
     * @return bulunan varlık
     */
    @GetMapping("/{id}")
    public ResponseEntity<Asset> getAssetById(@PathVariable Long id) {
        return assetService.getAssetById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Müşteriye ait varlıkları listeler
     *
     * @param customerId müşteri ID'si
     * @return müşteri varlıkları listesi
     */
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<CustomerAsset>> getAssetsByCustomerId(@PathVariable Long customerId) {
        List<CustomerAsset> customerAssets = customerAssetService.getCustomerAssetsByCustomerId(customerId);
        return ResponseEntity.ok(customerAssets);
    }
}
