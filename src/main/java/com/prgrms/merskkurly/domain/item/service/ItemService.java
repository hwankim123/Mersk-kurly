package com.prgrms.merskkurly.domain.item.service;

import com.prgrms.merskkurly.domain.common.exception.NotFoundException;
import com.prgrms.merskkurly.domain.item.dto.ItemRequest;
import com.prgrms.merskkurly.domain.item.dto.ItemResponse;
import com.prgrms.merskkurly.domain.item.entity.Item;
import com.prgrms.merskkurly.domain.item.repository.ItemRepository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ItemService {

  private static final int TOP_RANK = 1;

  private final ItemRepository itemRepository;

  public ItemService(ItemRepository itemRepository) {
    this.itemRepository = itemRepository;
  }

  @Transactional(readOnly = true)
  public List<ItemResponse.Shortcuts> findTopRankingItems() {
    List<Item> items = itemRepository.findByBuyCntLimit(TOP_RANK);
    return items.stream()
        .map(item -> new ItemResponse.Shortcuts(item.getId(), item.getName(), item.getPrice(),
            item.getBuyCnt()))
        .collect(Collectors.toList());
  }

  @Transactional(readOnly = true)
  public List<ItemResponse.Shortcuts> findAllByMemberId(Long memberId) {
    List<Item> items = itemRepository.findByMemberId(memberId);
    return items.stream()
        .map(item -> new ItemResponse.Shortcuts(item.getId(), item.getName(), item.getPrice(),
            item.getBuyCnt()))
        .collect(Collectors.toList());
  }

  @Transactional(readOnly = true)
  public ItemResponse.Details findById(Long id) {
    Optional<Item> found = itemRepository.findById(id);
    Item item = found.orElseThrow(() -> new NotFoundException("Not Found Item id: " + id));

    return ItemResponse.Details.from(item);
  }

  public void newItem(ItemRequest.NewForm newForm) {
    Item item = Item.newInstance(newForm.getMemberId(),
        newForm.getName(),
        newForm.getCategory(),
        newForm.getDescription(),
        newForm.getPrice(),
        newForm.getStock());

    itemRepository.save(item);
  }

  public void update(ItemResponse.UpdateForm updateForm) {
    Optional<Item> found = itemRepository.findById(updateForm.getId());
    Item item = found.orElseThrow(
        () -> new NotFoundException("Not Found Item id: " + updateForm.getId()));

    item.update(
        updateForm.getName(),
        updateForm.getCategory(),
        updateForm.getDescription(),
        updateForm.getPrice(),
        updateForm.getStock());

    itemRepository.update(item);
  }

  public void delete(Long id) {
    itemRepository.delete(id);
  }

  public List<ItemResponse.Shortcuts> search(ItemRequest.SearchForm searchForm) {
    List<Item> result = itemRepository.search(searchForm.getKeyword(), searchForm.getCategory());
    return result.stream()
        .map(item -> new ItemResponse.Shortcuts(item.getId(), item.getName(), item.getPrice(),
            item.getBuyCnt()))
        .collect(Collectors.toList());
  }

  public void order(Long id, int quantity) {
    Optional<Item> found = itemRepository.findById(id);
    Item item = found.orElseThrow(() -> new NotFoundException("Not Found Item id: " + id));

    item.release(quantity);
    itemRepository.update(item);
  }
}
