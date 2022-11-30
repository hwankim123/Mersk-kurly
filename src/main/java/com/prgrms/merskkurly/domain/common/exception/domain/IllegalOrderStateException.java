package com.prgrms.merskkurly.domain.common.exception.domain;

import com.prgrms.merskkurly.domain.order.entity.OrderStatus;

public class IllegalOrderStateException extends RuntimeException {
    private static enum OrderStatusInner{

    }
    private static final String messageFormatChangeStatus = "IllegalOrderStateException occurred when order status was trying to be changed. Status changing to %s is only possible when current status is %s";
    private static final String messageFormatUpdate = "IllegalOrderStateException occurred when order was trying to be updated. Updating order is not possible when current status is %s";

//    private String desireStatus;
//    private String currentStatus;

    public IllegalOrderStateException(String desireStatus, String currentStatus) {
        super(String.format(messageFormatChangeStatus, desireStatus, currentStatus));
//        this.currentStatus = currentStatus;
//        this.desireStatus = desireStatus;
    }

    public IllegalOrderStateException(String currentStatus){
        super(String.format(messageFormatUpdate, currentStatus));
//        this.currentStatus = currentStatus;
//        this.desireStatus = null;
    }
}
