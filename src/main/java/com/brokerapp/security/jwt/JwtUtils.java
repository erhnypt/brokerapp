package com.brokerapp.security.jwt;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.brokerapp.security.services.AdminDetailsImpl;
import com.brokerapp.security.services.CustomerDetailsImpl;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;

/**
 * JWT token üretimi ve doğrulaması için yardımcı sınıf
 */
@Component
public class JwtUtils {

    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration}")
    private int jwtExpirationMs;

    /**
     * Kullanıcı kimlik doğrulama nesnesinden JWT token oluşturur
     *
     * @param authentication kimlik doğrulama nesnesi
     * @return oluşturulan JWT token
     */
    public String generateJwtToken(Authentication authentication) {
        String username = "";
        String role = "ROLE_CUSTOMER";

        if (authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            username = userDetails.getUsername();

            if (userDetails instanceof AdminDetailsImpl) {
                role = "ROLE_ADMIN";
            }
        } else {
            username = authentication.getName();

            for (var authority : authentication.getAuthorities()) {
                if ("ROLE_ADMIN".equals(authority.getAuthority())) {
                    role = "ROLE_ADMIN";
                    break;
                }
            }
        }

        logger.debug("JWT token oluşturuluyor: kullanıcı={}, rol={}", username, role);

        byte[] keyBytes = jwtSecret.getBytes(StandardCharsets.UTF_8);

        return Jwts.builder()
                .setSubject(username)
                .claim("role", role)
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(Keys.hmacShaKeyFor(keyBytes))
                .compact();
    }

    /**
     * JWT token'dan kullanıcı adını çıkarır
     *
     * @param token JWT token
     * @return çıkarılan kullanıcı adı
     */
    public String getUserNameFromJwtToken(String token) {
        byte[] keyBytes = jwtSecret.getBytes(StandardCharsets.UTF_8);

        Claims claims = Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(keyBytes))
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }

    /**
     * JWT token'dan kullanıcı rolünü çıkarır
     *
     * @param token JWT token
     * @return çıkarılan kullanıcı rolü
     */
    public String getRoleFromJwtToken(String token) {
        byte[] keyBytes = jwtSecret.getBytes(StandardCharsets.UTF_8);

        Claims claims = Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(keyBytes))
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.get("role", String.class);
    }

    /**
     * JWT token'ın geçerliliğini doğrular
     *
     * @param authToken JWT token
     * @return geçerli ise true, değilse false
     */
    public boolean validateJwtToken(String authToken) {
        try {
            byte[] keyBytes = jwtSecret.getBytes(StandardCharsets.UTF_8);

            Jwts.parserBuilder()
                    .setSigningKey(Keys.hmacShaKeyFor(keyBytes))
                    .build()
                    .parseClaimsJws(authToken);
            return true;
        } catch (SignatureException e) {
            logger.error("Geçersiz JWT imzası: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            logger.error("Geçersiz JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.error("JWT token süresi doldu: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("JWT token desteklenmiyor: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string boş: {}", e.getMessage());
        }

        return false;
    }
}
