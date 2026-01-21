package com.orderservice.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.descriptor.jdbc.JdbcType;

import java.math.BigDecimal;
import java.sql.Types;
import java.util.Map;
import java.util.UUID;

@Entity
@Table(name = "order_lines")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderLineModel {

    @Id
    @Column(name = "order_line_id")
    private UUID orderLineId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    @JsonBackReference
    private OrderModel order;

    @Column(name = "product_id", nullable = false)
    private UUID productId;

    private String productName;

    private UUID variantId;

    @Column(columnDefinition = "JSONB")
    @JdbcTypeCode(Types.OTHER)
    private Map<String, Object> attributes;

    @Column(nullable = false)
    private int quantity;

    @Column(nullable = false)
    private BigDecimal price;


    public OrderLineModel(OrderModel order, UUID productId, UUID variantId, int quantity, BigDecimal price) {
        this.orderLineId = UUID.randomUUID();
        this.order = order;
        this.productId = productId;
        this.variantId = variantId;
        this.quantity = quantity;
        this.price = price;
    }
}
