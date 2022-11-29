package com.prgrms.merskkurly.domain.order.entity;

import com.prgrms.merskkurly.domain.common.exception.domain.ArgumentOutOfBoundException;
import com.prgrms.merskkurly.domain.common.exception.domain.IllegalOrderStateException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.EnumSource;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class OrderTest {

    /**
     * 신규 주문 객체를 생성할 수 있음
     *  - validation에 실패할 때 객체 생성에 실패함
     * 기존 주문 데이터로부터 주문 객체를 생성할 수 있음
     * 주문 정보를 수정할 수 있음
     *  - 배송중, 혹은 배송 완료 상태라면 주문을 수정할 수 없음
     *  - 이메일, 주소, 주문 상태를 수정할 수 있고, updatedAt는 자동으로 수정됨
     *  - 주문을 취소할 수 있음
     *   - 주문 상태가 "배송 확정"이라면 주문을 취소할 수 없음
     *  생성된 주문 상태를 "배송 준비" 상태로 만들 수 있음
     *   - 생성된 주문 상태를 "배송 확정" 상태로 만드려고 할 시 상태 변경 실패
     *  배송 준비(배송 중인) 상태의 주문 상태를 "배송 확정" 상태로 만들 수 있음
     *   - 배송 확정 상태의 모든 주문 정보를 수정하려고 할 때 상태 변경 실패
     */

    @Test
    @DisplayName("신규 주문 객체를 생성할 수 있습니다")
    void newOrder() {
        //given
        String address = "경기도 성남시 분당구 판교동 123-234";

        //when
        Order order = Order.newInstance(address);

        //then
        assertThat(order.getAddress()).isEqualTo(address);
        assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.ACCEPTED);
    }

    @ParameterizedTest
    @EnumSource(OrderStatus.class)
    @DisplayName("기존 주문 데이터를 통해 주문 객체를 생성할 수 있습니다")
    void getOrder(OrderStatus orderStatus) {
        //given
        Order order = OrderGenerator();
        Long id = order.getId();
        Long memberId = order.getMemberId();
        String address = order.getAddress();
        LocalDateTime createdAt = order.getCreatedAt();
        LocalDateTime updatedAt = order.getUpdatedAt();

        //when
        Order getOrder = Order.getInstance(id, memberId, address, orderStatus, createdAt, updatedAt);

        //then
        assertThat(getOrder.getId()).isEqualTo(id);
        assertThat(getOrder.getMemberId()).isEqualTo(memberId);
        assertThat(getOrder.getAddress()).isEqualTo(address);
        assertThat(getOrder.getOrderStatus()).isEqualTo(orderStatus);
        assertThat(getOrder.getCreatedAt()).isEqualTo(createdAt);
        assertThat(getOrder.getUpdatedAt()).isEqualTo(updatedAt);
    }

    @ParameterizedTest
    @CsvSource(value = {"성남시 분당구 판교동", "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa" +
            "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"})
    @DisplayName("주소가 12글자에서 100글자 사이에 이루어져있지 않으면 신규 회원 생성에 실패합니다")
    void orderCreateFailedAddressLength(String address) {
        //when & then
        assertThrows(ArgumentOutOfBoundException.class, () -> Order.newInstance(address));
    }

    private Order OrderGenerator(){
        String address = "경기도 성남시 분당구 판교동 123-234";
        return Order.newInstance(address);
    }

    @ParameterizedTest
    @CsvSource({"CANCEL", "READY_FOR_DELIVERY"})
    @DisplayName("주문 취소, 혹은 배송 준비 상태의 주문을 확정할 수 있습니다")
    void acceptOrder(String OrderStatusString) {
        //given
        Order order = Order.getInstance(0L, 0L, "경기도 성남시 분당구 판교동 123-234", OrderStatus.valueOf(OrderStatusString), LocalDateTime.now(), LocalDateTime.now());

        //when
        order.accept();

        // then
        assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.ACCEPTED);
    }

    @ParameterizedTest
    @CsvSource({"ACCEPTED", "SETTLED"})
    @DisplayName("주문 확정, 혹은 배송 완료 상태의 주문을 확정할 수 없습니다")
    void acceptOrderFailure(String OrderStatusString) {
        //given
        Order order = Order.getInstance(0L, 0L, "경기도 성남시 분당구 판교동 123-234", OrderStatus.valueOf(OrderStatusString), LocalDateTime.now(), LocalDateTime.now());

        //when&then
        assertThrows(IllegalOrderStateException.class, order::accept);
    }

    @ParameterizedTest
    @CsvSource({"ACCEPTED", "READY_FOR_DELIVERY"})
    @DisplayName("주문 확정, 혹은 배송 준비 상태의 주문을 취소할 수 있습니다")
    void cancelOrder(String OrderStatusString) {
        //given
        Order order = Order.getInstance(0L, 0L, "경기도 성남시 분당구 판교동 123-234", OrderStatus.valueOf(OrderStatusString), LocalDateTime.now(), LocalDateTime.now());

        //when
        order.cancel();

        // then
        assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.CANCEL);
    }

    @ParameterizedTest
    @CsvSource(value = {"SETTLED", "CANCEL"})
    @DisplayName("배달 완료 상태, 혹은 이미 취소된 주문을 취소할 수 없습니다")
    void cancelOrderFailed(String orderStatusString) {
        //given
        Order order = Order.getInstance(0L, 0L, "경기도 성남시 분당구 판교동 123-234", OrderStatus.valueOf(orderStatusString), LocalDateTime.now(), LocalDateTime.now());

        //when&then
        assertThrows(IllegalOrderStateException.class, order::cancel);
    }

    @ParameterizedTest
    @CsvSource(value = {"ACCEPTED", "SETTLED"})
    @DisplayName("주문 확정, 혹은 배달 완료 상태의 주문 상태를 배송 준비 상태로 만들 수 있습니다")
    void orderSetReadyForDelivery(String orderStatusString) {
        //given
        Order order = Order.getInstance(0L, 0L, "경기도 성남시 분당구 판교동 123-234", OrderStatus.valueOf(orderStatusString), LocalDateTime.now(), LocalDateTime.now());

        //when
        order.readyForDelivery();

        //then
        assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.READY_FOR_DELIVERY);
    }

    @ParameterizedTest
    @CsvSource(value = {"READY_FOR_DELIVERY", "CANCEL"})
    @DisplayName("주문 확정, 혹은 배달 완료 상태의 주문 상태를 배송 준비 상태로 만들 수 있습니다")
    void readyForDelieryFailure(String orderStatusString) {
        //given
        Order order = Order.getInstance(0L, 0L, "경기도 성남시 분당구 판교동 123-234", OrderStatus.valueOf(orderStatusString), LocalDateTime.now(), LocalDateTime.now());

        //when&then
        assertThrows(IllegalOrderStateException.class, order::readyForDelivery);
    }

    @Test
    @DisplayName("배송 준비 상태의 주문 상태를 배송 확정 상태로 만들 수 있습니다")
    void settleOrder() {
        //given
        Order order = Order.getInstance(0L, 0L, "경기도 성남시 분당구 판교동 123-234", OrderStatus.READY_FOR_DELIVERY, LocalDateTime.now(), LocalDateTime.now());

        //when
        order.settle();

        //then
        assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.SETTLED);
    }

    @ParameterizedTest
    @CsvSource(value = {"ACCEPTED", "SETTLED", "CANCEL"})
    @DisplayName("주문 확정, 배송 완료, 혹은 주문 취소 상태의 주문 상태를 배송 확정 상태로 만들 수 없습니다")
    void orderSetSettledFailed(String orderStatusString) {
        //given
        Order order = Order.getInstance(0L, 0L, "경기도 성남시 분당구 판교동 123-234", OrderStatus.valueOf(orderStatusString), LocalDateTime.now(), LocalDateTime.now());

        //when&then
        assertThrows(IllegalOrderStateException.class, order::settle);
    }

    @Test
    @DisplayName("배송 확정 상태의 모든 주문 정보를 수정하려고 할 때 상태 변경 실패")
    void updateOrderFailedSettledOrder() {
        //given
        Order order = Order.getInstance(0L, 0L, "경기도 성남시 분당구 판교동 123-234", OrderStatus.SETTLED, LocalDateTime.now(), LocalDateTime.now());
        String newAddress = "newAddress12345667789";
        OrderStatus newOrderStatus = OrderStatus.ACCEPTED;

        //when&then
        assertThrows(IllegalOrderStateException.class, () -> order.update(newAddress, newOrderStatus));

    }
}