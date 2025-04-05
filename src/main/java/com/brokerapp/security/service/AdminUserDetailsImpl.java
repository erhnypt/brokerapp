package com.brokerapp.security.service;

import com.brokerapp.model.Admin;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

/**
 * Spring Security UserDetails arayüzünün Admin için implementasyonu
 */
@Getter
public class AdminUserDetailsImpl implements UserDetails {

    private final Admin admin;

    /**
     * Admin ile constructor
     *
     * @param admin admin kullanıcı
     */
    public AdminUserDetailsImpl(Admin admin) {
        this.admin = admin;
    }

    /**
     * Admin yetkilerini döndürür
     *
     * @return yetki koleksiyonu
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_ADMIN"));
    }

    /**
     * Admin şifresini döndürür
     *
     * @return şifre
     */
    @Override
    public String getPassword() {
        return admin.getPassword();
    }

    /**
     * Admin kullanıcı adını döndürür
     *
     * @return kullanıcı adı
     */
    @Override
    public String getUsername() {
        return admin.getUsername();
    }

    /**
     * Hesabın süresinin dolup dolmadığını kontrol eder
     *
     * @return her zaman true
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * Hesabın kilitli olup olmadığını kontrol eder
     *
     * @return her zaman true
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * Kimlik bilgilerinin süresinin dolup dolmadığını kontrol eder
     *
     * @return her zaman true
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * Hesabın aktif olup olmadığını kontrol eder
     *
     * @return hesap aktifse true, değilse false
     */
    @Override
    public boolean isEnabled() {
        return admin.isActive();
    }
}
