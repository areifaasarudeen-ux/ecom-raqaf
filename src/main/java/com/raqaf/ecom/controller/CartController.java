package com.raqaf.ecom.controller;

import com.raqaf.ecom.dto.CartItemResponse;
import com.raqaf.ecom.dto.DtoMapper;
import com.raqaf.ecom.model.CartItem;
import com.raqaf.ecom.repository.CartItemRepository;
import com.raqaf.ecom.service.CartService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/cart")
public class CartController {
    private final CartService cartService;
    private final CartItemRepository cartRepo;

    public CartController(CartService cartService, CartItemRepository cartRepo) {
        this.cartService = cartService;
        this.cartRepo = cartRepo;
    }

    @PostMapping("/add")
    public ResponseEntity<CartItemResponse> add(@RequestParam Long userId, @RequestParam Long productId, @RequestParam(defaultValue = "1") int qty) {
        CartItem ci = cartService.addToCart(userId, productId, qty);
        return ResponseEntity.ok(DtoMapper.toCartItemResponse(ci));
    }

    @GetMapping("/{userId}")
    public List<CartItemResponse> getCart(@PathVariable Long userId) {
        return cartRepo.findByUserId(userId).stream().map(DtoMapper::toCartItemResponse).collect(Collectors.toList());
    }

    @DeleteMapping("/{cartItemId}")
    public ResponseEntity<Void> remove(@PathVariable Long cartItemId) {
        cartService.removeFromCart(cartItemId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{cartItemId}")
    public ResponseEntity<CartItemResponse> updateQty(@PathVariable Long cartItemId, @RequestParam int qty) {
        return ResponseEntity.ok(DtoMapper.toCartItemResponse(cartService.updateQuantity(cartItemId, qty)));
    }
}
