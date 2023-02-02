package com.moksem.moksembank.util.exceptions;

public class LoginAlreadyTakenException extends Exception{
    public LoginAlreadyTakenException(String message) {
        super(message);
    }
}
