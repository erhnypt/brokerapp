package com.brokerapp.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.brokerapp.dto.OrderDTO;
import com.brokerapp.enums.OrderStatus;
import com.brokerapp.enums.OrderType;
import com.brokerapp.exception.ResourceNotFoundException;
import com.brokerapp.model.Asset;
import com.brokerapp.model.Customer;
import com.brokerapp.model.CustomerAsset;
import com.brokerapp.model.Order;
import com.brokerapp.repository.AssetRepository;
import com.brokerapp.repository.CustomerAssetRepository;
import com.brokerapp.repository.CustomerRepository;
import com.brokerapp.repository.OrderRepository;
import com.brokerapp.service.CustomerAssetService;
import com.brokerapp.service.OrderService;

/**
 * OrderService arayüzünün implementasyonu
 */
@Service
public class OrderServiceImpl implements OrderService {

    private static final Logger log = LoggerFactory.getLogger(OrderServiceImpl.class);

    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    private final AssetRepository assetRepository;
    private final CustomerAssetRepository customerAssetRepository;
    private final CustomerAssetService customerAssetService;

    /**
     * Constructor-based dependency injection
     */
    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository, CustomerRepository customerRepository,
            AssetRepository assetRepository, CustomerAssetRepository customerAssetRepository,
            CustomerAssetService customerAssetService) {
        this.orderRepository = orderRepository;
        this.customerRepository = customerRepository;
        this.assetRepository = assetRepository;
        this.customerAssetRepository = customerAssetRepository;
        this.customerAssetService = customerAssetService;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public Order createOrder(OrderDTO orderDTO) {
        log.info("Creating order: {}", orderDTO);
        
        // Customer check
        Customer customer = customerRepository.findById(orderDTO.getCustomerId())
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found: " + orderDTO.getCustomerId()));
        
        // Asset check
        Asset asset = assetRepository.findById(orderDTO.getAssetId())
                .orElseThrow(() -> new ResourceNotFoundException("Asset not found: " + orderDTO.getAssetId()));
        
        // Check balance for BUY operation
        if (orderDTO.getSide() == OrderType.BUY) {
            double totalCost = orderDTO.getPrice() * orderDTO.getSize();
            if (customer.getTryUsableBalance() < totalCost) {
                throw new IllegalStateException("Insufficient balance. Required: " + totalCost + ", Available: " + customer.getTryUsableBalance());
            }
            
            // Reduce balance
            customer.setTryUsableBalance(customer.getTryUsableBalance() - totalCost);
            customerRepository.save(customer);
        } 
        // Check asset amount for SELL operation
        else if (orderDTO.getSide() == OrderType.SELL) {
            // Check customer's asset amount
            Optional<CustomerAsset> customerAsset = customerAssetService.getCustomerAssetByCustomerIdAndAssetId(
                    customer.getId(), asset.getId());
            
            if (customerAsset.isEmpty() || customerAsset.get().getUsableSize() < orderDTO.getSize()) {
                throw new IllegalStateException("Insufficient asset. Required: " + orderDTO.getSize() + 
                        ", Available: " + (customerAsset.isEmpty() ? 0 : customerAsset.get().getUsableSize()));
            }
            
            try {
                // Reduce usable asset amount
                customerAssetService.decreaseCustomerAssetUsableAmount(
                        customer.getId(), asset.getId(), orderDTO.getSize());
                
                log.info("Customer asset updated for sale: Customer={}, Asset={}, Amount={}",
                        customer.getId(), asset.getAssetName(), orderDTO.getSize());
            } catch (Exception e) {
                log.error("Error updating asset amount during sale process: {}", e.getMessage(), e);
                throw new IllegalStateException("Error during sale process: " + e.getMessage(), e);
            }
        }
        
        // Create order
        Order order = Order.builder()
                .customer(customer)
                .asset(asset)
                .side(orderDTO.getSide())
                .size(orderDTO.getSize())
                .price(orderDTO.getPrice())
                .status(OrderStatus.PENDING)
                .createDate(LocalDateTime.now())
                .build();
        
        Order savedOrder = orderRepository.save(order);
        log.info("Order created successfully: {}", savedOrder);
        
        // Process matching orders (can be done asynchronously)
        processMatchingOrders(savedOrder.getId());

        return savedOrder;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Order> getOrderById(Long id) {
        return orderRepository.findById(id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Order> getOrdersByCustomerId(Long customerId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found: " + customerId));
        return orderRepository.findByCustomer(customer);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Order> getOrdersByCustomerIdAndStatus(Long customerId, OrderStatus status) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found: " + customerId));
        return orderRepository.findByCustomerAndStatus(customer, status);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Order> getOrdersByCustomerIdAndDateRange(Long customerId, LocalDateTime startDate, LocalDateTime endDate) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found: " + customerId));
        return orderRepository.findByCustomerAndCreateDateBetween(customer, startDate, endDate);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public List<Order> getOrdersByCustomerIdAndStatusAndDateRange(Long customerId, OrderStatus status, 
                                                                 LocalDateTime startDate, LocalDateTime endDate) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found: " + customerId));
        return orderRepository.findByCustomerAndStatusAndCreateDateBetween(customer, status, startDate, endDate);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Order> getOrdersByAssetId(Long assetId) {
        Asset asset = assetRepository.findById(assetId)
                .orElseThrow(() -> new ResourceNotFoundException("Asset not found: " + assetId));
        return orderRepository.findByAsset(asset);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Order> getOrdersByAssetIdAndStatus(Long assetId, OrderStatus status) {
        Asset asset = assetRepository.findById(assetId)
                .orElseThrow(() -> new ResourceNotFoundException("Asset not found: " + assetId));
        return orderRepository.findByAssetAndStatus(asset, status);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public Order updateOrderStatus(Long id, OrderStatus status) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found: " + id));
        
        log.info("Updating order status: {} -> {}", order.getStatus(), status);
        
        // Same status, do nothing
        if (order.getStatus() == status) {
            log.info("Order already in {} status, no change made", status);
            return order;
        }
        
        OrderStatus oldStatus = order.getStatus();
        
        // Transition to MATCHED status
        if (status == OrderStatus.MATCHED) {
            // If BUY operation, add asset to buyer
            if (order.getSide() == OrderType.BUY) {
                Customer buyer = order.getCustomer();
                Asset asset = order.getAsset();
                
                // Update buyer's asset balance
                customerAssetService.increaseCustomerAssetAmount(
                    buyer.getId(), asset.getId(), order.getSize());
                
                log.info("Asset updated for buyer: Customer={}, Asset={}, Amount={}",
                        buyer.getId(), asset.getAssetName(), order.getSize());
            }
            // If SELL operation, add money to seller
            else if (order.getSide() == OrderType.SELL) {
                Customer seller = order.getCustomer();
                double saleAmount = order.getSize() * order.getPrice();
                
                // Update seller's TRY balance
                seller.setTryBalance(seller.getTryBalance() + saleAmount);
                seller.setTryUsableBalance(seller.getTryUsableBalance() + saleAmount);
                customerRepository.save(seller);
                
                log.info("TRY updated for seller: Customer={}, Amount={}", seller.getId(), saleAmount);
            }
        }
        // Transition to CANCELED status
        else if (status == OrderStatus.CANCELED && oldStatus == OrderStatus.PENDING) {
            // If BUY operation, refund balance
            if (order.getSide() == OrderType.BUY) {
                Customer customer = order.getCustomer();
                double refundAmount = order.getPrice() * order.getSize();
                
                log.info("Balance refund: Customer={}, Amount={}", customer.getId(), refundAmount);
                customer.setTryUsableBalance(customer.getTryUsableBalance() + refundAmount);
                customerRepository.save(customer);
            }
            // If SELL operation, return asset
            else if (order.getSide() == OrderType.SELL) {
                // Return usable asset amount
                customerAssetService.increaseCustomerAssetUsableAmount(
                    order.getCustomer().getId(), order.getAsset().getId(), order.getSize());
                
                log.info("Asset refund: Customer={}, Asset={}, Amount={}",
                        order.getCustomer().getId(), order.getAsset().getAssetName(), order.getSize());
            }
        }
        
        // Update status
        order.setStatus(status);
        order.setUpdatedAt(LocalDateTime.now());
        Order updatedOrder = orderRepository.save(order);
        log.info("Order status successfully updated: {}", updatedOrder);
        return updatedOrder;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public Order cancelOrder(Long id) {
        log.info("Cancelling order, ID: {}", id);
        // Now using updateOrderStatus method for cancellation
        return updateOrderStatus(id, OrderStatus.CANCELED);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public List<Order> processMatchingOrders(Long orderId) {
        Order newOrder = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found: " + orderId));
        
        // List to track matched orders
        List<Order> matchedOrders = new ArrayList<>();
        
        // If new order is already matched or canceled, do nothing
        if (newOrder.getStatus() != OrderStatus.PENDING) {
            log.info("Order cannot be matched because it is not in PENDING status: {}", newOrder);
            return matchedOrders;
        }
        
        // Counter order type
        OrderType oppositeType = (newOrder.getSide() == OrderType.BUY) ? OrderType.SELL : OrderType.BUY;
        
        // Find counter orders for the same asset
        List<Order> potentialMatches = orderRepository.findByAssetAndStatus(newOrder.getAsset(), OrderStatus.PENDING);
        
        // Track remaining amount
        double remainingQuantity = newOrder.getSize();
        
        for (Order potentialMatch : potentialMatches) {
            // Don't match with own orders and only check counter type orders
            if (potentialMatch.getId().equals(newOrder.getId()) || potentialMatch.getSide() != oppositeType) {
                continue;
            }
            
            // Check price compatibility
            boolean priceMatches = false;
            if (newOrder.getSide() == OrderType.BUY) {
                // For BUY order, price must be equal or higher than sell price
                priceMatches = newOrder.getPrice() >= potentialMatch.getPrice();
            } else {
                // For SELL order, price must be equal or lower than buy price
                priceMatches = newOrder.getPrice() <= potentialMatch.getPrice();
            }
            
            if (!priceMatches) {
                continue;
            }
            
            // Order match found
            log.info("Order match found: {} with {}", newOrder.getId(), potentialMatch.getId());
            
            // Determine matched amount (minimum amount in orders)
            double matchQuantity = Math.min(remainingQuantity, potentialMatch.getSize());
            
            // Buyer in the BUY operation
            Customer buyer = (newOrder.getSide() == OrderType.BUY) ? newOrder.getCustomer() : potentialMatch.getCustomer();
            // Seller in the SELL operation
            Customer seller = (newOrder.getSide() == OrderType.BUY) ? potentialMatch.getCustomer() : newOrder.getCustomer();
            // Asset in transaction
            Asset asset = newOrder.getAsset();
            
            // Add asset for buyer
            customerAssetService.increaseCustomerAssetAmount(buyer.getId(), asset.getId(), matchQuantity);
            
            // Add money for seller (increase balance when SELL order is matched)
            double saleAmount = matchQuantity * potentialMatch.getPrice();
            seller.setTryBalance(seller.getTryBalance() + saleAmount);
            seller.setTryUsableBalance(seller.getTryUsableBalance() + saleAmount);
            customerRepository.save(seller);
            
            log.info("Asset and money transfer completed: Buyer={}, Seller={}, Asset={}, Amount={}, Price={}",
                    buyer.getId(), seller.getId(), asset.getAssetName(), matchQuantity, potentialMatch.getPrice());
            
            // Update status for both orders
            if (potentialMatch.getSize() == matchQuantity) {
                potentialMatch.setStatus(OrderStatus.MATCHED);
                orderRepository.save(potentialMatch);
                matchedOrders.add(potentialMatch);
            } else {
                // Partial match - reduce counter order amount
                potentialMatch.setSize(potentialMatch.getSize() - matchQuantity);
                orderRepository.save(potentialMatch);
                // In this case counter order is still in PENDING status
            }
            
            // Update remaining amount
            remainingQuantity -= matchQuantity;
            
            // If all amount is matched, exit loop
            if (remainingQuantity == 0) {
                newOrder.setStatus(OrderStatus.MATCHED);
                orderRepository.save(newOrder);
                matchedOrders.add(newOrder);
                break;
            }
        }
        
        // If order is partially matched, update with remaining amount
        if (remainingQuantity > 0 && remainingQuantity < newOrder.getSize()) {
            newOrder.setSize(remainingQuantity);
            // Status remains PENDING
            orderRepository.save(newOrder);
        }
        
        log.info("Order matching completed. Matched orders: {}", matchedOrders.size());
        
        return matchedOrders;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public Order matchOrder(Long orderId) {
        log.info("Admin is matching order, ID: {}", orderId);
        
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found: " + orderId));
        
        // Only PENDING orders can be matched
        if (order.getStatus() != OrderStatus.PENDING) {
            throw new IllegalStateException("Only PENDING orders can be matched. Current status: " + order.getStatus());
        }
        
        // Now using updateOrderStatus method for matching
        return updateOrderStatus(orderId, OrderStatus.MATCHED);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public int matchAllPendingOrders() {
        log.info("Matching all pending orders...");
        
        // Find all pending orders
        List<Order> pendingOrders = orderRepository.findByStatus(OrderStatus.PENDING);
        log.info("Found a total of {} pending orders", pendingOrders.size());
        
        int matchedCount = 0;
        
        // Match each pending order
        for (Order order : pendingOrders) {
            try {
                // Use updateOrderStatus() method to update status
                updateOrderStatus(order.getId(), OrderStatus.MATCHED);
                matchedCount++;
                log.info("Order successfully matched: ID={}, Type={}, Amount={}, Price={}",
                        order.getId(), order.getSide(), order.getSize(), order.getPrice());
            } catch (Exception e) {
                log.error("Error matching order: {}", e.getMessage(), e);
            }
        }
        
        log.info("Total of {} orders successfully matched", matchedCount);
        return matchedCount;
    }
} 