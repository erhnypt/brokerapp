package com.brokerapp.security.services;

import java.util.Collection;
import java.util.Collections;
import java.util.Objects;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.brokerapp.model.Admin;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Spring Security için Admin kullanıcısı detayları
 */
public class AdminDetailsImpl implements UserDetails {
    private static final long serialVersionUID = 1L;

    private Long id;
    private String username;
    @JsonIgnore
    private String password;
    private boolean active;
    private Collection<? extends GrantedAuthority> authorities;

    public AdminDetailsImpl(Long id, String username, String password, boolean active,
                          Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.active = active;
        this.authorities = authorities;
    }

    /**
     * Admin nesnesinden AdminDetailsImpl oluşturur
     * 
     * @param admin Admin nesnesi
     * @return AdminDetailsImpl nesnesi
     */
    public static AdminDetailsImpl build(Admin admin) {
        // Tüm adminler ROLE_ADMIN yetkisine sahiptir
        Collection<GrantedAuthority> authorities = Collections.singletonList(
                new SimpleGrantedAuthority("ROLE_ADMIN"));

        return new AdminDetailsImpl(
                admin.getId(),
                admin.getUsername(),
                admin.getPassword(),
                admin.isActive(),
                authorities);
    }

    /**
     * Kullanıcı ID'sini döner
     * 
     * @return kullanıcı ID'si
     */
    public Long getId() {
        return id;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
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
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AdminDetailsImpl admin = (AdminDetailsImpl) o;
        return Objects.equals(id, admin.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
} 