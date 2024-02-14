package io.sobok.SobokSobok.exception.model;

import io.sobok.SobokSobok.exception.ErrorCode;

public class ForbiddenException extends SobokException {

    public ForbiddenException(ErrorCode errorCode) {
        super(errorCode);
    }
}
