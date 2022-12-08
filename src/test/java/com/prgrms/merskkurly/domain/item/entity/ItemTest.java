package com.prgrms.merskkurly.domain.item.entity;

import com.prgrms.merskkurly.domain.common.exception.domain.ArgumentOutOfBoundException;
import com.prgrms.merskkurly.domain.common.exception.domain.LackOfStockException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class ItemTest {

    /**
     * 새로운 상품 객체를 생성할 수 있다.
     *  - validation 실패 시 새로운 상품 생성 실패
     * 기존 데이터를 통해 상품 객체를 생성할 수 있다.
     * 상품을 수정할 수 있다.
     *  - name, category, description, price을 수정할 수 있고, updatedAt는 자동으로 수정된다.
     */

    @ParameterizedTest
    @EnumSource(Category.class)
    @DisplayName("새로운 상품 객체를 생성할 수 있습니다")
    void newItem(Category category){
        //given
        String name = "name";
        String description = "description";
        int price = 10000;
        int stock = 1000;

        //when
        Item item = Item.newInstance(0L, name, category, description, price, stock);

        //then
        assertThat(item.getName()).isEqualTo(name);
        assertThat(item.getCategory()).isEqualTo(category);
        assertThat(item.getDescription()).isEqualTo(description);
        assertThat(item.getPrice()).isEqualTo(price);
    }

    @ParameterizedTest
    @EnumSource(Category.class)
    @DisplayName("기존 데이터를 통해 상품 객체를 생성할 수 있습니다")
    void getItem(Category categoryParameter) {
        //given
        Item item = newItemGenerator(categoryParameter);
        Long id = item.getId();
        Long memberId = item.getMemberId();
        String name = item.getName();
        Category category = item.getCategory();
        String description = item.getDescription();
        int price = item.getPrice();
        int stock = item.getStock();
        int buyCnt = item.getBuyCnt();
        LocalDateTime createdAt = item.getCreatedAt();
        LocalDateTime updatedAt = item.getUpdatedAt();

        //when
        Item getItem = Item.getInstance(id, memberId, name, category, description, price, stock, buyCnt, createdAt, updatedAt);

        //then
        assertThat(getItem.getId()).isEqualTo(id);
        assertThat(getItem.getMemberId()).isEqualTo(memberId);
        assertThat(getItem.getName()).isEqualTo(name);
        assertThat(getItem.getCategory()).isEqualTo(category);
        assertThat(getItem.getDescription()).isEqualTo(description);
        assertThat(getItem.getPrice()).isEqualTo(price);
        assertThat(getItem.getBuyCnt()).isEqualTo(buyCnt);
        assertThat(getItem.getCreatedAt()).isEqualTo(createdAt);
        assertThat(getItem.getUpdatedAt()).isEqualTo(updatedAt);
    }

    private Item newItemGenerator(Category category){
        String name = "name";
        String description = "description";
        int price = 10000;
        int stock = 1000;
        return Item.newInstance(0L, name, category, description, price, stock);
    }

    @Test
    @DisplayName("상품 정보를 수정할 수 있습니다")
    void updateItem() throws InterruptedException {
        //given
        Category beforeCategory = Category.DRINK;
        Item item = newItemGenerator(beforeCategory);
        LocalDateTime beforeUpdatedAt = item.getUpdatedAt();

        //when
        Thread.sleep(100);

        String newName = "name";
        Category newCategory = Category.MEAT;
        String newDescription = "newDescription";
        int newPrice = 10;
        int newStock = 1;
        item.update(newName, newCategory, newDescription, newPrice, newStock);

        //then
        assertThat(item.getName()).isEqualTo(newName);
        assertThat(item.getCategory()).isEqualTo(newCategory);
        assertThat(item.getDescription()).isEqualTo(newDescription);
        assertThat(item.getPrice()).isEqualTo(newPrice);
        assertThat(item.getStock()).isEqualTo(newStock);
        assertThat(item.getUpdatedAt()).isEqualTo(beforeUpdatedAt);
    }

    @Test
    @DisplayName("이름이 50글자 이내가 아니면 신규 아이템 생성에 실패합니다")
    void newItemFailureNameLength(){
        //given
        String wrongName = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa";
        Category category = Category.DRINK;
        String description = "description";
        int price = 10000;
        int stock = 1000;

        //when & then
        assertThrows(ArgumentOutOfBoundException.class, () -> Item.newInstance(0L, wrongName, category, description, price, stock));
    }

    @Test
    @DisplayName("설명이 250글자 이내가 아니면 신규 아이템 생성에 실패합니다")
    void newItemFailureDescriptionLength(){
        //given
        String name = "name";
        Category category = Category.DRINK;
        String tooLongDescription = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa" +
                "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa" +
                "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa";
        int price = 10000;
        int stock = 1000;

        //when & then
        assertThrows(ArgumentOutOfBoundException.class, () -> Item.newInstance(0L, name, category, tooLongDescription, price, stock));
    }

    @ParameterizedTest
    @ValueSource(ints = {-1000, 0})
    @DisplayName("가격이 양수가 아니면 신규 아이템 생성에 실패합니다")
    void newItemFailurePriceNegative(int wrongPrice){
        //given
        String name = "name";
        Category category = Category.DRINK;
        String tooLongDescription = "description";
        int stock = 1000;

        //when & then
        assertThrows(ArgumentOutOfBoundException.class, () -> Item.newInstance(0L, name, category, tooLongDescription, wrongPrice, stock));
    }

    @Test
    @DisplayName("상품의 재고보다 더 많은 수량으로 출고 요청을 하면 출고에 실패합니다")
    void orderItemFailureLackOfStock(){
        //given
        Item item = newItemGenerator(Category.DRINK);

        //when
        int quantityRequested = item.getStock() + 100;

        // then
        assertThrows(LackOfStockException.class, () -> item.release(quantityRequested));
    }
}