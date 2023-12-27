package io.sobok.SobokSobok.exception.model;

import io.sobok.SobokSobok.exception.ErrorCode;

public class BadRequestException extends SobokException {

    public BadRequestException(ErrorCode errorCode) {
        super(errorCode);
    }
}
