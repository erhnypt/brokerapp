package com.brokerapp.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.brokerapp.dto.CustomerAssetDTO;
import com.brokerapp.model.Asset;
import com.brokerapp.model.Customer;
import com.brokerapp.model.CustomerAsset;
import com.brokerapp.repository.AssetRepository;
import com.brokerapp.repository.CustomerAssetRepository;
import com.brokerapp.repository.CustomerRepository;
import com.brokerapp.service.CustomerAssetService;

/**
 * CustomerAssetService arabiriminin uygulaması
 */
@Service
public class CustomerAssetServiceImpl implements CustomerAssetService {

    private final CustomerAssetRepository customerAssetRepository;
    private final CustomerRepository customerRepository;
    private final AssetRepository assetRepository;

    /**
     * Constructor-based dependency injection
     */
    @Autowired
    public CustomerAssetServiceImpl(CustomerAssetRepository customerAssetRepository,
            CustomerRepository customerRepository, AssetRepository assetRepository) {
        this.customerAssetRepository = customerAssetRepository;
        this.customerRepository = customerRepository;
        this.assetRepository = assetRepository;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public CustomerAsset createOrUpdateCustomerAsset(CustomerAssetDTO customerAssetDTO) {
        Customer customer = customerRepository.findById(customerAssetDTO.getCustomerId())
                .orElseThrow(() -> new IllegalArgumentException("Customer not found: " + customerAssetDTO.getCustomerId()));

        Asset asset = assetRepository.findById(customerAssetDTO.getAssetId())
                .orElseThrow(() -> new IllegalArgumentException("Asset not found: " + customerAssetDTO.getAssetId()));

        // Check if customer has this asset
        Optional<CustomerAsset> existingAsset = customerAssetRepository.findByCustomerAndAsset(customer, asset);

        if (existingAsset.isPresent()) {
            // If exists, update it
            CustomerAsset customerAsset = existingAsset.get();
            customerAsset.setSize(customerAssetDTO.getSize());
            customerAsset.setUsableSize(customerAssetDTO.getUsableSize());
            customerAsset.setActive(customerAssetDTO.getActive());
            return customerAssetRepository.save(customerAsset);
        } else {
            // If not, create new
            CustomerAsset customerAsset = CustomerAsset.builder()
                    .customer(customer)
                    .asset(asset)
                    .size(customerAssetDTO.getSize())
                    .usableSize(customerAssetDTO.getUsableSize())
                    .active(customerAssetDTO.getActive())
                    .build();
            return customerAssetRepository.save(customerAsset);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<CustomerAsset> getCustomerAssetById(Long id) {
        return customerAssetRepository.findById(id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<CustomerAsset> getCustomerAssetsByCustomerId(Long customerId) {
        return customerAssetRepository.findByCustomerId(customerId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<CustomerAsset> getCustomerAssetByCustomerIdAndAssetId(Long customerId, Long assetId) {
        return customerAssetRepository.findByCustomerIdAndAssetId(customerId, assetId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<CustomerAsset> getCustomerAssetByCustomerIdAndAssetName(Long customerId, String assetName) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new IllegalArgumentException("Customer not found: " + customerId));

        Asset asset = assetRepository.findByAssetName(assetName)
                .orElseThrow(() -> new IllegalArgumentException("Asset not found: " + assetName));

        return customerAssetRepository.findByCustomerAndAsset(customer, asset);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public CustomerAsset updateCustomerAsset(Long id, CustomerAssetDTO customerAssetDTO) {
        CustomerAsset customerAsset = customerAssetRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Customer asset not found: " + id));

        if (customerAssetDTO.getCustomerId() != null) {
            Customer customer = customerRepository.findById(customerAssetDTO.getCustomerId())
                    .orElseThrow(() -> new IllegalArgumentException("Customer not found: " + customerAssetDTO.getCustomerId()));
            customerAsset.setCustomer(customer);
        }

        if (customerAssetDTO.getAssetId() != null) {
            Asset asset = assetRepository.findById(customerAssetDTO.getAssetId())
                    .orElseThrow(() -> new IllegalArgumentException("Asset not found: " + customerAssetDTO.getAssetId()));
            customerAsset.setAsset(asset);
        }

        customerAsset.setSize(customerAssetDTO.getSize());
        customerAsset.setUsableSize(customerAssetDTO.getUsableSize());
        customerAsset.setActive(customerAssetDTO.getActive());

        return customerAssetRepository.save(customerAsset);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void deleteCustomerAsset(Long id) {
        if (!customerAssetRepository.existsById(id)) {
            throw new IllegalArgumentException("Customer asset not found: " + id);
        }
        customerAssetRepository.deleteById(id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public CustomerAsset increaseCustomerAssetAmount(Long customerId, Long assetId, Double amount) {
        CustomerAsset customerAsset = getOrCreateCustomerAsset(customerId, assetId);
        customerAsset.setSize(customerAsset.getSize() + amount);
        customerAsset.setUsableSize(customerAsset.getUsableSize() + amount);
        return customerAssetRepository.save(customerAsset);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public CustomerAsset decreaseCustomerAssetAmount(Long customerId, Long assetId, Double amount) {
        CustomerAsset customerAsset = customerAssetRepository.findByCustomerIdAndAssetId(customerId, assetId)
                .orElseThrow(() -> new IllegalArgumentException("Customer asset not found"));

        if (customerAsset.getSize() < amount) {
            throw new IllegalArgumentException("Insufficient asset amount");
        }

        customerAsset.setSize(customerAsset.getSize() - amount);

        if (customerAsset.getUsableSize() < amount) {
            throw new IllegalArgumentException("Insufficient usable asset amount");
        }

        customerAsset.setUsableSize(customerAsset.getUsableSize() - amount);

        return customerAssetRepository.save(customerAsset);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public CustomerAsset increaseCustomerAssetUsableAmount(Long customerId, Long assetId, Double amount) {
        CustomerAsset customerAsset = getOrCreateCustomerAsset(customerId, assetId);
        customerAsset.setUsableSize(customerAsset.getUsableSize() + amount);
        return customerAssetRepository.save(customerAsset);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public CustomerAsset decreaseCustomerAssetUsableAmount(Long customerId, Long assetId, Double amount) {
        CustomerAsset customerAsset = customerAssetRepository.findByCustomerIdAndAssetId(customerId, assetId)
                .orElseThrow(() -> new IllegalArgumentException("Customer asset not found"));

        if (customerAsset.getUsableSize() < amount) {
            throw new IllegalArgumentException("Insufficient usable asset amount");
        }

        customerAsset.setUsableSize(customerAsset.getUsableSize() - amount);

        return customerAssetRepository.save(customerAsset);
    }

    /**
     * Müşteri varlığını getirir, yoksa yeni oluşturur
     */
    private CustomerAsset getOrCreateCustomerAsset(Long customerId, Long assetId) {
        Optional<CustomerAsset> customerAssetOpt = customerAssetRepository.findByCustomerIdAndAssetId(customerId, assetId);

        if (customerAssetOpt.isPresent()) {
            return customerAssetOpt.get();
        } else {
            Customer customer = customerRepository.findById(customerId)
                    .orElseThrow(() -> new IllegalArgumentException("Customer not found: " + customerId));

            Asset asset = assetRepository.findById(assetId)
                    .orElseThrow(() -> new IllegalArgumentException("Asset not found: " + assetId));

            CustomerAsset customerAsset = CustomerAsset.builder()
                    .customer(customer)
                    .asset(asset)
                    .size(0.0)
                    .usableSize(0.0)
                    .active(true)
                    .build();

            return customerAssetRepository.save(customerAsset);
        }
    }
}
