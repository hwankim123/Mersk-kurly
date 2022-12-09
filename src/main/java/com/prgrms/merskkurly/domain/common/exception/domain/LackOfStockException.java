package com.prgrms.merskkurly.domain.common.exception.domain;

public class LackOfStockException extends RuntimeException {
    private static final String messageFormat = "LackOfStockException exception occurred when item %s was ordered. Stock was %d, but quantity of order was %d.";
    private final String itemName;
    private final int stock;
    private final int quantity;

    public LackOfStockException(String itemName, int stock, int quantity) {
        super(String.format(messageFormat, itemName, stock, quantity));
        this.itemName = itemName;
        this.stock = stock;
        this.quantity = quantity;
    }

    public String getItemName() {
        return itemName;
    }

    public int getStock() {
        return stock;
    }

    public int getQuantity() {
        return quantity;
    }
}
