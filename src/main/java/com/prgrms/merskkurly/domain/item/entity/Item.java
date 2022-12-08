package com.prgrms.merskkurly.domain.item.entity;

import com.prgrms.merskkurly.domain.common.exception.domain.ArgumentOutOfBoundException;
import com.prgrms.merskkurly.domain.common.exception.domain.LackOfStockException;

import java.time.LocalDateTime;

import static com.prgrms.merskkurly.domain.item.util.ItemValidateFields.*;

public class Item {
    private static final Long BEFORE_INITIALIZED_ID = 0L;
    private static final int ZERO = 0;

    private final Long id;
    private final Long memberId;
    private String name;
    private Category category;
    private String description;
    private int price;
    private int stock;
    private int buyCnt;
    private final LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private Item(Long id, Long memberId){
        this.id = id;
        this.memberId = memberId;
        this.createdAt = LocalDateTime.now();
    }

    public Item(Long id, Long memberId, LocalDateTime createdAt) {
        this.id = id;
        this.memberId = memberId;
        this.createdAt = createdAt;
    }

    public static Item newInstance(Long memberId, String name, Category category, String description, int price, int stock){
        validateName(name);
        validateDescription(description);
        validatePrice(price);
        validateStock(stock);

        Item item = new Item(BEFORE_INITIALIZED_ID, memberId);
        item.name = name;
        item.category = category;
        item.description = description;
        item.price = price;
        item.stock = stock;
        item.buyCnt = ZERO;
        item.updatedAt = item.createdAt;
        return item;
    }

    public static Item getInstance(Long id, Long memberId, String name, Category category, String description, int price, int stock, int buyCnt, LocalDateTime createdAt, LocalDateTime updatedAt){
        validateName(name);
        validateDescription(description);
        validatePrice(price);
        validateStock(stock);

        Item item = new Item(id, memberId, createdAt);
        item.name = name;
        item.category = category;
        item.description = description;
        item.price = price;
        item.stock = stock;
        item.buyCnt = buyCnt;
        item.updatedAt = updatedAt;
        return item;
    }

    public void update(String newName, Category newCategory, String newDescription, int newPrice, int newStock) {
        validateName(newName);
        validateDescription(newDescription);
        validatePrice(newPrice);
        validateStock(newStock);

        name = newName;
        category = newCategory;
        description = newDescription;
        price = newPrice;
        stock = newStock;
    }

    public void release(int quantity){
        if(stock < quantity){
            throw new LackOfStockException(name, stock, quantity);
        }
        stock -= quantity;
    }

    public Long getId() {
        return id;
    }

    public Long getMemberId() {
        return memberId;
    }

    public String getName() {
        return name;
    }

    public Category getCategory() {
        return category;
    }

    public int getPrice() {
        return price;
    }

    public int getStock() {
        return stock;
    }

    public String getDescription() {
        return description;
    }

    public int getBuyCnt() {
        return buyCnt;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    private static void validateName(String name) {
        if(MIN_NAME > name.length() || name.length() > MAX_NAME){
            throw new ArgumentOutOfBoundException(NAME_FIELD_NAME, MIN_NAME, MAX_NAME, name.length());
        }
    }

    private static void validateDescription(String description) {
        if(description.length() > MAX_DESCRIPTION){
            throw new ArgumentOutOfBoundException(DESCRIPTION_FIELD_NAME, ZERO, MAX_DESCRIPTION, description.length());
        }
    }

    private static void validatePrice(int price) {
        if(price < MIN_PRICE){
            throw new ArgumentOutOfBoundException(PRICE_FIELD_NAME, MIN_PRICE, Integer.MAX_VALUE, price);
        }
    }

    private static void validateStock(int stock) {
        if(stock < MIN_STOCK){
            throw new ArgumentOutOfBoundException(STOCK_FIELD_NAME, MIN_PRICE, Integer.MAX_VALUE, stock);
        }
    }
}
