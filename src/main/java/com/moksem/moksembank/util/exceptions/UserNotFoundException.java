package com.moksem.moksembank.util.exceptions;

import java.security.MessageDigest;

public class UserNotFoundException extends Exception{
    private final static String MESSAGE = "User not found";
    public UserNotFoundException(){
        super(MESSAGE);
    }
    public UserNotFoundException(String message){
        super(message);
    }
}
