package io.sobok.SobokSobok.exception.model;

import io.sobok.SobokSobok.exception.ErrorCode;

public class UnauthorizedException extends SobokException {

    public UnauthorizedException(ErrorCode errorCode) {
        super(errorCode);
    }
}
