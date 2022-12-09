package com.prgrms.merskkurly.domain.item.controller;

import com.prgrms.merskkurly.domain.item.dto.ItemRequest;
import com.prgrms.merskkurly.domain.item.dto.ItemResponse;
import com.prgrms.merskkurly.domain.item.service.ItemService;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/item")
public class ItemController {

    private final ItemService itemService;

    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @GetMapping("/search")
    public ResponseEntity<List<ItemResponse.Shortcuts>> search(@RequestBody ItemRequest.SearchForm searchForm) {
        List<ItemResponse.Shortcuts> items = itemService.search(searchForm);
        return ResponseEntity.ok(items);
    }

    @GetMapping("/search/{itemId}")
    public ResponseEntity<ItemResponse.Details> itemDetailForUser(@PathVariable Long itemId) {
        ItemResponse.Details item = itemService.findById(itemId);
        return ResponseEntity.ok(item);
    }
}
