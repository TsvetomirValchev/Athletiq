package com.valchev.athletiq.domain.exception;

public class NoSuchUserException extends UserFriendlyException {
    public NoSuchUserException(String message) {
        super(message);
    }
}