package com.orderservice.event.consumer.payment;

import com.orderservice.dto.request.OrderItemRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentStatusEvent {
    private UUID userId;
    private String fullName;
    private String email;
    private UUID orderId;
    private BigDecimal totalAmount;
    private List<OrderItemRequest> items;

}