package com.brokerapp.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.brokerapp.dto.OrderDTO;
import com.brokerapp.enums.OrderStatus;
import com.brokerapp.model.Order;

/**
 * İşlemler için servis arayüzü
 */
public interface OrderService {

    /**
     * Yeni bir işlem oluşturur
     *
     * @param orderDTO işlem DTO nesnesi
     * @return oluşturulan işlem
     */
    Order createOrder(OrderDTO orderDTO);

    /**
     * ID'ye göre işlem bulur
     *
     * @param id işlem ID'si
     * @return bulunan işlem
     */
    Optional<Order> getOrderById(Long id);

    /**
     * Müşteri ID'sine göre işlemleri bulur
     *
     * @param customerId müşteri ID'si
     * @return işlem listesi
     */
    List<Order> getOrdersByCustomerId(Long customerId);

    /**
     * Müşteri ID'sine ve duruma göre işlemleri bulur
     *
     * @param customerId müşteri ID'si
     * @param status işlem durumu
     * @return işlem listesi
     */
    List<Order> getOrdersByCustomerIdAndStatus(Long customerId, OrderStatus status);

    /**
     * Müşteri ID'sine ve tarih aralığına göre işlemleri bulur
     *
     * @param customerId müşteri ID'si
     * @param startDate başlangıç tarihi
     * @param endDate bitiş tarihi
     * @return işlem listesi
     */
    List<Order> getOrdersByCustomerIdAndDateRange(Long customerId, LocalDateTime startDate, LocalDateTime endDate);

    /**
     * Müşteri ID'sine, duruma ve tarih aralığına göre işlemleri bulur
     *
     * @param customerId müşteri ID'si
     * @param status işlem durumu
     * @param startDate başlangıç tarihi
     * @param endDate bitiş tarihi
     * @return işlem listesi
     */
    List<Order> getOrdersByCustomerIdAndStatusAndDateRange(Long customerId, OrderStatus status,
            LocalDateTime startDate, LocalDateTime endDate);

    /**
     * Varlık ID'sine göre işlemleri bulur
     *
     * @param assetId varlık ID'si
     * @return işlem listesi
     */
    List<Order> getOrdersByAssetId(Long assetId);

    /**
     * Varlık ID'sine ve duruma göre işlemleri bulur
     *
     * @param assetId varlık ID'si
     * @param status işlem durumu
     * @return işlem listesi
     */
    List<Order> getOrdersByAssetIdAndStatus(Long assetId, OrderStatus status);

    /**
     * Tüm işlemleri getirir
     *
     * @return işlem listesi
     */
    List<Order> getAllOrders();

    /**
     * İşlem durumunu günceller
     *
     * @param id işlem ID'si
     * @param status yeni durum
     * @return güncellenen işlem
     */
    Order updateOrderStatus(Long id, OrderStatus status);

    /**
     * İşlemi iptal eder
     *
     * @param id işlem ID'si
     * @return iptal edilen işlem
     */
    Order cancelOrder(Long id);

    /**
     * Eşleşen işlemleri kontrol eder ve işler
     *
     * @param orderId yeni oluşturulan işlem ID'si
     * @return eşleşen işlem listesi
     */
    List<Order> processMatchingOrders(Long orderId);

    /**
     * Admin tarafından bekleyen bir siparişi eşleştirir Varlık kullanım
     * miktarlarını günceller
     *
     * @param orderId eşleştirilecek sipariş ID'si
     * @return eşleştirilen sipariş
     */
    Order matchOrder(Long orderId);

    /**
     * Bekleyen tüm siparişleri otomatik olarak eşleştirir Bu işlem, tüm PENDING
     * durumundaki siparişleri MATCHED durumuna getirir ve ilgili varlık ve
     * bakiye güncellemelerini yapar
     *
     * @return Eşleştirilen sipariş sayısı
     */
    int matchAllPendingOrders();
}
