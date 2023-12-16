package io.sobok.SobokSobok.exception.model;

import io.sobok.SobokSobok.exception.ErrorCode;

public class ConflictException extends SobokException {

    public ConflictException(ErrorCode errorCode) {
        super(errorCode);
    }
}
