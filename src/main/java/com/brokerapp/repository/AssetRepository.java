package com.brokerapp.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.brokerapp.model.Asset;
import com.brokerapp.model.Customer;

@Repository
public interface AssetRepository extends JpaRepository<Asset, Long> {

    List<Asset> findByCustomer(Customer customer);

    Optional<Asset> findByCustomerAndAssetName(Customer customer, String assetName);
}
