package com.valchev.athletiq.domain.exception;

import java.io.Serial;

public class UserFriendlyException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    public UserFriendlyException(String message) {
        super(message);
    }

    public UserFriendlyException(String message, Throwable cause) {
        super(message, cause);
    }

}
