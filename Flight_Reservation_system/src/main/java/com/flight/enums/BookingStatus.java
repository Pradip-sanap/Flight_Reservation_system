package com.flight.enums;

public enum BookingStatus {
    CANCELLED("CANCELLED"),
    PAYMENT_PENDING("PAYMENT_PENDING"),
    COMPLETED("COMPLETED");

    private final String value;

    BookingStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
