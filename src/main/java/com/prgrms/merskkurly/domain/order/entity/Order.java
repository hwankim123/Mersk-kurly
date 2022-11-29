package com.prgrms.merskkurly.domain.order.entity;

import com.prgrms.merskkurly.domain.common.exception.domain.ArgumentOutOfBoundException;
import com.prgrms.merskkurly.domain.common.exception.domain.IllegalOrderStateException;
import com.prgrms.merskkurly.domain.order.util.OrderValidateFields;
import com.prgrms.merskkurly.domain.orderitem.OrderItem;
import org.springframework.util.Assert;

import java.time.LocalDateTime;
import java.util.List;

import static com.prgrms.merskkurly.domain.order.entity.OrderStatus.*;
import static com.prgrms.merskkurly.domain.order.util.OrderValidateFields.*;

public class Order {
    private static final Long BEFORE_INITIALIZED_ID = 0L;

    private final Long id;
    private final Long memberId;
    private String address;
    private OrderStatus orderStatus;
    private final LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private Order(String address){
        this.id = BEFORE_INITIALIZED_ID;
        this.memberId = BEFORE_INITIALIZED_ID;
        this.address = address;
        this.orderStatus = ACCEPTED;
        this.createdAt = LocalDateTime.now();
    }

    private Order(Long id, Long memberId, String address, OrderStatus orderStatus, LocalDateTime createdAt) {
        this.id = id;
        this.memberId = memberId;
        this.address = address;
        this.orderStatus = orderStatus;
        this.createdAt = createdAt;
    }

    public static Order newInstance(String address){
        validateAddress(address);

        Order order = new Order(address);
        order.updatedAt = order.createdAt;
        return order;
    }

    public static Order getInstance(Long id, Long memberId, String address, OrderStatus orderStatus, LocalDateTime createdAt, LocalDateTime updatedAt){
        validateAddress(address);

        Order order = new Order(id, memberId, address, orderStatus, createdAt);
        order.updatedAt = updatedAt;
        return order;
    }

    public void accept(){
        if(orderStatus == ACCEPTED || orderStatus == SETTLED){
            throw new IllegalOrderStateException(CANCEL.name(), orderStatus.name());
        }
        orderStatus = ACCEPTED;
    }

    public void cancel() {
        if(orderStatus == CANCEL || orderStatus == SETTLED){
            throw new IllegalOrderStateException(CANCEL.name(), orderStatus.name());
        }
        orderStatus = CANCEL;
    }

    public void readyForDelivery() {
        if(orderStatus == READY_FOR_DELIVERY || orderStatus == CANCEL){
            throw new IllegalOrderStateException(READY_FOR_DELIVERY.name(), orderStatus.name());
        }
        orderStatus = READY_FOR_DELIVERY;
    }

    public void settle() {
        if(orderStatus == SETTLED || orderStatus == ACCEPTED || orderStatus == CANCEL){
            throw new IllegalOrderStateException(SETTLED.name(), orderStatus.name());
        }
        orderStatus = SETTLED;
    }

    public Long getId() {
        return id;
    }

    public Long getMemberId() {
        return memberId;
    }

    public String getAddress() {
        return address;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    private static void validateAddress(String address) {
        if(address.length() < MIN_ADDRESS || MAX_ADDRESS < address.length()){
            throw new ArgumentOutOfBoundException(ADDRESS_FIELD_NAME, MIN_ADDRESS, MAX_ADDRESS, address.length());
        }
    }

    public void update(String newAddress, OrderStatus newOrderStatus) {
        address = newAddress;
        orderStatus = orderStatus;
    }
}
