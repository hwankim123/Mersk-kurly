package com.prgrms.merskkurly.domain.orderitem.entity;

import static com.prgrms.merskkurly.domain.orderitem.util.OrderItemValidateFields.*;

import com.prgrms.merskkurly.domain.common.exception.domain.ArgumentOutOfBoundException;
import java.time.LocalDateTime;

public class OrderItem {
    private final Long BEFORE_INITIALIZED_ID = 0L;

    private final Long id;
    private final Long memberId;
    private final Long itemId;
    private Long orderId;
    private int quantity;
    private boolean isOrdered;
    private final LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private OrderItem(Long memberId, Long itemId){
        this.id = BEFORE_INITIALIZED_ID;
        this.memberId= memberId;
        this.itemId = itemId;
        this.orderId = BEFORE_INITIALIZED_ID;
        this.isOrdered = false;
        this.createdAt = LocalDateTime.now();
    }

    public OrderItem(Long id, Long memberId, Long itemId, Long orderId, boolean isOrdered, LocalDateTime createdAt) {
        this.id = id;
        this.memberId = memberId;
        this.itemId = itemId;
        this.orderId = orderId;
        this.isOrdered = isOrdered;
        this.createdAt = createdAt;
    }

    public static OrderItem newInstance(Long memberId, Long itemId, int quantity){
        validateQuantity(quantity);

        OrderItem orderItem = new OrderItem(memberId, itemId);
        orderItem.quantity = quantity;
        orderItem.updatedAt = orderItem.createdAt;
        return orderItem;
    }

    public static OrderItem getInstance(Long id, Long memberId, Long itemId, Long orderId, int quantity, boolean isOrdered, LocalDateTime createdAt, LocalDateTime updatedAt){
        validateQuantity(quantity);

        OrderItem orderItem = new OrderItem(id, memberId, itemId, orderId, isOrdered, createdAt);
        orderItem.quantity = quantity;
        orderItem.updatedAt = updatedAt;
        return orderItem;
    }

    public void changeQuantity(int newQuantity){
        validateQuantity(newQuantity);
        quantity = newQuantity;
    }

    public void order(Long orderId){
        this.orderId = orderId;
        isOrdered = true;
    }

    private static void validateQuantity(int newQuantity) {
        if(newQuantity < MIN_QUANTITY){
            throw new ArgumentOutOfBoundException(QUANTITY_FIELD_NAME, MIN_QUANTITY, Integer.MAX_VALUE, newQuantity);
        }
    }

    public boolean getIsOrdered() {
        return isOrdered;
    }

    public Long getId() {
        return id;
    }

    public Long getMemberId(){
        return memberId;
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
