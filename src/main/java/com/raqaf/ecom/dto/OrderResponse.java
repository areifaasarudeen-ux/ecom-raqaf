package com.raqaf.ecom.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter @Setter @NoArgsConstructor
public class OrderResponse {
    private Long id;
    private Long userId;
    private BigDecimal totalPrice;
    private String status;
    private List<OrderItemResponse> items;
    private String createdAt;
}