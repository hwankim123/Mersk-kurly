package com.prgrms.merskkurly.domain.order.dto;

import com.prgrms.merskkurly.domain.order.entity.Order;

public class OrderResponse {

  public static class Shortcuts {
    private Long id;
  }

  public static class Details {
    private Long id;

    public static Details from(Order order) {
      Details details = new Details();

      //dosomething...

      return details;
    }
  }
}
