package com.brokerapp.security.services;

import java.util.Collection;
import java.util.Collections;
import java.util.Objects;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.brokerapp.model.Customer;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Spring Security UserDetails arayüzünün Müşteri için implementasyonu
 */
public class CustomerDetailsImpl implements UserDetails {

    private static final long serialVersionUID = 1L;

    private Long id;
    private String username;
    @JsonIgnore
    private String password;
    private boolean active;

    public CustomerDetailsImpl(Long id, String username, String password, boolean active) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.active = active;
    }

    /**
     * Customer nesnesinden UserDetails oluşturur
     *
     * @param customer Müşteri
     * @return CustomerDetailsImpl nesnesi
     */
    public static CustomerDetailsImpl build(Customer customer) {
        return new CustomerDetailsImpl(
                customer.getId(),
                customer.getUsername(),
                customer.getPassword(),
                customer.isActive());
    }

    /**
     * Kullanıcının yetkilerini döndürür
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_CUSTOMER"));
    }

    /**
     * Kullanıcı ID'sini döndürür
     *
     * @return Kullanıcı ID'si
     */
    public Long getId() {
        return id;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return active;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CustomerDetailsImpl user = (CustomerDetailsImpl) o;
        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
