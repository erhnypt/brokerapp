package com.brokerapp.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.brokerapp.dto.CustomerDTO;
import com.brokerapp.model.Customer;
import com.brokerapp.repository.CustomerRepository;
import com.brokerapp.service.CustomerService;

/**
 * Müşteri servis arabiriminin uygulaması
 */
@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Constructor-based dependency injection
     */
    @Autowired
    public CustomerServiceImpl(CustomerRepository customerRepository, PasswordEncoder passwordEncoder) {
        this.customerRepository = customerRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public Customer createCustomer(CustomerDTO customerDTO) {
        if (!isUsernameAvailable(customerDTO.getUsername())) {
            throw new IllegalArgumentException("Bu kullanıcı adı zaten kullanılıyor: " + customerDTO.getUsername());
        }

        Customer customer = Customer.builder()
                .username(customerDTO.getUsername())
                .password(passwordEncoder.encode(customerDTO.getPassword()))
                .tryBalance(customerDTO.getTryBalance())
                .tryUsableBalance(customerDTO.getTryUsableBalance())
                .active(customerDTO.getActive())
                .build();

        return customerRepository.save(customer);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Customer> getCustomerById(Long id) {
        return customerRepository.findById(id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Customer> getCustomerByUsername(String username) {
        return customerRepository.findByUsername(username);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public Customer updateCustomer(Long id, CustomerDTO customerDTO) {
        Customer existingCustomer = customerRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("ID ile müşteri bulunamadı: " + id));

        // If username has changed and new username is used by another customer
        if (!existingCustomer.getUsername().equals(customerDTO.getUsername())
                && !isUsernameAvailable(customerDTO.getUsername())) {
            throw new IllegalArgumentException("Bu kullanıcı adı zaten kullanılıyor: " + customerDTO.getUsername());
        }

        existingCustomer.setUsername(customerDTO.getUsername());

        // If password has changed, encrypt it
        if (customerDTO.getPassword() != null && !customerDTO.getPassword().isEmpty()) {
            existingCustomer.setPassword(passwordEncoder.encode(customerDTO.getPassword()));
        }

        existingCustomer.setTryBalance(customerDTO.getTryBalance());
        existingCustomer.setTryUsableBalance(customerDTO.getTryUsableBalance());
        existingCustomer.setActive(customerDTO.getActive());

        return customerRepository.save(existingCustomer);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void deleteCustomer(Long id) {
        if (!customerRepository.existsById(id)) {
            throw new IllegalArgumentException("ID ile müşteri bulunamadı: " + id);
        }
        customerRepository.deleteById(id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isUsernameAvailable(String username) {
        return !customerRepository.existsByUsername(username);
    }
}
