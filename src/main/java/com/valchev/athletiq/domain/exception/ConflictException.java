package com.valchev.athletiq.domain.exception;

import java.io.Serial;

public class ConflictException extends UserFriendlyException {
    @Serial
    private static final long serialVersionUID = 1L;

    public ConflictException(String message) {
        super(message);
    }

    public ConflictException(String message, Throwable cause) {
        super(message, cause);
    }
}
