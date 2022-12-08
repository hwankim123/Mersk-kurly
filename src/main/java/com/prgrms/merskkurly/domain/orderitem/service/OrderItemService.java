package com.prgrms.merskkurly.domain.orderitem.service;

import com.prgrms.merskkurly.domain.common.exception.NotFoundException;
import com.prgrms.merskkurly.domain.item.entity.Item;
import com.prgrms.merskkurly.domain.item.repository.ItemRepository;
import com.prgrms.merskkurly.domain.orderitem.dto.OrderItemRequest;
import com.prgrms.merskkurly.domain.orderitem.entity.OrderItem;
import com.prgrms.merskkurly.domain.orderitem.repository.OrderItemRepository;
import java.util.Optional;
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

    orderItemRepository.save(orderItem);
  }
}
