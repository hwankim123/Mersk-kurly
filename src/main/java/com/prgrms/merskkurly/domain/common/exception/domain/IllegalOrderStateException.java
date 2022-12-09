package com.prgrms.merskkurly.domain.common.exception.domain;

public class IllegalOrderStateException extends RuntimeException {

    private static final String messageFormatChangeStatus = "IllegalOrderStateException occurred when order status was trying to be changed. Status changing to %s is only possible when current status is %s";
    private static final String messageFormatUpdate = "IllegalOrderStateException occurred when order was trying to be updated. Updating order is not possible when current status is %s";

    public IllegalOrderStateException(String desireStatus, String currentStatus) {
        super(String.format(messageFormatChangeStatus, desireStatus, currentStatus));

    }

    public IllegalOrderStateException(String currentStatus) {
        super(String.format(messageFormatUpdate, currentStatus));
    }
}
