package com.prgrms.merskkurly.domain.common.exception.domain;

public class ArgumentOutOfBoundException extends IllegalArgumentException {
    private static final String messageFormat = "ArgumentOutOfBoundException occurred when new instance was initialized. Field name %s should be between %d and %d. Input was %d.";
    private final String fieldName;
    private final int minBound;
    private final int maxBound;
    private final int argument;

    public ArgumentOutOfBoundException(String fieldName, int minBound, int maxBound, int argument) {
        super(String.format(messageFormat, fieldName, minBound, maxBound, argument));
        this.fieldName = fieldName;
        this.minBound = minBound;
        this.maxBound = maxBound;
        this.argument = argument;
    }

    public String getFieldName() {
        return fieldName;
    }

    public int getMinBound() {
        return minBound;
    }

    public int getMaxBound() {
        return maxBound;
    }

    public int getArgument() {
        return argument;
    }
}
