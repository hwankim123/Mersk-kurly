package com.prgrms.merskkurly.domain.orderitem.entity;

import static com.prgrms.merskkurly.domain.orderitem.util.OrderItemValidateFields.*;

import com.prgrms.merskkurly.domain.common.exception.domain.ArgumentOutOfBoundException;
import com.prgrms.merskkurly.domain.orderitem.util.OrderItemValidateFields;
import java.time.LocalDateTime;

public class OrderItem {
    private final Long BEFORE_INITIALIZED_ID = 0L;
    private final Long id;
    private final Long itemId;
    private final Long orderId;
    private int quantity;
    private final LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private OrderItem(){
        this.id = BEFORE_INITIALIZED_ID;
        this.itemId = BEFORE_INITIALIZED_ID;
        this.orderId = BEFORE_INITIALIZED_ID;
        this.createdAt = LocalDateTime.now();
    }

    public OrderItem(Long id, Long itemId, Long orderId, LocalDateTime createdAt) {
        this.id = id;
        this.itemId = itemId;
        this.orderId = orderId;
        this.createdAt = createdAt;
    }

    public static OrderItem newInstance(int quantity){
        validateQuantity(quantity);

        OrderItem orderItem = new OrderItem();
        orderItem.quantity = quantity;
        orderItem.updatedAt = orderItem.createdAt;
        return orderItem;
    }

    public static OrderItem getInstance(Long id, Long itemId, Long orderId, int quantity, LocalDateTime createdAt, LocalDateTime updatedAt){
        validateQuantity(quantity);

        OrderItem orderItem = new OrderItem(id, itemId, orderId, createdAt);
        orderItem.quantity = quantity;
        orderItem.updatedAt = updatedAt;
        return orderItem;
    }

    public void changeQuantity(int newQuantity){
        validateQuantity(newQuantity);
        quantity = newQuantity;
    }

    private static void validateQuantity(int newQuantity) {
        if(newQuantity < MIN_QUANTITY){
            throw new ArgumentOutOfBoundException(QUANTITY_FIELD_NAME, MIN_QUANTITY, Integer.MAX_VALUE, newQuantity);
        }
    }

    public Long getId() {
        return id;
    }

    public Long getOrderId() {
        return orderId;
    }

    public Long getItemId() {
        return itemId;
    }

    public int getQuantity() {
        return quantity;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}
