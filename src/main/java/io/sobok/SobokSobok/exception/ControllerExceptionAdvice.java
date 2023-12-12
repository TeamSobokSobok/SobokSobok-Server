package io.sobok.SobokSobok.exception;

import io.sobok.SobokSobok.common.dto.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class ControllerExceptionAdvice {

    /**
     * 400 Bad Request
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    protected ResponseEntity<ApiResponse<Void>> handleHttpMessageNotReadableException(final HttpMessageNotReadableException error) {
        return new ResponseEntity<>(
                ApiResponse.error(ErrorCode.INVALID_REQUEST_BODY),
                HttpStatus.BAD_REQUEST
        );
    }
}
