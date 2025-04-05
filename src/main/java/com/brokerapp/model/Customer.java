package com.brokerapp.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Customer entity class. Stores customer information.
 */
@Entity
@Table(name = "customers")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Customer {

    /**
     * Customer ID, automatically generated
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Customer username, must be unique
     */
    @Column(nullable = false, unique = true)
    private String username;

    /**
     * Customer password (stored encrypted)
     */
    @Column(nullable = false)
    private String password;

    /**
     * Customer's total TRY balance
     */
    @Column(nullable = false)
    private Double tryBalance;

    /**
     * Customer's usable TRY balance (not in transaction)
     */
    @Column(nullable = false)
    private Double tryUsableBalance;

    /**
     * Whether the customer account is active
     */
    @Column(nullable = false)
    private boolean active;

    // Getters
    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public Double getTryBalance() {
        return tryBalance;
    }

    public Double getTryUsableBalance() {
        return tryUsableBalance;
    }

    public boolean isActive() {
        return active;
    }

    // Setters
    public void setId(Long id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setTryBalance(Double tryBalance) {
        this.tryBalance = tryBalance;
    }

    public void setTryUsableBalance(Double tryUsableBalance) {
        this.tryUsableBalance = tryUsableBalance;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    // Builder class
    public static CustomerBuilder builder() {
        return new CustomerBuilder();
    }

    public static class CustomerBuilder {

        private Long id;
        private String username;
        private String password;
        private Double tryBalance;
        private Double tryUsableBalance;
        private boolean active;

        public CustomerBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public CustomerBuilder username(String username) {
            this.username = username;
            return this;
        }

        public CustomerBuilder password(String password) {
            this.password = password;
            return this;
        }

        public CustomerBuilder tryBalance(Double tryBalance) {
            this.tryBalance = tryBalance;
            return this;
        }

        public CustomerBuilder tryUsableBalance(Double tryUsableBalance) {
            this.tryUsableBalance = tryUsableBalance;
            return this;
        }

        public CustomerBuilder active(boolean active) {
            this.active = active;
            return this;
        }

        public Customer build() {
            Customer customer = new Customer();
            customer.id = this.id;
            customer.username = this.username;
            customer.password = this.password;
            customer.tryBalance = this.tryBalance;
            customer.tryUsableBalance = this.tryUsableBalance;
            customer.active = this.active;
            return customer;
        }
    }
}
