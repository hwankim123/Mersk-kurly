package com.prgrms.merskkurly.domain.item.controller;

import static com.prgrms.merskkurly.domain.common.auth.SessionAttributes.ID;
import static com.prgrms.merskkurly.domain.common.auth.SessionAttributes.ROLE;

import com.prgrms.merskkurly.domain.common.exception.ForbiddenException;
import com.prgrms.merskkurly.domain.common.exception.UnAuthorizedException;
import com.prgrms.merskkurly.domain.item.dto.ItemRequest;
import com.prgrms.merskkurly.domain.item.dto.ItemResponse;
import com.prgrms.merskkurly.domain.item.dto.ItemResponse.Details;
import com.prgrms.merskkurly.domain.item.dto.ItemResponse.Shortcuts;
import com.prgrms.merskkurly.domain.item.entity.Category;
import com.prgrms.merskkurly.domain.item.service.ItemService;
import com.prgrms.merskkurly.domain.member.entity.Role;
import java.net.URI;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/item")
public class ItemController {

  private final ItemService itemService;

  public ItemController(ItemService itemService){
    this.itemService = itemService;
  }

  @GetMapping("/search")
  public ResponseEntity<List<Shortcuts>> search(@RequestBody ItemRequest.SearchForm searchForm) {
    List<Shortcuts> items = itemService.search(searchForm);
    return ResponseEntity.ok(items);
  }

  @GetMapping("/search/{itemId}")
  public ResponseEntity<ItemResponse.Details> itemDetailForUser(@PathVariable Long itemId) {
    Details item = itemService.findById(itemId);
    return ResponseEntity.ok(item);
  }
}
