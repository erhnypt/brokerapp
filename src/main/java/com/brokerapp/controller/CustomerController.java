package com.brokerapp.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.brokerapp.enums.OrderStatus;
import com.brokerapp.model.Customer;
import com.brokerapp.model.CustomerAsset;
import com.brokerapp.model.Order;
import com.brokerapp.security.services.CustomerDetailsImpl;
import com.brokerapp.service.CustomerAssetService;
import com.brokerapp.service.CustomerService;
import com.brokerapp.service.OrderService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/customers")
@Tag(name = "Müşteri İşlemleri", description = "Müşteri için işlemler")
@SecurityRequirement(name = "bearerAuth") // Swagger'da güvenlik şeması kullanımı
public class CustomerController {

    private static final Logger log = LoggerFactory.getLogger(CustomerController.class);

    @Autowired
    private CustomerService customerService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private CustomerAssetService customerAssetService;

    /**
     * Mevcut müşteri bilgilerini getirir. Bu endpoint sadece müşteri rolüne
     * sahip kullanıcılar tarafından kullanılabilir.
     *
     * @return Müşteri bilgileri
     */
    @GetMapping("/me")
    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    @Operation(
            summary = "Mevcut müşteri bilgilerini göster",
            description = "Giriş yapmış müşterinin kendi bilgilerini getirir",
            responses = {
                @ApiResponse(responseCode = "200", description = "Başarılı"),
                @ApiResponse(responseCode = "401", description = "Yetkilendirme hatası"),
                @ApiResponse(responseCode = "404", description = "Müşteri bulunamadı")
            }
    )
    public ResponseEntity<Customer> getCurrentCustomer() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomerDetailsImpl customerDetails = (CustomerDetailsImpl) authentication.getPrincipal();

        log.info("Current customer information requested. Customer ID: {}", customerDetails.getId());

        return customerService.getCustomerById(customerDetails.getId())
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResponseStatusException(org.springframework.http.HttpStatus.NOT_FOUND,
                "Müşteri bulunamadı: " + customerDetails.getId()));
    }

    /**
     * Müşterinin kendi varlıklarını getirir
     *
     * @return Müşteri varlıkları listesi
     */
    @GetMapping("/me/assets")
    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    @Operation(
            summary = "Müşteri varlıklarını göster",
            description = "Giriş yapmış müşterinin varlıklarını getirir",
            responses = {
                @ApiResponse(responseCode = "200", description = "Başarılı"),
                @ApiResponse(responseCode = "401", description = "Yetkilendirme hatası"),
                @ApiResponse(responseCode = "404", description = "Müşteri bulunamadı")
            }
    )
    public ResponseEntity<List<CustomerAsset>> getCurrentCustomerAssets() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomerDetailsImpl customerDetails = (CustomerDetailsImpl) authentication.getPrincipal();

        log.info("Customer assets requested. Customer ID: {}", customerDetails.getId());

        List<CustomerAsset> assets = customerAssetService.getCustomerAssetsByCustomerId(customerDetails.getId());
        return ResponseEntity.ok(assets);
    }

    /**
     * Müşterinin kendi siparişlerini getirir
     *
     * @return Müşteri siparişleri listesi
     */
    @GetMapping("/me/orders")
    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    @Operation(
            summary = "Müşteri siparişlerini göster",
            description = "Giriş yapmış müşterinin siparişlerini getirir",
            responses = {
                @ApiResponse(responseCode = "200", description = "Başarılı"),
                @ApiResponse(responseCode = "401", description = "Yetkilendirme hatası"),
                @ApiResponse(responseCode = "404", description = "Müşteri bulunamadı")
            }
    )
    public ResponseEntity<List<Order>> getCurrentCustomerOrders() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomerDetailsImpl customerDetails = (CustomerDetailsImpl) authentication.getPrincipal();

        log.info("Customer orders requested. Customer ID: {}", customerDetails.getId());

        List<Order> orders = orderService.getOrdersByCustomerId(customerDetails.getId());
        return ResponseEntity.ok(orders);
    }

    /**
     * Müşterinin belirli durumdaki siparişlerini getirir
     *
     * @param status Sipariş durumu
     * @return Müşteri siparişleri listesi
     */
    @GetMapping("/me/orders/status/{status}")
    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    @Operation(
            summary = "Müşterinin belirli durumdaki siparişlerini göster",
            description = "Giriş yapmış müşterinin belirli durumdaki siparişlerini getirir",
            responses = {
                @ApiResponse(responseCode = "200", description = "Başarılı"),
                @ApiResponse(responseCode = "401", description = "Yetkilendirme hatası"),
                @ApiResponse(responseCode = "404", description = "Müşteri bulunamadı")
            }
    )
    public ResponseEntity<List<Order>> getCurrentCustomerOrdersByStatus(@PathVariable OrderStatus status) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomerDetailsImpl customerDetails = (CustomerDetailsImpl) authentication.getPrincipal();

        log.info("Customer orders with status {} requested. Customer ID: {}", status, customerDetails.getId());

        List<Order> orders = orderService.getOrdersByCustomerIdAndStatus(customerDetails.getId(), status);
        return ResponseEntity.ok(orders);
    }

    /**
     * Müşterinin belirli tarih aralığındaki siparişlerini getirir
     *
     * @param startDate Başlangıç tarihi
     * @param endDate Bitiş tarihi
     * @return Müşteri siparişleri listesi
     */
    @GetMapping("/me/orders/dateRange")
    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    @Operation(
            summary = "Müşterinin belirli tarih aralığındaki siparişlerini göster",
            description = "Giriş yapmış müşterinin belirli tarih aralığındaki siparişlerini getirir",
            responses = {
                @ApiResponse(responseCode = "200", description = "Başarılı"),
                @ApiResponse(responseCode = "401", description = "Yetkilendirme hatası"),
                @ApiResponse(responseCode = "404", description = "Müşteri bulunamadı")
            }
    )
    public ResponseEntity<List<Order>> getCurrentCustomerOrdersByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomerDetailsImpl customerDetails = (CustomerDetailsImpl) authentication.getPrincipal();

        log.info("Customer orders between dates {} - {} requested. Customer ID: {}",
                startDate, endDate, customerDetails.getId());

        List<Order> orders = orderService.getOrdersByCustomerIdAndDateRange(customerDetails.getId(), startDate, endDate);
        return ResponseEntity.ok(orders);
    }
}
