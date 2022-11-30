package com.prgrms.merskkurly.domain.order.entity;

import com.prgrms.merskkurly.domain.common.exception.domain.ArgumentOutOfBoundException;
import com.prgrms.merskkurly.domain.common.exception.domain.IllegalOrderStateException;
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
     *  - payed 상태의 주문을 cancel 상태로 만들 있음
     *  - 주문 생성 시, 혹은 취소된 주문을 payed상태로 만들 수 있음
     *  - 결제완료된 주문을 on_delivery상태로 만들 수 있음
     *  - 배송 중인 주문을 settled 상태로 만들 수 있음
     *
     *   - PAYED: CANCELED 상태가 아닐 때 pay을 호출하면 예외 발생
     *   - ON_DELIVERY: PAYED 상태가 아닐 때 delivery를 호출하면 예외 발생
     *   - SETTLED: ON_DELIVERY 상태가 아닐 때 settle를 호출하면 예외 발생
     *   - CANCELED: PAYED 상태가 아닐 떄 cancel을 호출하면 예외 발생
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
        assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.PAYED);
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

    @Test
    @DisplayName("CANCELED 상태의 주문을 pay할 수 있습니다")
    void payOrder() {
        //given
        Order order = Order.getInstance(0L, 0L, "경기도 성남시 분당구 판교동 123-234", OrderStatus.CANCELED, LocalDateTime.now(), LocalDateTime.now());

        //when
        order.pay();

        //then
        assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.PAYED);
    }

    @ParameterizedTest
    @CsvSource({"PAYED", "ON_DELIVERY", "CONFIRMED"})
    @DisplayName("CANCELD 상태의 주문을 pay할 수 없습니다")
    void payOrderFailure(String OrderStatusString) {
        //given
        Order order = Order.getInstance(0L, 0L, "경기도 성남시 분당구 판교동 123-234", OrderStatus.valueOf(OrderStatusString), LocalDateTime.now(), LocalDateTime.now());

        //when&then
        assertThrows(IllegalOrderStateException.class, order::pay);
    }

    @Test
    @DisplayName("PAYED 상태의 주문을 cancel할 수 있습니다")
    void cancelOrder() {
        //given
        Order order = Order.getInstance(0L, 0L, "경기도 성남시 분당구 판교동 123-234", OrderStatus.PAYED, LocalDateTime.now(), LocalDateTime.now());

        //when
        order.cancel();

        //then
        assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.CANCELED);
    }

    @ParameterizedTest
    @CsvSource(value = {"ON_DELIVERY", "CONFIRMED", "CANCELED"})
    @DisplayName("PAYED 상태가 아니면 주문을 cancel할 수 없습니다")
    void cancelOrderFailure(String orderStatusString) {
        //given
        Order order = Order.getInstance(0L, 0L, "경기도 성남시 분당구 판교동 123-234", OrderStatus.valueOf(orderStatusString), LocalDateTime.now(), LocalDateTime.now());

        //when&then
        assertThrows(IllegalOrderStateException.class, order::cancel);
    }

    @Test
    @DisplayName("PAYED 상태의 주문을 delivery할 수 있습니다")
    void deliveryOrder() {
        //given
        Order order = Order.getInstance(0L, 0L, "경기도 성남시 분당구 판교동 123-234", OrderStatus.PAYED, LocalDateTime.now(), LocalDateTime.now());

        //when
        order.delivery();

        //then
        assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.ON_DELIVERY);
    }

    @ParameterizedTest
    @CsvSource(value = {"ON_DELIVERY", "CONFIRMED", "CANCELED"})
    @DisplayName("PAYED 상태가 아닌 주문을 delivery할 수 없습니다")
    void deliveryOrderFailure(String orderStatusString) {
        //given
        Order order = Order.getInstance(0L, 0L, "경기도 성남시 분당구 판교동 123-234", OrderStatus.valueOf(orderStatusString), LocalDateTime.now(), LocalDateTime.now());

        //when&then
        assertThrows(IllegalOrderStateException.class, order::delivery);
    }

    @Test
    @DisplayName("ON_DELIVERY상태의 주문을 confirm할 수 있습니다")
    void confirmOrder() {
        //given
        Order order = Order.getInstance(0L, 0L, "경기도 성남시 분당구 판교동 123-234", OrderStatus.ON_DELIVERY, LocalDateTime.now(), LocalDateTime.now());

        //when
        order.confirm();

        //then
        assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.CONFIRMED);
    }

    @ParameterizedTest
    @CsvSource(value = {"PAYED", "CONFIRMED", "CANCELED"})
    @DisplayName("ON_DELIVERY 상태의 주문을 confirm할 수 없습니다")
    void confirmOrderFailure(String orderStatusString) {
        //given
        Order order = Order.getInstance(0L, 0L, "경기도 성남시 분당구 판교동 123-234", OrderStatus.valueOf(orderStatusString), LocalDateTime.now(), LocalDateTime.now());

        //when&then
        assertThrows(IllegalOrderStateException.class, order::confirm);
    }

    @Test
    @DisplayName("배송 확정 상태의 모든 주문 정보를 수정하려고 할 때 상태 변경 실패")
    void updateOrderFailedSettledOrder() {
        //given
        Order order = Order.getInstance(0L, 0L, "경기도 성남시 분당구 판교동 123-234", OrderStatus.CONFIRMED, LocalDateTime.now(), LocalDateTime.now());
        String newAddress = "newAddress12345667789";

        //when&then
        assertThrows(IllegalOrderStateException.class, () -> order.update(newAddress));
    }
}