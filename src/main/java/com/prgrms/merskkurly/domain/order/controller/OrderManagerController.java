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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/order/manage")
public class OrderManagerController {

  private final OrderService orderService;

  public OrderManagerController(OrderService orderService){
    this.orderService = orderService;
  }

  @GetMapping
  public ResponseEntity<List<OrderResponse.Shortcuts>> orders(HttpServletRequest request){
    HttpSession session = request.getSession();
    Long memberId = (Long) session.getAttribute(ID);
    if (memberId == null) {
      throw new UnAuthorizedException("UnAuthorized");
    }

    Role role = Role.valueOf((String) session.getAttribute(ROLE));
    if (role.equals(Role.USER) || role.equals(Role.ADMIN)) {
      throw new ForbiddenException("Forbidden");
    }

    List<OrderResponse.Shortcuts> orders = orderService.findAll();
    return ResponseEntity.ok(orders);
  }

  @PutMapping("/{orderId}")
  public void startDelivery(@PathVariable Long orderId, HttpServletRequest request){
    HttpSession session = request.getSession();
    Long memberId = (Long) session.getAttribute(ID);
    if (memberId == null) {
      throw new UnAuthorizedException("UnAuthorized");
    }

    Role role = Role.valueOf((String) session.getAttribute(ROLE));
    if (role.equals(Role.USER) || role.equals(Role.ADMIN)) {
      throw new ForbiddenException("Forbidden");
    }

    orderService.delivery(orderId);
  }
}
