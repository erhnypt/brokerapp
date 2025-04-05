package com.brokerapp.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.brokerapp.dto.AssetDTO;
import com.brokerapp.model.Asset;
import com.brokerapp.repository.AssetRepository;
import com.brokerapp.service.AssetService;

/**
 * Hisse senedi servis arabiriminin uygulaması
 */
@Service
public class AssetServiceImpl implements AssetService {

    private final AssetRepository assetRepository;

    /**
     * Constructor-based dependency injection
     */
    @Autowired
    public AssetServiceImpl(AssetRepository assetRepository) {
        this.assetRepository = assetRepository;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public Asset createAsset(AssetDTO assetDTO) {
        if (!isAssetNameAvailable(assetDTO.getAssetName())) {
            throw new IllegalArgumentException("This asset name is already in use: " + assetDTO.getAssetName());
        }

        Asset asset = Asset.builder()
                .assetName(assetDTO.getAssetName())
                .totalSupply(assetDTO.getTotalSupply())
                .price(assetDTO.getPrice())
                .active(assetDTO.getActive())
                .build();

        return assetRepository.save(asset);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Asset> getAssetById(Long id) {
        return assetRepository.findById(id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Asset> getAssetByName(String assetName) {
        return assetRepository.findByAssetName(assetName);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Asset> getAllAssets() {
        return assetRepository.findAll();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public Asset updateAsset(Long id, AssetDTO assetDTO) {
        Asset existingAsset = assetRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Asset not found with ID: " + id));

        // If asset name has changed and new asset name is used by another asset
        if (!existingAsset.getAssetName().equals(assetDTO.getAssetName())
                && !isAssetNameAvailable(assetDTO.getAssetName())) {
            throw new IllegalArgumentException("This asset name is already in use: " + assetDTO.getAssetName());
        }

        existingAsset.setAssetName(assetDTO.getAssetName());
        existingAsset.setTotalSupply(assetDTO.getTotalSupply());
        existingAsset.setPrice(assetDTO.getPrice());
        existingAsset.setActive(assetDTO.getActive());

        return assetRepository.save(existingAsset);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void deleteAsset(Long id) {
        if (!assetRepository.existsById(id)) {
            throw new IllegalArgumentException("Asset not found with ID: " + id);
        }
        assetRepository.deleteById(id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isAssetNameAvailable(String assetName) {
        return !assetRepository.existsByAssetName(assetName);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public Asset updateAssetPrice(Long id, Double price) {
        Asset asset = assetRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Asset not found with ID: " + id));

        asset.setPrice(price);
        return assetRepository.save(asset);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public Asset updateAssetTotalSupply(Long id, Double totalSupply) {
        Asset asset = assetRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Asset not found with ID: " + id));

        asset.setTotalSupply(totalSupply);
        return assetRepository.save(asset);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Asset> getActiveAssets() {
        return assetRepository.findAll().stream()
                .filter(asset -> asset.getActive())
                .collect(Collectors.toList());
    }
}
