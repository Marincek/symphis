package com.marincek.sympis.domain.exceptions;

public class UnknownUserException extends RuntimeException {

    public UnknownUserException() {
        super("Unknown User");
    }
}
