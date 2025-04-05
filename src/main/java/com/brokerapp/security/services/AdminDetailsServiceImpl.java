package com.brokerapp.security.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.brokerapp.model.Admin;
import com.brokerapp.repository.AdminRepository;

/**
 * UserDetailsService implementation for Admin users
 */
@Service
@Primary
public class AdminDetailsServiceImpl implements UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(AdminDetailsServiceImpl.class);

    @Autowired
    private AdminRepository adminRepository;

    /**
     * Loads user details by username
     *
     * @param username username
     * @return user details
     * @throws UsernameNotFoundException if user not found
     */
    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        logger.debug("loadUserByUsername called: {}", username);

        Admin admin = adminRepository.findByUsername(username)
                .orElseThrow(() -> {
                    logger.error("User not found: {}", username);
                    return new UsernameNotFoundException("User not found: " + username);
                });

        logger.debug("User found: {}", username);
        return AdminDetailsImpl.build(admin);
    }
}
