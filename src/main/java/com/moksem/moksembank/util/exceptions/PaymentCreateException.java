package com.moksem.moksembank.util.exceptions;

public class PaymentCreateException extends Exception {
    private final static String MESSAGE = "Card sender and receiver is identical";
    public PaymentCreateException() {
        super(MESSAGE);
    }
}
