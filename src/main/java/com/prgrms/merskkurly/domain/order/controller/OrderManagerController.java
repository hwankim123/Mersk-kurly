package com.prgrms.merskkurly.domain.order.controller;

import static com.prgrms.merskkurly.domain.common.auth.SessionAttributes.ID;
import static com.prgrms.merskkurly.domain.common.auth.SessionAttributes.ROLE;

import com.prgrms.merskkurly.domain.common.exception.ForbiddenException;
import com.prgrms.merskkurly.domain.common.exception.UnAuthorizedException;
import com.prgrms.merskkurly.domain.member.entity.Role;
import com.prgrms.merskkurly.domain.order.service.OrderService;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
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
  public void orders(HttpServletRequest request){
    HttpSession session = request.getSession();
    Long memberId = (Long) session.getAttribute(ID);
    if (memberId == null) {
      throw new UnAuthorizedException("UnAuthorized");
    }

    Role role = Role.valueOf((String) session.getAttribute(ROLE));
    if (role.equals(Role.USER)) {
      throw new ForbiddenException("Forbidden");
    }

    orderService.findByMemberId(memberId);
  }

  @GetMapping("/{orderId}")
  public void details(@PathVariable Long orderId){}

  @PutMapping("/{orderId}/on-delivery")
  public void startDelivery(){}
}
