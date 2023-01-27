package com.moksem.moksembank.util.exceptions;

public class InvalidAmountException extends Exception{
    private static final String MESSAGE = "Invalid amount value format";
    public InvalidAmountException(){
        super(MESSAGE);
    }
}
