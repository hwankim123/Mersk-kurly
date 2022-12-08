package com.prgrms.merskkurly.domain.order.controller;

import static com.prgrms.merskkurly.domain.common.auth.SessionAttributes.ID;
import static com.prgrms.merskkurly.domain.common.auth.SessionAttributes.ROLE;

import com.prgrms.merskkurly.domain.common.exception.ForbiddenException;
import com.prgrms.merskkurly.domain.common.exception.UnAuthorizedException;
import com.prgrms.merskkurly.domain.member.entity.Role;
import com.prgrms.merskkurly.domain.order.dto.OrderResponse;
import com.prgrms.merskkurly.domain.order.dto.OrderResponse.Shortcuts;
import com.prgrms.merskkurly.domain.order.service.OrderService;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/order")
public class OrderController {

  private final OrderService orderService;

  public OrderController(OrderService orderService){
    this.orderService = orderService;
  }

  @GetMapping
  public ResponseEntity<List<OrderResponse.Shortcuts>> orders(HttpServletRequest request){
    HttpSession session = request.getSession();
    Long memberId = (Long) session.getAttribute(ID);
    if (memberId == null) {
      throw new UnAuthorizedException("UnAuthorized");
    }

    List<Shortcuts> orders = orderService.findByMemberId(memberId);
    return ResponseEntity.ok(orders);
  }

  @GetMapping("/{orderId}")
  public void details(@PathVariable Long orderId, HttpServletRequest request){
    HttpSession session = request.getSession();
    Long memberId = (Long) session.getAttribute(ID);
    if (memberId == null) {
      throw new UnAuthorizedException("UnAuthorized");
    }

    orderService.findById(memberId, orderId);
  }

  @PostMapping("/order")
  public void order(){}

  @PutMapping("/{orderId}/confirm")
  public void confirm(){}

  @PutMapping("/{orderId}/cancel")
  public void cancel(){}
}
