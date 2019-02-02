package com.marincek.sympis.domain.exceptions;

public class ExistingUrlException extends RuntimeException {

    public ExistingUrlException() {
        super("Link is already added");
    }
}
