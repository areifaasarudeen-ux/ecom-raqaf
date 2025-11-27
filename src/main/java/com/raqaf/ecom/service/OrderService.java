package com.raqaf.ecom.service;

import com.raqaf.ecom.model.Order;

import java.util.List;

public interface OrderService {
    Order createOrderFromCart(Long userId);
    List<Order> findByUser(Long userId);
    Order findById(Long id);
}
