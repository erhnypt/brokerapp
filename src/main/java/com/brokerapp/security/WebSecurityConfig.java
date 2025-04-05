package com.brokerapp.security;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.brokerapp.security.jwt.JwtAuthenticationFilter;
import com.brokerapp.security.services.AdminDetailsServiceImpl;
import com.brokerapp.security.services.CustomerDetailsServiceImpl;

/**
 * Spring Security konfigürasyonu
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class WebSecurityConfig {

    @Autowired
    private AdminDetailsServiceImpl adminDetailsService;

    @Autowired
    private CustomerDetailsServiceImpl customerDetailsService;

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * SecurityFilterChain tanımlar
     *
     * @param http HttpSecurity nesnesi
     * @return yapılandırılmış SecurityFilterChain
     * @throws Exception güvenlik yapılandırması hatası
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // Security settings for development phase - disable all security protections
        http.csrf(AbstractHttpConfigurer::disable) // Disable CSRF
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                // Allow all authentication-related endpoints
                .requestMatchers("/api/auth/**", "/api/auth/login", "/api/auth/register", "/api/auth/simple-login", "/api/simple-auth/**", "/api/alt-auth/**").permitAll()
                // Allow Swagger UI and API docs
                .requestMatchers("/swagger-ui/**", "/swagger-ui.html", "/v3/api-docs/**", "/api-docs/**").permitAll()
                // Allow H2 console
                .requestMatchers("/h2-console/**").permitAll()
                // Protect admin endpoints
                .requestMatchers("/api/admin/**").hasRole("ADMIN")
                // Protect customer-specific endpoints
                .requestMatchers("/api/customers/me/**").hasRole("CUSTOMER")
                // Require authentication for everything else
                .anyRequest().authenticated());

        // Disable frame options for H2 console
        http.headers(headers -> headers.frameOptions(frameOptions -> frameOptions.disable()));

        // Add authentication providers
        http.authenticationProvider(adminAuthenticationProvider());
        http.authenticationProvider(customerAuthenticationProvider());

        // Add JWT filter before the standard authentication filter
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /**
     * CORS yapılandırması
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("authorization", "content-type", "x-auth-token"));
        configuration.setExposedHeaders(Arrays.asList("x-auth-token"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    /**
     * Admin AuthenticationProvider bean'i
     *
     * @return DaoAuthenticationProvider
     */
    @Bean
    public DaoAuthenticationProvider adminAuthenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(adminDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder);
        authProvider.setHideUserNotFoundExceptions(false);
        return authProvider;
    }

    /**
     * Müşteri AuthenticationProvider bean'i
     *
     * @return DaoAuthenticationProvider
     */
    @Bean
    public DaoAuthenticationProvider customerAuthenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(customerDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder);
        authProvider.setHideUserNotFoundExceptions(false);
        return authProvider;
    }

    /**
     * AuthenticationManager bean'i
     *
     * @param authConfig AuthenticationConfiguration
     * @return AuthenticationManager
     * @throws Exception yapılandırma hatası
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }
}
