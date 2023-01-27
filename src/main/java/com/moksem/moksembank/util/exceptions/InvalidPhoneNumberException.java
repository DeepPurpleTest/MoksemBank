package com.moksem.moksembank.util.exceptions;

public class InvalidPhoneNumberException extends Exception{
    private final static String MESSAGE = "Invalid phone number format";
    public InvalidPhoneNumberException() {
        super(MESSAGE);
    }
}
