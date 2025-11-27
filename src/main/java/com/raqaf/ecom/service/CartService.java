package com.raqaf.ecom.service;


import com.raqaf.ecom.model.CartItem;

import java.util.List;

public interface CartService {
    CartItem addToCart(Long userId, Long productId, int quantity);
    List<CartItem> getCart(Long userId);
    void removeFromCart(Long cartItemId);
    CartItem updateQuantity(Long cartItemId, int quantity);
}