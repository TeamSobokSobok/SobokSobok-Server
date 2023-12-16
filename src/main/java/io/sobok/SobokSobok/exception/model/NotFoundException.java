package io.sobok.SobokSobok.exception.model;

import io.sobok.SobokSobok.exception.ErrorCode;

public class NotFoundException extends SobokException {

    public NotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }
}
