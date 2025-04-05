package com.brokerapp.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.brokerapp.dto.OrderDTO;
import com.brokerapp.enums.OrderStatus;
import com.brokerapp.model.Order;
import com.brokerapp.service.OrderService;

import jakarta.validation.Valid;

/**
 * İşlemler için REST API controller
 */
@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    /**
     * Constructor-based dependency injection
     */
    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    /**
     * Yeni bir işlem oluşturur
     *
     * @param orderDTO işlem DTO nesnesi
     * @return oluşturulan işlem ve 201 Created status
     */
    @PostMapping
    public ResponseEntity<Order> createOrder(@Valid @RequestBody OrderDTO orderDTO) {
        Order createdOrder = orderService.createOrder(orderDTO);
        return new ResponseEntity<>(createdOrder, HttpStatus.CREATED);
    }

    /**
     * ID'ye göre işlem getirir
     *
     * @param id işlem ID'si
     * @return bulunan işlem
     */
    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrderById(@PathVariable Long id) {
        return orderService.getOrderById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Müşteri ID'sine göre işlem listesi getirir
     *
     * @param customerId müşteri ID'si
     * @return işlem listesi
     */
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<Order>> getOrdersByCustomerId(@PathVariable Long customerId) {
        List<Order> orders = orderService.getOrdersByCustomerId(customerId);
        return ResponseEntity.ok(orders);
    }

    /**
     * Müşteri ID'si ve duruma göre işlem listesi getirir
     *
     * @param customerId müşteri ID'si
     * @param status işlem durumu
     * @return işlem listesi
     */
    @GetMapping("/customer/{customerId}/status/{status}")
    public ResponseEntity<List<Order>> getOrdersByCustomerIdAndStatus(@PathVariable Long customerId, @PathVariable OrderStatus status) {
        List<Order> orders = orderService.getOrdersByCustomerIdAndStatus(customerId, status);
        return ResponseEntity.ok(orders);
    }

    /**
     * Müşteri ID'si ve tarih aralığına göre işlem listesi getirir
     *
     * @param customerId müşteri ID'si
     * @param startDate başlangıç tarihi
     * @param endDate bitiş tarihi
     * @return işlem listesi
     */
    @GetMapping("/customer/{customerId}/dateRange")
    public ResponseEntity<List<Order>> getOrdersByCustomerIdAndDateRange(
            @PathVariable Long customerId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        List<Order> orders = orderService.getOrdersByCustomerIdAndDateRange(customerId, startDate, endDate);
        return ResponseEntity.ok(orders);
    }

    /**
     * Müşteri ID'si, durumu ve tarih aralığına göre işlem listesi getirir
     *
     * @param customerId müşteri ID'si
     * @param status işlem durumu
     * @param startDate başlangıç tarihi
     * @param endDate bitiş tarihi
     * @return işlem listesi
     */
    @GetMapping("/customer/{customerId}/status/{status}/dateRange")
    public ResponseEntity<List<Order>> getOrdersByCustomerIdAndStatusAndDateRange(
            @PathVariable Long customerId,
            @PathVariable OrderStatus status,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        List<Order> orders = orderService.getOrdersByCustomerIdAndStatusAndDateRange(customerId, status, startDate, endDate);
        return ResponseEntity.ok(orders);
    }

    /**
     * Varlık ID'sine göre işlem listesi getirir
     *
     * @param assetId varlık ID'si
     * @return işlem listesi
     */
    @GetMapping("/asset/{assetId}")
    public ResponseEntity<List<Order>> getOrdersByAssetId(@PathVariable Long assetId) {
        List<Order> orders = orderService.getOrdersByAssetId(assetId);
        return ResponseEntity.ok(orders);
    }

    /**
     * Varlık ID'si ve duruma göre işlem listesi getirir
     *
     * @param assetId varlık ID'si
     * @param status işlem durumu
     * @return işlem listesi
     */
    @GetMapping("/asset/{assetId}/status/{status}")
    public ResponseEntity<List<Order>> getOrdersByAssetIdAndStatus(@PathVariable Long assetId, @PathVariable OrderStatus status) {
        List<Order> orders = orderService.getOrdersByAssetIdAndStatus(assetId, status);
        return ResponseEntity.ok(orders);
    }

    /**
     * Tüm işlemleri listeler
     *
     * @return işlem listesi
     */
    @GetMapping
    public ResponseEntity<List<Order>> getAllOrders() {
        List<Order> orders = orderService.getAllOrders();
        return ResponseEntity.ok(orders);
    }

    /**
     * İşlem durumunu günceller
     *
     * @param id işlem ID'si
     * @param status yeni durum
     * @return güncellenen işlem
     */
    @PutMapping("/{id}/status/{status}")
    public ResponseEntity<Order> updateOrderStatus(@PathVariable Long id, @PathVariable OrderStatus status) {
        Order updatedOrder = orderService.updateOrderStatus(id, status);
        return ResponseEntity.ok(updatedOrder);
    }

    /**
     * İşlemi iptal eder
     *
     * @param id işlem ID'si
     * @return iptal edilen işlem
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Order> cancelOrder(@PathVariable Long id) {
        Order canceledOrder = orderService.cancelOrder(id);
        return ResponseEntity.ok(canceledOrder);
    }
}
