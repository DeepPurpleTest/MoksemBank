package com.moksem.moksembank.util.exceptions;

public class UserNotFoundException extends Exception{
    private static final String MESSAGE = "error.user.not_found";
    public UserNotFoundException(){
        super(MESSAGE);
    }
}
