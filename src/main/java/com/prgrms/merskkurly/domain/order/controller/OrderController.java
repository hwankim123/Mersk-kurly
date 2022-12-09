package com.prgrms.merskkurly.domain.order.controller;

import static com.prgrms.merskkurly.domain.common.auth.SessionAttributes.ID;

import com.prgrms.merskkurly.domain.common.exception.UnAuthorizedException;
import com.prgrms.merskkurly.domain.order.dto.OrderRequest;
import com.prgrms.merskkurly.domain.order.dto.OrderResponse;
import com.prgrms.merskkurly.domain.order.service.OrderService;
import java.net.URI;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
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

    List<OrderResponse.Shortcuts> orders = orderService.findByMemberId(memberId);
    return ResponseEntity.ok(orders);
  }

  @PostMapping
  public ResponseEntity<?> order(@RequestBody OrderRequest.OrderForm orderForm, HttpServletRequest request){
    HttpSession session = request.getSession();
    Long memberId = (Long) session.getAttribute(ID);
    if (memberId == null) {
      throw new UnAuthorizedException("UnAuthorized");
    }

    orderService.order(memberId, orderForm);

    HttpHeaders headers = new HttpHeaders();
    headers.setLocation(URI.create("/"));
    return new ResponseEntity<>(headers, HttpStatus.MOVED_PERMANENTLY);
  }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponse.Shortcuts> details(@PathVariable Long orderId, HttpServletRequest request){
      HttpSession session = request.getSession();
      Long memberId = (Long) session.getAttribute(ID);
      if (memberId == null) {
        throw new UnAuthorizedException("UnAuthorized");
      }

      OrderResponse.Shortcuts order = orderService.findById(memberId, orderId);
      return ResponseEntity.ok(order);
    }

  @PutMapping("/{orderId}/confirm")
  public void confirm(@PathVariable Long orderId, HttpServletRequest request){
    HttpSession session = request.getSession();
    Long memberId = (Long) session.getAttribute(ID);
    if (memberId == null) {
      throw new UnAuthorizedException("UnAuthorized");
    }

    orderService.confirm(memberId, orderId);
  }

  @PutMapping("/{orderId}/cancel")
  public void cancel(@PathVariable Long orderId, HttpServletRequest request){
    HttpSession session = request.getSession();
    Long memberId = (Long) session.getAttribute(ID);
    if (memberId == null) {
      throw new UnAuthorizedException("UnAuthorized");
    }

    orderService.cancel(memberId, orderId);
  }
}
