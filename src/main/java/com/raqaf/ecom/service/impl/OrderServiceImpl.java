package com.raqaf.ecom.service.impl;

import com.raqaf.ecom.model.Order;
import com.raqaf.ecom.model.OrderItem;
import com.raqaf.ecom.repository.*;
import com.raqaf.ecom.service.OrderService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class OrderServiceImpl implements OrderService {
    private final CartItemRepository cartRepo;
    private final OrderRepository orderRepo;
    private final OrderItemRepository orderItemRepo;
    private final UserRepository userRepo;
    private final ProductRepository productRepo;

    public OrderServiceImpl(CartItemRepository cartRepo, OrderRepository orderRepo,
                            OrderItemRepository orderItemRepo, UserRepository userRepo,
                            ProductRepository productRepo) {
        this.cartRepo = cartRepo;
        this.orderRepo = orderRepo;
        this.orderItemRepo = orderItemRepo;
        this.userRepo = userRepo;
        this.productRepo = productRepo;
    }

    @Override
    public Order createOrderFromCart(Long userId) {
        var user = userRepo.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        var cart = cartRepo.findByUserId(userId);
        if (cart.isEmpty()) throw new RuntimeException("Cart is empty");

        Order order = new Order();
        order.setUser(user);
        order.setStatus("PENDING");

        BigDecimal total = cart.stream()
                .map(ci -> ci.getProduct().getPrice().multiply(BigDecimal.valueOf(ci.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        order.setTotalPrice(total);

        order = orderRepo.save(order);

        Order finalOrder = order;
        List<OrderItem> items = cart.stream().map(ci -> {
            var oi = new OrderItem();
            oi.setOrder(finalOrder);
            oi.setProduct(ci.getProduct());
            oi.setQuantity(ci.getQuantity());
            oi.setPrice(ci.getProduct().getPrice());
// optionally decrement stock
            var p = ci.getProduct();
            p.setStock(Math.max(0, p.getStock() - ci.getQuantity()));
            productRepo.save(p);
            return oi;
        }).collect(Collectors.toList());

        order.setItems(orderItemRepo.saveAll(items));
        cartRepo.deleteAll(cart); // clear cart
        return order;
    }

    @Override public List<Order> findByUser(Long userId) { return orderRepo.findByUserId(userId); }
    @Override public Order findById(Long id) { return orderRepo.findById(id).orElseThrow(() -> new RuntimeException("Order not found")); }
}
