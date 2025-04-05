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
import org.springframework.security.crypto.password.PasswordEncoder;

import com.brokerapp.dto.CustomerDTO;
import com.brokerapp.model.Customer;
import com.brokerapp.repository.CustomerRepository;
import com.brokerapp.service.impl.CustomerServiceImpl;

/**
 * CustomerService için birim testleri
 */
@ExtendWith(MockitoExtension.class)
public class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private CustomerServiceImpl customerService;

    private CustomerDTO customerDTO;
    private Customer customer;

    @BeforeEach
    void setUp() {
        // Test için kullanılacak veri nesnelerini hazırla
        customerDTO = new CustomerDTO();
        customerDTO.setUsername("testcustomer");
        customerDTO.setPassword("password");
        customerDTO.setTryBalance(1000.0);
        customerDTO.setTryUsableBalance(1000.0);
        customerDTO.setActive(true);

        customer = Customer.builder()
                .id(1L)
                .username("testcustomer")
                .password("encodedPassword")
                .tryBalance(1000.0)
                .tryUsableBalance(1000.0)
                .active(true)
                .build();
    }

    @Test
    void createCustomer_Success() {
        // Mock davranışı ayarla
        when(customerRepository.existsByUsername(anyString())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(customerRepository.save(any(Customer.class))).thenReturn(customer);

        // Metodu çağır
        Customer result = customerService.createCustomer(customerDTO);

        // Sonuçları doğrula
        assertNotNull(result);
        assertEquals("testcustomer", result.getUsername());
        assertEquals("encodedPassword", result.getPassword());
        assertEquals(1000.0, result.getTryBalance());
        assertEquals(1000.0, result.getTryUsableBalance());
        assertTrue(result.isActive());

        // Mock metotların çağrıldığını doğrula
        verify(customerRepository).existsByUsername("testcustomer");
        verify(passwordEncoder).encode("password");
        verify(customerRepository).save(any(Customer.class));
    }

    @Test
    void createCustomer_DuplicateUsername() {
        // Mock davranışı ayarla
        when(customerRepository.existsByUsername(anyString())).thenReturn(true);

        // Metot çağrıldığında exception fırlatılmalı
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            customerService.createCustomer(customerDTO);
        });

        // Exception mesajını doğrula
        assertTrue(exception.getMessage().contains("Bu kullanıcı adı zaten kullanılıyor"));

        // save metodunun çağrılmadığını doğrula
        verify(customerRepository, never()).save(any(Customer.class));
    }

    @Test
    void getCustomerById_Success() {
        // Mock davranışı ayarla
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));

        // Metodu çağır
        Optional<Customer> result = customerService.getCustomerById(1L);

        // Sonuçları doğrula
        assertTrue(result.isPresent());
        assertEquals("testcustomer", result.get().getUsername());
        assertEquals(1000.0, result.get().getTryBalance());

        // Mock metotların çağrıldığını doğrula
        verify(customerRepository).findById(1L);
    }

    @Test
    void getAllCustomers_Success() {
        // Mock davranışı ayarla
        Customer customer2 = Customer.builder()
                .id(2L)
                .username("anothercustomer")
                .password("pass")
                .tryBalance(2000.0)
                .tryUsableBalance(2000.0)
                .active(true)
                .build();

        when(customerRepository.findAll()).thenReturn(Arrays.asList(customer, customer2));

        // Metodu çağır
        List<Customer> result = customerService.getAllCustomers();

        // Sonuçları doğrula
        assertEquals(2, result.size());
        assertEquals("testcustomer", result.get(0).getUsername());
        assertEquals("anothercustomer", result.get(1).getUsername());
        assertEquals(1000.0, result.get(0).getTryBalance());
        assertEquals(2000.0, result.get(1).getTryBalance());

        // Mock metodun çağrıldığını doğrula
        verify(customerRepository).findAll();
    }
}
