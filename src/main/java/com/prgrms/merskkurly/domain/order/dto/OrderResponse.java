package com.prgrms.merskkurly.domain.order.dto;

import com.prgrms.merskkurly.domain.order.entity.Order;
import com.prgrms.merskkurly.domain.order.entity.OrderStatus;
import java.time.LocalDateTime;

public class OrderResponse {

  public static class Shortcuts {
    private Long id;
    private String address;
    private OrderStatus orderStatus;
    private LocalDateTime createdAt;

    public Shortcuts(Long id, String address, OrderStatus orderStatus, LocalDateTime createdAt) {
      this.id = id;
      this.address = address;
      this.orderStatus = orderStatus;
      this.createdAt = createdAt;
    }

    public Long getId() {
      return id;
    }

    public void setId(Long id) {
      this.id = id;
    }

    public String getAddress() {
      return address;
    }

    public void setAddress(String address) {
      this.address = address;
    }

    public OrderStatus getOrderStatus() {
      return orderStatus;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
      this.orderStatus = orderStatus;
    }

    public LocalDateTime getCreatedAt() {
      return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
      this.createdAt = createdAt;
    }
  }
}
