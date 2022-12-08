package com.prgrms.merskkurly.domain.orderitem.dto;

public class OrderItemRequest {

  public static class AddForm{
    private Long itemId;
    private int quantity;

    public AddForm(){}

    public AddForm(Long itemId, int quantity) {
      this.itemId = itemId;
      this.quantity = quantity;
    }

    public Long getItemId() {
      return itemId;
    }

    public void setItemId(Long itemId) {
      this.itemId = itemId;
    }

    public int getQuantity() {
      return quantity;
    }

    public void setQuantity(int quantity) {
      this.quantity = quantity;
    }
  }

}
