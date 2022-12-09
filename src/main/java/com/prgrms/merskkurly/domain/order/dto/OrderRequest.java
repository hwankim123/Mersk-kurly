package com.prgrms.merskkurly.domain.order.dto;

public class OrderRequest {

    public static class OrderForm {
        String address;

        public OrderForm() {
        }

        public OrderForm(String address) {
            this.address = address;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }
    }
}
