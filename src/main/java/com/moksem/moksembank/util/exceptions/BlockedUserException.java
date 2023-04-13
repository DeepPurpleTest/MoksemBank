package com.moksem.moksembank.util.exceptions;

public class BlockedUserException extends Exception{
    private static final String MESSAGE = "error.blocked_user";
    public BlockedUserException() {
        super(MESSAGE);
    }
}
