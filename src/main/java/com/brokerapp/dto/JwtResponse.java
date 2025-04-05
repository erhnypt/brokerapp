package com.brokerapp.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * JWT token response DTO class
 */
@Data
@NoArgsConstructor
public class JwtResponse {

    /**
     * JWT token
     */
    private String token;

    /**
     * Token type
     */
    private String type = "Bearer";

    /**
     * Username
     */
    private String username;

    /**
     * User ID
     */
    private Long id;

    /**
     * Constructor with token only - adds Bearer type
     *
     * @param token JWT token
     */
    public JwtResponse(String token) {
        this.token = token;
        this.type = "Bearer";
    }

    /**
     * Constructor with all parameters
     *
     * @param token JWT token
     * @param username username
     * @param id user id
     */
    public JwtResponse(String token, String username, Long id) {
        this.token = token;
        this.type = "Bearer";
        this.username = username;
        this.id = id;
    }
}
