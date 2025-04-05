package com.brokerapp.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.brokerapp.enums.OrderStatus;
import com.brokerapp.model.Asset;
import com.brokerapp.model.Customer;
import com.brokerapp.model.Order;

/**
 * İşlemler için repository arayüzü
 */
@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    /**
     * Müşteriye ait işlemleri bulur
     *
     * @param customer müşteri
     * @return işlem listesi
     */
    List<Order> findByCustomer(Customer customer);

    /**
     * Müşteriye ait belirli durumda olan işlemleri bulur
     *
     * @param customer müşteri
     * @param status işlem durumu
     * @return işlem listesi
     */
    List<Order> findByCustomerAndStatus(Customer customer, OrderStatus status);

    /**
     * Varlığa ait işlemleri bulur
     *
     * @param asset varlık
     * @return işlem listesi
     */
    List<Order> findByAsset(Asset asset);

    /**
     * Varlığa ait belirli durumda olan işlemleri bulur
     *
     * @param asset varlık
     * @param status işlem durumu
     * @return işlem listesi
     */
    List<Order> findByAssetAndStatus(Asset asset, OrderStatus status);

    /**
     * Müşteriye ait işlemleri belirli bir tarih aralığında bulur
     *
     * @param customer müşteri
     * @param startDate başlangıç tarihi
     * @param endDate bitiş tarihi
     * @return işlem listesi
     */
    List<Order> findByCustomerAndCreateDateBetween(Customer customer, LocalDateTime startDate, LocalDateTime endDate);

    /**
     * Müşteriye ait belirli durumda olan işlemleri tarih aralığında bulur
     *
     * @param customer müşteri
     * @param status işlem durumu
     * @param startDate başlangıç tarihi
     * @param endDate bitiş tarihi
     * @return işlem listesi
     */
    List<Order> findByCustomerAndStatusAndCreateDateBetween(Customer customer, OrderStatus status,
            LocalDateTime startDate, LocalDateTime endDate);

    /**
     * Bekleyen (PENDING) durumda olan tüm işlemleri bulur
     *
     * @return bekleyen işlemler listesi
     */
    List<Order> findByStatus(OrderStatus status);
}
