package com.moksem.moksembank.util.exceptions;

public class UserNotFoundException extends Exception{
    private static final String MESSAGE = "User not found";
    public UserNotFoundException(){
        super(MESSAGE);
    }
}
