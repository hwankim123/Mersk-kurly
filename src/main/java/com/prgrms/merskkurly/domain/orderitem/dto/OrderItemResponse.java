package com.prgrms.merskkurly.domain.orderitem.dto;

import com.prgrms.merskkurly.domain.orderitem.entity.OrderItem;

import java.time.LocalDateTime;

public class OrderItemResponse {

    public static class Details {

        private Long id;
        private Long memberId;
        private Long itemId;
        private Long orderId;
        private int quantity;
        private boolean isOrdered;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public Details() {
        }

        public static Details from(OrderItem orderItem) {
            Details details = new Details();
            details.id = orderItem.getId();
            details.memberId = orderItem.getMemberId();
            details.itemId = orderItem.getItemId();
            details.orderId = orderItem.getOrderId();
            details.quantity = orderItem.getQuantity();
            details.isOrdered = orderItem.getIsOrdered();
            details.createdAt = orderItem.getCreatedAt();
            details.updatedAt = orderItem.getUpdatedAt();

            return details;
        }

        public OrderItem mapToOrderItem() {

            return OrderItem.getInstance(this.id, this.memberId, this.itemId, this.orderId, this.quantity,
                    this.isOrdered, this.createdAt, this.updatedAt);
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public Long getMemberId() {
            return memberId;
        }

        public void setMemberId(Long memberId) {
            this.memberId = memberId;
        }

        public Long getItemId() {
            return itemId;
        }

        public void setItemId(Long itemId) {
            this.itemId = itemId;
        }

        public Long getOrderId() {
            return orderId;
        }

        public void setOrderId(Long orderId) {
            this.orderId = orderId;
        }

        public int getQuantity() {
            return quantity;
        }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }

        public boolean getIsOrdered() {
            return isOrdered;
        }

        public void setIsOrdered(boolean isOrdered) {
            this.isOrdered = isOrdered;
        }

        public LocalDateTime getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
        }

        public LocalDateTime getUpdatedAt() {
            return updatedAt;
        }

        public void setUpdatedAt(LocalDateTime updatedAt) {
            this.updatedAt = updatedAt;
        }
    }
}
