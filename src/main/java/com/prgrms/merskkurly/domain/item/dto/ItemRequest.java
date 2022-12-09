package com.prgrms.merskkurly.domain.item.dto;

import com.prgrms.merskkurly.domain.item.entity.Category;

public class ItemRequest {

    public static class SearchForm {
        String keyword;
        Category category;

        public SearchForm() {
        }

        public SearchForm(String keyword, Category category) {
            this.keyword = keyword;
            this.category = category;
        }

        public String getKeyword() {
            return keyword;
        }

        public void setKeyword(String keyword) {
            this.keyword = keyword;
        }

        public Category getCategory() {
            return category;
        }

        public void setCategory(Category category) {
            this.category = category;
        }

    }

    public static class NewForm {
        private Long memberId;
        private String name;
        private Category category;
        private String description;
        private int price;
        private int stock;

        public NewForm() {
        }

        public NewForm(Long memberId, String name, Category category, String description, int price, int stock) {
            this.memberId = memberId;
            this.name = name;
            this.category = category;
            this.description = description;
            this.price = price;
            this.stock = stock;
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
    }

}
