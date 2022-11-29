package com.prgrms.merskkurly.domain.common.exception.domain;

import com.prgrms.merskkurly.domain.order.entity.OrderStatus;

public class IllegalOrderStateException extends RuntimeException {
    private static final String messageFormat = "IllegalOrderStateException occurred when order status was trying to be changed. Status changing to %s is not possible when current status is %s";

    private String desireStatus;
    private String currentStatus;

    public IllegalOrderStateException(String desireStatus, String currentStatus) {
        super(String.format(messageFormat, desireStatus, currentStatus));
        this.currentStatus = currentStatus;
        this.desireStatus = desireStatus;
    }
}
