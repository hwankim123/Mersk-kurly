package com.prgrms.merskkurly.domain.orderitem.controller;

import static com.prgrms.merskkurly.domain.common.auth.SessionAttributes.ID;

import com.prgrms.merskkurly.domain.common.exception.UnAuthorizedException;
import com.prgrms.merskkurly.domain.orderitem.dto.OrderItemRequest;
import com.prgrms.merskkurly.domain.orderitem.dto.OrderItemResponse;
import com.prgrms.merskkurly.domain.orderitem.service.OrderItemService;
import java.net.URI;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/kart")
public class OrderItemController {

  private final OrderItemService orderItemService;

  public OrderItemController(OrderItemService orderItemService){
    this.orderItemService = orderItemService;
  }

  @GetMapping
  public ResponseEntity<List<OrderItemResponse.Details>> showKart(HttpServletRequest request){
    HttpSession session = request.getSession();
    Long memberId = (Long) session.getAttribute(ID);
    if (memberId == null) {
      throw new UnAuthorizedException("UnAuthorized");
    }

    List<OrderItemResponse.Details> orderItems = orderItemService.findByMemberIdAndNotOrdered(memberId);
    return ResponseEntity.ok(orderItems);
  }

  @PostMapping
  public ResponseEntity<?> add(@RequestBody OrderItemRequest.AddForm addForm, HttpServletRequest request){
    HttpSession session = request.getSession();
    Long memberId = (Long) session.getAttribute(ID);
    if (memberId == null) {
      throw new UnAuthorizedException("UnAuthorized");
    }

    orderItemService.add(memberId, addForm);

    HttpHeaders headers = new HttpHeaders();
    headers.setLocation(URI.create("/"));
    return new ResponseEntity<>(headers,  HttpStatus.MOVED_PERMANENTLY);
  }

}
