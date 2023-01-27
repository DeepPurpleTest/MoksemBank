package com.moksem.moksembank.util.exceptions;

public class TransactionException extends Exception{
    private final static String MESSAGE = "Transaction is failed";
    public TransactionException() {
        super(MESSAGE);
    }
}
