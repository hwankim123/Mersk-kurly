package com.prgrms.merskkurly.domain.order.service;

import com.prgrms.merskkurly.domain.common.exception.NotFoundException;
import com.prgrms.merskkurly.domain.item.service.ItemService;
import com.prgrms.merskkurly.domain.order.dto.OrderRequest;
import com.prgrms.merskkurly.domain.order.dto.OrderResponse;
import com.prgrms.merskkurly.domain.order.dto.OrderResponse.Shortcuts;
import com.prgrms.merskkurly.domain.order.entity.Order;
import com.prgrms.merskkurly.domain.order.entity.OrderStatus;
import com.prgrms.merskkurly.domain.order.repository.OrderRepository;
import com.prgrms.merskkurly.domain.orderitem.dto.OrderItemResponse;
import com.prgrms.merskkurly.domain.orderitem.entity.OrderItem;
import com.prgrms.merskkurly.domain.orderitem.service.OrderItemService;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class OrderService {

  private final OrderRepository orderRepository;
  private final OrderItemService orderItemService;
  private final ItemService itemService;

  public OrderService(OrderRepository orderRepository, OrderItemService orderItemService, ItemService itemService){
    this.orderRepository = orderRepository;
    this.orderItemService = orderItemService;
    this.itemService = itemService;
  }

  @Transactional(readOnly = true)
  public List<OrderResponse.Shortcuts> findAll() {
    List<Order> orders = orderRepository.findAll();

    return orders.stream()
        .map(order -> new OrderResponse.Shortcuts(order.getId(), order.getAddress(), order.getOrderStatus(), order.getUpdatedAt()))
        .collect(Collectors.toList());
  }

  @Transactional(readOnly = true)
  public List<OrderResponse.Shortcuts> findByMemberId(Long memberId) {
    List<Order> orders = orderRepository.findByMemberId(memberId);

    return orders.stream()
        .map(order -> new OrderResponse.Shortcuts(order.getId(), order.getAddress(), order.getOrderStatus(), order.getUpdatedAt()))
        .collect(Collectors.toList());
  }

  @Transactional(readOnly = true)
  public OrderResponse.Shortcuts findById(Long memberId, Long orderId) {
    Optional<Order> found = orderRepository.findByMemberIdAndOrderId(memberId, orderId);
    found.orElseThrow(() -> new NotFoundException("Not Found Order id: " + orderId));

    Order order = found.get();
    return new OrderResponse.Shortcuts(
        order.getId(), order.getAddress(), order.getOrderStatus(), order.getCreatedAt());
  }

  public void order(Long memberId, OrderRequest.OrderForm orderForm) {
    List<OrderItemResponse.Details> detailsList = orderItemService.findByMemberIdAndNotOrdered(memberId);

    Order order = Order.newInstance(memberId, orderForm.getAddress());
    Order savedOrder = orderRepository.save(order);
    detailsList.forEach(details -> {
      OrderItem orderItem = details.mapToOrderItem();
      orderItem.order(savedOrder.getId());
      itemService.order(orderItem.getItemId(), orderItem.getQuantity());
    });
    orderItemService.order(savedOrder.getId(), memberId);
  }

  public void delivery(Long orderId) {
    Optional<Order> found = orderRepository.findById(orderId);
    Order order = found.orElseThrow(() -> new NotFoundException("Not Found Order id: " + orderId));
    order.delivery();
    System.out.println("order.getOrderStatus() = " + order.getOrderStatus());

    orderRepository.update(order);
  }

  public void confirm(Long memberId, Long orderId) {
    Optional<Order> found = orderRepository.findByMemberIdAndOrderId(memberId, orderId);
    Order order = found.orElseThrow(() -> new NotFoundException("Not Found Order id: " + orderId));
    order.confirm();

    orderRepository.update(order);
  }

  public void cancel(Long memberId, Long orderId) {
    Optional<Order> found = orderRepository.findByMemberIdAndOrderId(memberId, orderId);
    Order order = found.orElseThrow(() -> new NotFoundException("Not Found Order id: " + orderId));
    order.cancel();

    orderRepository.update(order);
  }
}
