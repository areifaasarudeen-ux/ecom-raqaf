package com.raqaf.ecom.dto;
import com.raqaf.ecom.model.*;

import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class DtoMapper {

    private static final DateTimeFormatter DF = DateTimeFormatter.ISO_INSTANT;

    public static UserResponse toUserResponse(User u) {
        if (u == null) return null;
        UserResponse r = new UserResponse();
        r.setId(u.getId());
        r.setName(u.getName());
        r.setEmail(u.getEmail());
        r.setRole(u.getRole() != null ? u.getRole().getName() : null);
        if (u.getCreatedAt() != null) r.setCreatedAt(DF.format(u.getCreatedAt().atOffset(ZoneOffset.UTC)));
        return r;
    }

    public static CategoryResponse toCategoryResponse(Category c) {
        if (c == null) return null;
        CategoryResponse r = new CategoryResponse();
        r.setId(c.getId());
        r.setName(c.getName());
        r.setDescription(c.getDescription());
        return r;
    }

    public static ProductResponse toProductResponse(Product p) {
        if (p == null) return null;
        ProductResponse r = new ProductResponse();
        r.setId(p.getId());
        r.setName(p.getName());
        r.setDescription(p.getDescription());
        r.setPrice(p.getPrice());
        r.setStock(p.getStock());
        r.setImageUrl(p.getImageUrl());
        r.setCategory(toCategoryResponse(p.getCategory()));
        if (p.getCreatedAt() != null) r.setCreatedAt(DF.format(p.getCreatedAt().atOffset(ZoneOffset.UTC)));
        return r;
    }

    public static CartItemResponse toCartItemResponse(CartItem ci) {
        if (ci == null) return null;
        CartItemResponse r = new CartItemResponse();
        r.setId(ci.getId());
        r.setProductId(ci.getProduct().getId());
        r.setProductName(ci.getProduct().getName());
        r.setQuantity(ci.getQuantity());
        r.setImageUrl(ci.getProduct().getImageUrl());
        return r;
    }

    public static OrderItemResponse toOrderItemResponse(OrderItem oi) {
        if (oi == null) return null;
        OrderItemResponse r = new OrderItemResponse();
        r.setId(oi.getId());
        r.setProductId(oi.getProduct().getId());
        r.setProductName(oi.getProduct().getName());
        r.setQuantity(oi.getQuantity());
        r.setPrice(oi.getPrice());
        return r;
    }

    public static OrderResponse toOrderResponse(Order o) {
        if (o == null) return null;
        OrderResponse r = new OrderResponse();
        r.setId(o.getId());
        r.setUserId(o.getUser().getId());
        r.setTotalPrice(o.getTotalPrice());
        r.setStatus(o.getStatus());
        r.setItems(o.getItems() != null ? o.getItems().stream().map(DtoMapper::toOrderItemResponse).collect(Collectors.toList()) : List.of());
        if (o.getCreatedAt() != null) r.setCreatedAt(DF.format(o.getCreatedAt().atOffset(ZoneOffset.UTC)));
        return r;
    }

    // Map product request -> product entity (partial: category must be set by caller)
    public static void updateProductFromRequest(Product p, ProductRequest req) {
        p.setName(req.getName());
        p.setDescription(req.getDescription());
        p.setPrice(req.getPrice());
        p.setStock(req.getStock());
        p.setImageUrl(req.getImageUrl());
    }
}
