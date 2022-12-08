package com.prgrms.merskkurly.domain.order.service;

import com.prgrms.merskkurly.domain.common.exception.NotFoundException;
import com.prgrms.merskkurly.domain.item.dto.ItemResponse.Details;
import com.prgrms.merskkurly.domain.item.entity.Item;
import com.prgrms.merskkurly.domain.order.dto.OrderResponse;
import com.prgrms.merskkurly.domain.order.dto.OrderResponse.Shortcuts;
import com.prgrms.merskkurly.domain.order.entity.Order;
import com.prgrms.merskkurly.domain.order.repository.OrderRepository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

  private final OrderRepository orderRepository;

  public OrderService(OrderRepository orderRepository){
    this.orderRepository = orderRepository;
  }

  public List<Shortcuts> findByMemberId(Long memberId) {
    List<Order> orders = orderRepository.findByMemberId(memberId);

    return orders.stream()
        .map(order -> new OrderResponse.Shortcuts())
        .collect(Collectors.toList());
  }

  public OrderResponse.Details findById(Long memberId, Long orderId) {
    Optional<Order> found = orderRepository.findById(memberId, orderId);

    if(found.isEmpty()){
      throw new NotFoundException("Not Found Order id: " + orderId);
    }

    Order order = found.get();
    return OrderResponse.Details.from(order);
  }
}
