package com.brokerapp.service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.brokerapp.dto.AssetDTO;
import com.brokerapp.model.Asset;
import com.brokerapp.repository.AssetRepository;
import com.brokerapp.service.impl.AssetServiceImpl;

/**
 * AssetService için birim testleri
 */
@ExtendWith(MockitoExtension.class)
public class AssetServiceTest {

    @Mock
    private AssetRepository assetRepository;

    @InjectMocks
    private AssetServiceImpl assetService;

    private AssetDTO assetDTO;
    private Asset asset;

    @BeforeEach
    void setUp() {
        // Test için kullanılacak veri nesnelerini hazırla
        assetDTO = new AssetDTO();
        assetDTO.setAssetName("XYZ");
        assetDTO.setTotalSupply(150.25);
        assetDTO.setPrice(10.5);
        assetDTO.setActive(true);

        asset = Asset.builder()
                .id(1L)
                .assetName("XYZ")
                .totalSupply(150.25)
                .price(10.5)
                .active(true)
                .build();
    }

    @Test
    void createAsset_Success() {
        // Mock davranışı ayarla
        when(assetRepository.existsByAssetName(anyString())).thenReturn(false);
        when(assetRepository.save(any(Asset.class))).thenReturn(asset);

        // Metodu çağır
        Asset result = assetService.createAsset(assetDTO);

        // Sonuçları doğrula
        assertNotNull(result);
        assertEquals("XYZ", result.getAssetName());
        assertEquals(150.25, result.getTotalSupply());
        assertEquals(10.5, result.getPrice());
        assertTrue(result.getActive());

        // Mock metotların çağrıldığını doğrula
        verify(assetRepository).existsByAssetName("XYZ");
        verify(assetRepository).save(any(Asset.class));
    }

    @Test
    void createAsset_DuplicateAssetName() {
        // Mock davranışı ayarla
        when(assetRepository.existsByAssetName(anyString())).thenReturn(true);

        // Metot çağrıldığında exception fırlatılmalı
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            assetService.createAsset(assetDTO);
        });

        // Exception mesajını doğrula
        assertTrue(exception.getMessage().contains("Bu varlık adı zaten kullanılıyor"));

        // save metodunun çağrılmadığını doğrula
        verify(assetRepository, never()).save(any(Asset.class));
    }

    @Test
    void getAssetById_Success() {
        // Mock davranışı ayarla
        when(assetRepository.findById(1L)).thenReturn(Optional.of(asset));

        // Metodu çağır
        Optional<Asset> result = assetService.getAssetById(1L);

        // Sonuçları doğrula
        assertTrue(result.isPresent());
        assertEquals("XYZ", result.get().getAssetName());
        assertEquals(150.25, result.get().getTotalSupply());
        assertEquals(10.5, result.get().getPrice());
        assertTrue(result.get().getActive());

        // Mock metotların çağrıldığını doğrula
        verify(assetRepository).findById(1L);
    }

    @Test
    void getAllAssets_Success() {
        // Mock davranışı ayarla
        Asset asset2 = Asset.builder()
                .id(2L)
                .assetName("ABC")
                .totalSupply(50.75)
                .price(20.3)
                .active(true)
                .build();

        when(assetRepository.findAll()).thenReturn(Arrays.asList(asset, asset2));

        // Metodu çağır
        List<Asset> result = assetService.getAllAssets();

        // Sonuçları doğrula
        assertEquals(2, result.size());
        assertEquals("XYZ", result.get(0).getAssetName());
        assertEquals("ABC", result.get(1).getAssetName());
        assertEquals(150.25, result.get(0).getTotalSupply());
        assertEquals(50.75, result.get(1).getTotalSupply());

        // Mock metodun çağrıldığını doğrula
        verify(assetRepository).findAll();
    }
}
