package com.brokerapp.security.jwt;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.brokerapp.security.services.AdminDetailsServiceImpl;
import com.brokerapp.security.services.CustomerDetailsServiceImpl;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * JWT kimlik doğrulama filtresi. Her HTTP isteğini yakalayıp JWT token'ı
 * doğrular.
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private AdminDetailsServiceImpl adminDetailsService;

    @Autowired
    private CustomerDetailsServiceImpl customerDetailsService;

    /**
     * HTTP isteklerini filtreleme ve JWT token doğrulaması
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {
        try {
            // Check for JWT token in request
            String jwt = parseJwt(request);
            log.info("Request path: {}, Method: {}", request.getRequestURI(), request.getMethod());

            if (jwt != null && jwtUtils.validateJwtToken(jwt)) {
                String username = jwtUtils.getUserNameFromJwtToken(jwt);
                String role = jwtUtils.getRoleFromJwtToken(jwt);
                log.info("Username in JWT: {}, role: {}", username, role);

                UserDetails userDetails;
                if ("ROLE_ADMIN".equals(role)) {
                    userDetails = adminDetailsService.loadUserByUsername(username);
                    log.info("Admin user details loaded: {}", username);
                } else {
                    userDetails = customerDetailsService.loadUserByUsername(username);
                    log.info("Customer user details loaded: {}", username);
                }

                log.info("User authorities: {}", userDetails.getAuthorities());

                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authentication);
                log.info("Authentication successful, SecurityContext updated");
            } else {
                // For debugging - log when no token is present or token is invalid
                log.debug("No JWT token found or token is invalid for path: {}", request.getRequestURI());
            }
        } catch (Exception e) {
            log.error("User authentication failed: {}", e.getMessage(), e);
        }

        filterChain.doFilter(request, response);
    }

    /**
     * İstek başlığından JWT token'ı çıkarır
     *
     * @param request HTTP isteği
     * @return JWT token veya null
     */
    private String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");
        log.info("Authorization header: {}", headerAuth != null ? headerAuth.substring(0, Math.min(20, headerAuth.length())) + "..." : "Null");

        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7);
        }

        return null;
    }
}
