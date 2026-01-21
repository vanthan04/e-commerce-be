package com.orderservice.service;

import com.orderservice.event.consumer.payment.PaymentStatusEvent;
import com.orderservice.exception.AppException;
import com.orderservice.exception.ErrorCode;
import com.orderservice.model.OrderModel;
import com.orderservice.model.OrderStatus;
import com.orderservice.repository.OrderRepository;
import lombok.AllArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class HandleEvent {
    private final OrderRepository orderRepository;

    @KafkaListener(topics = "payment-success", groupId = "inventoryGroup")
    public void updateSuccessOrder(PaymentStatusEvent event){
        OrderModel order = orderRepository.findById(event.getOrderId())
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));
        order.updateOrder(OrderStatus.PAID);
        orderRepository.save(order);
    }

    @KafkaListener(topics = "payment-failed", groupId = "inventoryGroup")
    public void updateFailedOrder(PaymentStatusEvent event){
        OrderModel order = orderRepository.findById(event.getOrderId())
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));
        order.updateOrder(OrderStatus.FAILED);
        orderRepository.save(order);
    }

}
