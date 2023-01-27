package com.moksem.moksembank.util.exceptions;


public class InvalidCardException extends Exception{
    private final static String MESSAGE = "Invalid card number";
    public InvalidCardException() {
        super(MESSAGE);
    }
    public InvalidCardException(String message) {
        super(message);
    }
}
