package com.moksem.moksembank.util.exceptions;

public class BlockedUserException extends Exception{
    private static final String MESSAGE = "This account is blocked";
    public BlockedUserException() {
        super(MESSAGE);
    }
}
