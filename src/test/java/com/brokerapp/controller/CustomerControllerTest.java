package com.brokerapp.controller;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.brokerapp.enums.OrderStatus;
import com.brokerapp.model.Customer;
import com.brokerapp.model.CustomerAsset;
import com.brokerapp.model.Order;
import com.brokerapp.security.services.CustomerDetailsImpl;
import com.brokerapp.service.CustomerAssetService;
import com.brokerapp.service.CustomerService;
import com.brokerapp.service.OrderService;

/**
 * CustomerController için birim testler
 */
@WebMvcTest(CustomerController.class)
class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CustomerService customerService;

    @MockBean
    private OrderService orderService;

    @MockBean
    private CustomerAssetService customerAssetService;

    private Customer testCustomer;
    private CustomerDetailsImpl customerDetails;

    @BeforeEach
    void setUp() {
        // Test müşterisi oluştur
        testCustomer = Customer.builder()
                .id(1L)
                .username("musteri1")
                .password("sifre123")
                .tryBalance(10000.0)
                .tryUsableBalance(10000.0)
                .active(true)
                .build();

        // CustomerDetailsImpl nesnesi oluştur
        customerDetails = CustomerDetailsImpl.build(testCustomer);

        // SecurityContext'e müşteri kullanıcı bilgilerini ekle
        UsernamePasswordAuthenticationToken authentication = 
                new UsernamePasswordAuthenticationToken(customerDetails, null, customerDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @Test
    @WithMockUser(roles = "CUSTOMER")
    @DisplayName("Mevcut müşteri bilgilerini getir - Başarılı")
    void getCurrentCustomer_ShouldReturnCustomerInfo() throws Exception {
        // Arrange
        when(customerService.getCustomerById(anyLong())).thenReturn(Optional.of(testCustomer));

        // Act & Assert
        mockMvc.perform(get("/api/customers/me")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(testCustomer.getId()))
                .andExpect(jsonPath("$.username").value(testCustomer.getUsername()))
                .andExpect(jsonPath("$.tryBalance").value(testCustomer.getTryBalance()))
                .andExpect(jsonPath("$.tryUsableBalance").value(testCustomer.getTryUsableBalance()));

        // Verify
        verify(customerService, times(1)).getCustomerById(eq(1L));
    }

    @Test
    @WithMockUser(roles = "CUSTOMER")
    @DisplayName("Müşteri varlıklarını getir - Başarılı")
    void getCurrentCustomerAssets_ShouldReturnAssets() throws Exception {
        // Arrange
        CustomerAsset asset = CustomerAsset.builder()
                .id(1L)
                .customer(testCustomer)
                .size(5.0)
                .usableSize(5.0)
                .build();
        
        when(customerAssetService.getCustomerAssetsByCustomerId(anyLong()))
                .thenReturn(Collections.singletonList(asset));

        // Act & Assert
        mockMvc.perform(get("/api/customers/me/assets")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(asset.getId()))
                .andExpect(jsonPath("$[0].size").value(asset.getSize()))
                .andExpect(jsonPath("$[0].usableSize").value(asset.getUsableSize()));

        // Verify
        verify(customerAssetService, times(1)).getCustomerAssetsByCustomerId(eq(1L));
    }

    @Test
    @WithMockUser(roles = "CUSTOMER")
    @DisplayName("Müşteri siparişlerini getir - Başarılı")
    void getCurrentCustomerOrders_ShouldReturnOrders() throws Exception {
        // Arrange
        Order order = Order.builder()
                .id(1L)
                .customer(testCustomer)
                .status(OrderStatus.PENDING)
                .createDate(LocalDateTime.now())
                .build();
        
        when(orderService.getOrdersByCustomerId(anyLong()))
                .thenReturn(Collections.singletonList(order));

        // Act & Assert
        mockMvc.perform(get("/api/customers/me/orders")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(order.getId()))
                .andExpect(jsonPath("$[0].status").value(order.getStatus().toString()));

        // Verify
        verify(orderService, times(1)).getOrdersByCustomerId(eq(1L));
    }

    @Test
    @WithMockUser(roles = "CUSTOMER")
    @DisplayName("Müşteri duruma göre siparişlerini getir - Başarılı")
    void getCurrentCustomerOrdersByStatus_ShouldReturnFilteredOrders() throws Exception {
        // Arrange
        Order order = Order.builder()
                .id(1L)
                .customer(testCustomer)
                .status(OrderStatus.PENDING)
                .createDate(LocalDateTime.now())
                .build();
        
        when(orderService.getOrdersByCustomerIdAndStatus(anyLong(), any(OrderStatus.class)))
                .thenReturn(Collections.singletonList(order));

        // Act & Assert
        mockMvc.perform(get("/api/customers/me/orders/status/PENDING")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(order.getId()))
                .andExpect(jsonPath("$[0].status").value(OrderStatus.PENDING.toString()));

        // Verify
        verify(orderService, times(1)).getOrdersByCustomerIdAndStatus(eq(1L), eq(OrderStatus.PENDING));
    }
} 