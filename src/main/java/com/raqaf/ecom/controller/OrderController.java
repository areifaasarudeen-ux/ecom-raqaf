package com.raqaf.ecom.controller;

import com.raqaf.ecom.dto.DtoMapper;
import com.raqaf.ecom.dto.OrderResponse;
import com.raqaf.ecom.model.Order;
import com.raqaf.ecom.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import org.springframework.web.bind.annotation.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    private final OrderService svc;
    public OrderController(OrderService svc) { this.svc = svc; }

    @PostMapping("/create")
    public ResponseEntity<OrderResponse> createFromCart(@RequestParam Long userId) {
        Order o = svc.createOrderFromCart(userId);
        return ResponseEntity.ok(DtoMapper.toOrderResponse(o));
    }

    @GetMapping("/user/{userId}")
    public List<OrderResponse> getUserOrders(@PathVariable Long userId) {
        return svc.findByUser(userId).stream().map(DtoMapper::toOrderResponse).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> get(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(DtoMapper.toOrderResponse(svc.findById(id)));
        } catch (RuntimeException ex) {
            return ResponseEntity.notFound().build();
        }
    }
}