package com.prgrms.merskkurly.domain.orderitem;

import java.time.LocalDateTime;

public class OrderItem {
    private final Long id;
    private final Long productId;
    private final Long orderId;
    private final long price;
    private int quantity;
    private final LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public OrderItem(Long id, Long productId, Long orderId, long price, LocalDateTime updatedAt, int quantity) {
        this.id = id;
        this.productId = productId;
        this.orderId = orderId;
        this.price = price;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = updatedAt;
        this.quantity = quantity;
    }

    public Long getId() {
        return id;
    }

    public Long getProductId() {
        return productId;
    }

    public Long getOrderId() {
        return orderId;
    }

    public long getQuantity() {
        return quantity;
    }
}
