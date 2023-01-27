package com.moksem.moksembank.util.exceptions;

public class InvalidLoginOrPasswordException extends Exception{
    private static  final  String MESSAGE = "Invalid Login or password";
    public InvalidLoginOrPasswordException() {
        super(MESSAGE);
    }
}
