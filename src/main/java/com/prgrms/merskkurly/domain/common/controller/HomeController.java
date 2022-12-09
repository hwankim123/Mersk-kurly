package com.prgrms.merskkurly.domain.common.controller;

import com.prgrms.merskkurly.domain.common.dto.HomeResponse;
import com.prgrms.merskkurly.domain.item.dto.ItemResponse.Shortcuts;
import com.prgrms.merskkurly.domain.item.entity.Category;
import com.prgrms.merskkurly.domain.item.service.ItemService;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    private final ItemService itemService;

    public HomeController(ItemService itemService) {
        this.itemService = itemService;
    }

    @GetMapping("/")
    public ResponseEntity<HomeResponse> home() {
        List<Shortcuts> topRankingItems = itemService.findTopRankingItems();
        HomeResponse homeResponse = new HomeResponse(List.of(Category.values()), topRankingItems);
        return ResponseEntity.ok(homeResponse);
    }
}
