package com.brokerapp.security.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.brokerapp.model.Customer;
import com.brokerapp.repository.CustomerRepository;

/**
 * Müşteri kullanıcıları için UserDetailsService implementasyonu
 */
@Service("customerDetailsService")
public class CustomerDetailsServiceImpl implements UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(CustomerDetailsServiceImpl.class);

    @Autowired
    private CustomerRepository customerRepository;

    /**
     * Verilen kullanıcı adına göre müşteri detaylarını yükler
     *
     * @param username kullanıcı adı
     * @return kullanıcı detayları
     * @throws UsernameNotFoundException kullanıcı bulunamazsa
     */
    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        logger.debug("loadUserByUsername called (Customer): {}", username);

        Customer customer = customerRepository.findByUsername(username)
                .orElseThrow(() -> {
                    logger.error("Customer not found: {}", username);
                    return new UsernameNotFoundException("Customer not found: " + username);
                });

        logger.debug("Customer found: {}", username);
        return CustomerDetailsImpl.build(customer);
    }
}
