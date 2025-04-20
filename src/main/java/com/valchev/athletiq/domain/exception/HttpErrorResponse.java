package com.valchev.athletiq.domain.exception;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class HttpErrorResponse {
    private final int statusCode;
    private final String error;
    private final String message;
}
