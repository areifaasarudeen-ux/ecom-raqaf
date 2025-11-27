package com.raqaf.ecom.service.impl;

import com.raqaf.ecom.model.CartItem;
import com.raqaf.ecom.repository.CartItemRepository;
import com.raqaf.ecom.repository.ProductRepository;
import com.raqaf.ecom.repository.UserRepository;
import com.raqaf.ecom.service.CartService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class CartServiceImpl implements CartService {
    private final CartItemRepository cartRepo;
    private final UserRepository userRepo;
    private final ProductRepository productRepo;

    public CartServiceImpl(CartItemRepository cartRepo, UserRepository userRepo, ProductRepository productRepo) {
        this.cartRepo = cartRepo;
        this.userRepo = userRepo;
        this.productRepo = productRepo;
    }

    @Override
    public CartItem addToCart(Long userId, Long productId, int quantity) {
        var user = userRepo.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        var product = productRepo.findById(productId).orElseThrow(() -> new RuntimeException("Product not found"));

        var existing = cartRepo.findByUserIdAndProductId(userId, productId);
        if (existing.isPresent()) {
            var ci = existing.get();
            ci.setQuantity(ci.getQuantity() + quantity);
            return cartRepo.save(ci);
        } else {
            var ci = new CartItem();
            ci.setUser(user);
            ci.setProduct(product);
            ci.setQuantity(quantity);
            return cartRepo.save(ci);
        }
    }

    @Override public List<CartItem> getCart(Long userId) { return cartRepo.findByUserId(userId); }
    @Override public void removeFromCart(Long cartItemId) { cartRepo.deleteById(cartItemId); }

    @Override
    public CartItem updateQuantity(Long cartItemId, int quantity) {
        return cartRepo.findById(cartItemId).map(ci -> {
            ci.setQuantity(quantity);
            return cartRepo.save(ci);
        }).orElseThrow(() -> new RuntimeException("Cart item not found"));
    }
}
