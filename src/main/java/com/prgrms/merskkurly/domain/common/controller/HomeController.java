package com.prgrms.merskkurly.domain.common.controller;

import static com.prgrms.merskkurly.domain.common.auth.SessionAttributes.*;

import com.prgrms.merskkurly.domain.common.dto.AuthDto;
import com.prgrms.merskkurly.domain.common.dto.HomeResponse;
import com.prgrms.merskkurly.domain.item.dto.ItemRequest;
import com.prgrms.merskkurly.domain.item.dto.ItemResponse.Shortcuts;
import com.prgrms.merskkurly.domain.item.entity.Category;
import com.prgrms.merskkurly.domain.item.service.ItemService;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

  private final ItemService itemService;

  public HomeController(ItemService itemService) {
    this.itemService = itemService;
  }

  @GetMapping("/")
  public ResponseEntity<HomeResponse> home(Model model, HttpServletRequest request) {
    HttpSession session = request.getSession();
    if (session.getAttribute(ID) == null) {
      model.addAttribute("authDto", new AuthDto());
    } else {
      model.addAttribute(
          new AuthDto(
              (Long) session.getAttribute(ID),
              (String) session.getAttribute(NAME),
              (String) session.getAttribute(ROLE)
          ));
    }
    List<Shortcuts> topRankingItems = itemService.findTopRankingItems();
    HomeResponse homeResponse = new HomeResponse(List.of(Category.values()), topRankingItems);
    return ResponseEntity.ok(homeResponse);
  }
}
