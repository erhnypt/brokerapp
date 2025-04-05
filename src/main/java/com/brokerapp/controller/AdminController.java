package com.brokerapp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.brokerapp.dto.AssetDTO;
import com.brokerapp.dto.CustomerAssetDTO;
import com.brokerapp.dto.CustomerDTO;
import com.brokerapp.dto.UserDTO;
import com.brokerapp.model.Asset;
import com.brokerapp.model.Customer;
import com.brokerapp.model.CustomerAsset;
import com.brokerapp.model.Order;
import com.brokerapp.model.User;
import com.brokerapp.service.AssetService;
import com.brokerapp.service.CustomerAssetService;
import com.brokerapp.service.CustomerService;
import com.brokerapp.service.OrderService;
import com.brokerapp.service.UserService;

import jakarta.validation.Valid;

/**
 * Admin işlemleri için REST controller
 */
@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final UserService userService;
    private final CustomerService customerService;
    private final AssetService assetService;
    private final CustomerAssetService customerAssetService;
    private final OrderService orderService;

    /**
     * Constructor-based dependency injection
     */
    @Autowired
    public AdminController(UserService userService, CustomerService customerService,
            AssetService assetService, CustomerAssetService customerAssetService,
            OrderService orderService) {
        this.userService = userService;
        this.customerService = customerService;
        this.assetService = assetService;
        this.customerAssetService = customerAssetService;
        this.orderService = orderService;
    }

    /**
     * User oluşturma endpoint'i
     *
     * @param userDTO Oluşturulacak kullanıcı bilgileri
     * @return Oluşturulan kullanıcı ve 201 Created status
     */
    @PostMapping("/users")
    public ResponseEntity<User> createUser(@Valid @RequestBody UserDTO userDTO) {
        User createdUser = userService.createUser(userDTO);
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }

    /**
     * Tüm kullanıcıları listeleme endpoint'i
     *
     * @return Kullanıcı listesi
     */
    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    /**
     * Customer oluşturma endpoint'i
     *
     * @param customerDTO Oluşturulacak müşteri bilgileri
     * @return Oluşturulan müşteri ve 201 Created status
     */
    @PostMapping("/customers")
    public ResponseEntity<Customer> createCustomer(@Valid @RequestBody CustomerDTO customerDTO) {
        Customer createdCustomer = customerService.createCustomer(customerDTO);
        return new ResponseEntity<>(createdCustomer, HttpStatus.CREATED);
    }

    /**
     * Tüm müşterileri listeleme endpoint'i
     *
     * @return Müşteri listesi
     */
    @GetMapping("/customers")
    public ResponseEntity<List<Customer>> getAllCustomers() {
        List<Customer> customers = customerService.getAllCustomers();
        return ResponseEntity.ok(customers);
    }

    /**
     * Asset (Genel Varlık/Hisse Senedi) oluşturma endpoint'i
     *
     * @param assetDTO Oluşturulacak varlık bilgileri
     * @return Oluşturulan varlık ve 201 Created status
     */
    @PostMapping("/assets")
    public ResponseEntity<Asset> createAsset(@Valid @RequestBody AssetDTO assetDTO) {
        Asset createdAsset = assetService.createAsset(assetDTO);
        return new ResponseEntity<>(createdAsset, HttpStatus.CREATED);
    }

    /**
     * Tüm varlıkları listeleme endpoint'i
     *
     * @return Varlık listesi
     */
    @GetMapping("/assets")
    public ResponseEntity<List<Asset>> getAllAssets() {
        List<Asset> assets = assetService.getAllAssets();
        return ResponseEntity.ok(assets);
    }

    /**
     * Müşteri varlığı oluşturma endpoint'i
     *
     * @param customerAssetDTO Oluşturulacak müşteri varlığı bilgileri
     * @return Oluşturulan müşteri varlığı ve 201 Created status
     */
    @PostMapping("/customer-assets")
    public ResponseEntity<CustomerAsset> createCustomerAsset(@Valid @RequestBody CustomerAssetDTO customerAssetDTO) {
        CustomerAsset createdCustomerAsset = customerAssetService.createOrUpdateCustomerAsset(customerAssetDTO);
        return new ResponseEntity<>(createdCustomerAsset, HttpStatus.CREATED);
    }

    /**
     * Belirli bir müşteriye ait varlıkları listeleme endpoint'i
     *
     * @param customerId Müşteri ID'si
     * @return Varlık listesi
     */
    @GetMapping("/customers/{customerId}/assets")
    public ResponseEntity<List<CustomerAsset>> getAssetsByCustomerId(@PathVariable Long customerId) {
        List<CustomerAsset> customerAssets = customerAssetService.getCustomerAssetsByCustomerId(customerId);
        return ResponseEntity.ok(customerAssets);
    }

    /**
     * Bekleyen siparişi eşleştirme endpoint'i (Bonus 2) Bu işlem, siparişi
     * MATCHED durumuna getirir ve ilgili varlık bakiyelerini günceller
     *
     * @param orderId Eşleştirilecek sipariş ID'si
     * @return Eşleştirilen sipariş
     */
    @PutMapping("/orders/{orderId}/match")
    public ResponseEntity<Order> matchOrder(@PathVariable Long orderId) {
        Order matchedOrder = orderService.matchOrder(orderId);
        return ResponseEntity.ok(matchedOrder);
    }

    /**
     * Bekleyen tüm siparişleri eşleştirme endpoint'i Tüm PENDING durumundaki
     * siparişleri MATCHED durumuna getirir ve ilgili varlık ve bakiye
     * güncellemelerini yapar
     *
     * @return Eşleştirilen sipariş sayısı bilgisi
     */
    @PutMapping("/orders/match-all")
    public ResponseEntity<String> matchAllPendingOrders() {
        int matchedCount = orderService.matchAllPendingOrders();
        return ResponseEntity.ok("Toplam " + matchedCount + " adet bekleyen sipariş başarıyla eşleştirildi.");
    }
}
