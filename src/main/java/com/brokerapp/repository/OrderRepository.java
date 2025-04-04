package com.brokerapp.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.brokerapp.model.Customer;
import com.brokerapp.model.Order;
import com.brokerapp.model.OrderStatus;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByCustomer(Customer customer);

    List<Order> findByCustomerAndCreateDateBetween(Customer customer, LocalDateTime startDate, LocalDateTime endDate);

    List<Order> findByCustomerAndStatus(Customer customer, OrderStatus status);
}
