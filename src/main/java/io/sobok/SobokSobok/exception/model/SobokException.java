package io.sobok.SobokSobok.exception.model;

import io.sobok.SobokSobok.exception.ErrorCode;
import lombok.Getter;

@Getter
public class SobokException extends RuntimeException {

    private final ErrorCode errorCode;

    public SobokException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
