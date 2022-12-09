package com.prgrms.merskkurly.domain.item.dto;

import com.prgrms.merskkurly.domain.item.entity.Category;
import com.prgrms.merskkurly.domain.item.entity.Item;

import java.time.LocalDateTime;

public class ItemResponse {

    public static class Shortcuts {

        private Long id;
        private String name;
        private int price;
        private int buyCnt;

        public Shortcuts(Long id, String name, int price, int buyCnt) {
            this.id = id;
            this.name = name;
            this.price = price;
            this.buyCnt = buyCnt;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getPrice() {
            return price;
        }

        public void setPrice(int price) {
            this.price = price;
        }

        public int getBuyCnt() {
            return buyCnt;
        }

        public void setBuyCnt(int buyCnt) {
            this.buyCnt = buyCnt;
        }
    }

    public static class Details {
        private Long id;
        private Long memberId;
        private String name;
        private Category category;
        private String description;
        private int price;
        private int stock;
        private int buyCnt;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public Details() {
        }

        public static Details from(Item item) {
            Details details = new Details();
            details.id = item.getId();
            details.memberId = item.getMemberId();
            details.name = item.getName();
            details.category = item.getCategory();
            details.description = item.getDescription();
            details.price = item.getPrice();
            details.stock = item.getStock();
            details.buyCnt = item.getBuyCnt();
            details.createdAt = item.getCreatedAt();
            details.updatedAt = item.getUpdatedAt();

            return details;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public Long getMemberId() {
            return memberId;
        }

        public void setMemberId(Long memberId) {
            this.memberId = memberId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Category getCategory() {
            return category;
        }

        public void setCategory(Category category) {
            this.category = category;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public int getPrice() {
            return price;
        }

        public void setPrice(int price) {
            this.price = price;
        }

        public int getStock() {
            return stock;
        }

        public void setStock(int stock) {
            this.stock = stock;
        }

        public int getBuyCnt() {
            return buyCnt;
        }

        public void setBuyCnt(int buyCnt) {
            this.buyCnt = buyCnt;
        }

        public LocalDateTime getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
        }

        public LocalDateTime getUpdatedAt() {
            return updatedAt;
        }

        public void setUpdatedAt(LocalDateTime updatedAt) {
            this.updatedAt = updatedAt;
        }
    }

    public static class UpdateForm {
        public Long id;
        public String name;
        public Category category;
        public String description;
        public int price;
        public int stock;

        public UpdateForm() {
        }

        public UpdateForm(String name, Category category, String description, int price, int stock) {
            this.name = name;
            this.category = category;
            this.description = description;
            this.price = price;
            this.stock = stock;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Category getCategory() {
            return category;
        }

        public void setCategory(Category category) {
            this.category = category;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public int getPrice() {
            return price;
        }

        public void setPrice(int price) {
            this.price = price;
        }

        public int getStock() {
            return stock;
        }

        public void setStock(int stock) {
            this.stock = stock;
        }
    }
}
