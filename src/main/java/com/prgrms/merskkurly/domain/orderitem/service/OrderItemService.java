package com.prgrms.merskkurly.domain.orderitem.service;

import com.prgrms.merskkurly.domain.common.exception.NotFoundException;
import com.prgrms.merskkurly.domain.item.entity.Item;
import com.prgrms.merskkurly.domain.item.repository.ItemRepository;
import com.prgrms.merskkurly.domain.orderitem.dto.OrderItemRequest;
import com.prgrms.merskkurly.domain.orderitem.dto.OrderItemResponse;
import com.prgrms.merskkurly.domain.orderitem.entity.OrderItem;
import com.prgrms.merskkurly.domain.orderitem.repository.OrderItemRepository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class OrderItemService {

  private final OrderItemRepository orderItemRepository;
  private final ItemRepository itemRepository;

  public OrderItemService(OrderItemRepository orderItemRepository, ItemRepository itemRepository){
    this.orderItemRepository = orderItemRepository;
    this.itemRepository = itemRepository;
  }

  public void add(Long memberId, OrderItemRequest.AddForm addForm) {
    Optional<Item> found = itemRepository.findById(addForm.getItemId()); // memberId도 넘겨주기

    if(found.isEmpty()){
      throw new NotFoundException("Item Not Found : " + addForm.getItemId());
    }

    OrderItem orderItem = OrderItem.newInstance(
        memberId, addForm.getItemId(), addForm.getQuantity());

    orderItemRepository.save(orderItem); // save or update
  }

  public List<OrderItem> findByMemberId(Long memberId) {
    List<OrderItem> orderItems = orderItemRepository.findByMemberId(memberId);
    if(orderItems.isEmpty()){
      throw new IllegalStateException("Kart is empty. You must add item in your kart.");
    }

    return orderItems;
  }

  public List<OrderItemResponse.Details> findByMemberIdAndNotOrdered(Long memberId) {
    List<OrderItem> orderItems = orderItemRepository.findByMemberIdAndNotOrdered(memberId);
    if(orderItems.isEmpty()){
      throw new IllegalStateException("Kart is empty. You must add item in your kart.");
    }

    return orderItems.stream()
        .map(OrderItemResponse.Details::from)
        .collect(Collectors.toList());
  }

  public void order(Long orderId, Long memberId) {
    orderItemRepository.order(orderId, memberId);
  }
}
