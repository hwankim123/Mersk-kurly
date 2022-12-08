package com.prgrms.merskkurly.domain.item.controller;

import static com.prgrms.merskkurly.domain.common.auth.SessionAttributes.ID;
import static com.prgrms.merskkurly.domain.common.auth.SessionAttributes.ROLE;

import com.prgrms.merskkurly.domain.common.exception.ForbiddenException;
import com.prgrms.merskkurly.domain.common.exception.UnAuthorizedException;
import com.prgrms.merskkurly.domain.item.dto.ItemRequest;
import com.prgrms.merskkurly.domain.item.dto.ItemResponse;
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
@RequestMapping("/item/manage")
public class ItemManageController {

  private final ItemService itemService;

  public ItemManageController(ItemService itemService) {
    this.itemService = itemService;
  }

  @GetMapping
  public ResponseEntity<List<Shortcuts>> showAllAdminItems(
      @ModelAttribute ItemRequest.SearchForm searchForm, HttpServletRequest request) {
    HttpSession session = request.getSession();
    Long memberId = (Long) session.getAttribute(ID);
    if (memberId == null) {
      throw new UnAuthorizedException("UnAuthorized");
    }

    Role role = Role.valueOf((String) session.getAttribute(ROLE));
    if (role.equals(Role.USER)) {
      throw new ForbiddenException("Forbidden");
    }

    List<Shortcuts> items = itemService.findAllByMemberId(memberId);
    return ResponseEntity.ok(items);
  }

  @GetMapping("/newForm")
  public ResponseEntity<List<Category>> newItemForm(HttpServletRequest request) {
    HttpSession session = request.getSession();
    Long memberId = (Long) session.getAttribute(ID);
    if (memberId == null) {
      throw new UnAuthorizedException("UnAuthorized");
    }

    Role role = Role.valueOf((String) session.getAttribute(ROLE));
    if (role.equals(Role.USER)) {
      throw new ForbiddenException("Forbidden");
    }

    List<Category> categories = List.of(Category.values());
    return ResponseEntity.ok(categories);
  }

  @PostMapping
  public ResponseEntity<?> newItem(@RequestBody ItemRequest.NewForm newForm,
      HttpServletRequest request) {
    HttpSession session = request.getSession();
    Long memberId = (Long) session.getAttribute(ID);
    if (memberId == null) {
      throw new UnAuthorizedException("UnAuthorized");
    }

    Role role = Role.valueOf((String) session.getAttribute(ROLE));
    if (role.equals(Role.USER)) {
      throw new ForbiddenException("Forbidden");
    }

    newForm.setMemberId(memberId);
    itemService.newItem(newForm);

    HttpHeaders headers = new HttpHeaders();
    headers.setLocation(URI.create("/manage/item"));
    return new ResponseEntity<>(headers, HttpStatus.MOVED_PERMANENTLY);
  }

  @GetMapping("/{itemId}")
  public ResponseEntity<ItemResponse.Details> itemDetailForAdmin(@PathVariable Long itemId,
      HttpServletRequest request) {
    HttpSession session = request.getSession();
    Long memberId = (Long) session.getAttribute(ID);
    if (memberId == null) {
      throw new UnAuthorizedException("UnAuthorized");
    }

    Role role = Role.valueOf((String) session.getAttribute(ROLE));
    if (role.equals(Role.USER)) {
      throw new ForbiddenException("Forbidden");
    }

    return ResponseEntity.ok(itemService.findById(itemId));
  }

  @GetMapping("/{itemId}/updateForm")
  public ResponseEntity<List<Category>> updateItemForm(@PathVariable Long itemId,
      HttpServletRequest request) {
    HttpSession session = request.getSession();
    Long memberId = (Long) session.getAttribute(ID);
    if (memberId == null) {
      throw new UnAuthorizedException("UnAuthorized");
    }

    Role role = Role.valueOf((String) session.getAttribute(ROLE));
    if (role.equals(Role.USER)) {
      throw new ForbiddenException("Forbidden");
    }

    List<Category> categories = List.of(Category.values());
    return ResponseEntity.ok(categories);
  }

  @PutMapping("/{itemId}")
  public ResponseEntity<?> updateItem(@RequestBody ItemResponse.UpdateForm updateForm,
      @PathVariable Long itemId, HttpServletRequest request) {
    HttpSession session = request.getSession();
    Long memberId = (Long) session.getAttribute(ID);
    if (memberId == null) {
      throw new UnAuthorizedException("UnAuthorized");
    }

    Role role = Role.valueOf((String) session.getAttribute(ROLE));
    if (role.equals(Role.USER)) {
      throw new ForbiddenException("Forbidden");
    }

    updateForm.setId(itemId);
    itemService.update(updateForm);

    HttpHeaders headers = new HttpHeaders();
    headers.setLocation(URI.create("/manage/item" + itemId));
    return new ResponseEntity<>(headers, HttpStatus.MOVED_PERMANENTLY);
  }

  @DeleteMapping("/{itemId}")
  public ResponseEntity<?> deleteItem(@PathVariable Long itemId, HttpServletRequest request) {
    HttpSession session = request.getSession();
    Long memberId = (Long) session.getAttribute(ID);
    if (memberId == null) {
      throw new UnAuthorizedException("UnAuthorized");
    }

    Role role = Role.valueOf((String) session.getAttribute(ROLE));
    if (role.equals(Role.USER)) {
      throw new ForbiddenException("Forbidden");
    }

    itemService.delete(itemId);

    HttpHeaders headers = new HttpHeaders();
    headers.setLocation(URI.create("/manage/item"));
    return new ResponseEntity<>(headers, HttpStatus.MOVED_PERMANENTLY);
  }

}
