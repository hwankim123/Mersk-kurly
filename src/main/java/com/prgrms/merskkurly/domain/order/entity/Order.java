package com.prgrms.merskkurly.domain.order.entity;

import com.prgrms.merskkurly.domain.common.exception.domain.ArgumentOutOfBoundException;
import com.prgrms.merskkurly.domain.common.exception.domain.IllegalOrderStateException;

import java.time.LocalDateTime;

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

    private Order(Long memberId, String address) {
        this.id = BEFORE_INITIALIZED_ID;
        this.memberId = memberId;
        this.address = address;
        this.orderStatus = PAYED;
        this.createdAt = LocalDateTime.now();
    }

    private Order(Long id, Long memberId, String address, OrderStatus orderStatus, LocalDateTime createdAt) {
        this.id = id;
        this.memberId = memberId;
        this.address = address;
        this.orderStatus = orderStatus;
        this.createdAt = createdAt;
    }

    public static Order newInstance(Long memberId, String address) {
        validateAddress(address);

        Order order = new Order(memberId, address);
        order.updatedAt = order.createdAt;
        return order;
    }

    public static Order getInstance(Long id, Long memberId, String address, OrderStatus orderStatus, LocalDateTime createdAt, LocalDateTime updatedAt) {
        validateAddress(address);

        Order order = new Order(id, memberId, address, orderStatus, createdAt);
        order.updatedAt = updatedAt;
        return order;
    }

    public void update(String newAddress) {
        if (orderStatus.equals(CONFIRMED)) {
            throw new IllegalOrderStateException(CONFIRMED.name());
        }
        validateAddress(newAddress);

        address = newAddress;
    }

    public void pay() {
        if (!orderStatus.equals(CANCELED)) {
            throw new IllegalOrderStateException(PAYED.name(), orderStatus.name());
        }
        orderStatus = PAYED;
    }

    public void cancel() {
        if (!orderStatus.equals(PAYED)) {
            throw new IllegalOrderStateException(CANCELED.name(), orderStatus.name());
        }
        orderStatus = CANCELED;
    }

    public void delivery() {
        if (!orderStatus.equals(PAYED)) {
            throw new IllegalOrderStateException(ON_DELIVERY.name(), orderStatus.name());
        }
        orderStatus = ON_DELIVERY;
    }

    public void confirm() {
        if (!orderStatus.equals(ON_DELIVERY)) {
            throw new IllegalOrderStateException(CONFIRMED.name(), orderStatus.name());
        }
        orderStatus = CONFIRMED;
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
        if (address.length() < MIN_ADDRESS || MAX_ADDRESS < address.length()) {
            throw new ArgumentOutOfBoundException(ADDRESS_FIELD_NAME, MIN_ADDRESS, MAX_ADDRESS, address.length());
        }
    }
}
