package io.sobok.SobokSobok.exception;

import feign.FeignException;
import io.sobok.SobokSobok.common.dto.ApiResponse;
import io.sobok.SobokSobok.exception.model.SobokException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class ControllerExceptionAdvice {

    /**
     * 400 Bad Request
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    protected ResponseEntity<ApiResponse<Void>> handleHttpMessageNotReadableException(final HttpMessageNotReadableException exception) {
        return new ResponseEntity<>(
                ApiResponse.error(ErrorCode.INVALID_REQUEST_BODY),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    protected ResponseEntity<ApiResponse<Void>> handleMissingServletRequestParameterException(final MissingServletRequestParameterException exception) {
        return new ResponseEntity<>(
                ApiResponse.error(ErrorCode.NOT_EXIST_PARAMETER),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    protected ResponseEntity<ApiResponse<Void>> handleHttpRequestMethodNotSupportedException(final HttpRequestMethodNotSupportedException exception) {
        return new ResponseEntity<>(
                ApiResponse.error(ErrorCode.NOT_SUPPORTED_METHOD),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<ApiResponse<Void>> handleMethodArgumentNotValidException(final MethodArgumentNotValidException exception) {
        StringBuilder sb = new StringBuilder();
        for (FieldError error : exception.getBindingResult().getFieldErrors()) {
            sb.append(error.getField()).append(": ").append(error.getDefaultMessage()).append(" ");
        }

        return new ResponseEntity<>(
                ApiResponse.error(ErrorCode.BAD_REQUEST_EXCEPTION, sb.toString()),
                HttpStatus.BAD_REQUEST
        );
    }

    /**
     * external Error
     */
    @ExceptionHandler(FeignException.class)
    protected ResponseEntity<ApiResponse<Void>> handleFeignException(final FeignException exception) {
        return new ResponseEntity<>(
                ApiResponse.error(ErrorCode.INVALID_EXTERNAL_REQUEST_DATA),
                HttpStatus.BAD_REQUEST
        );
    }

    /**
     * Sobok custom Error
     */
    @ExceptionHandler(SobokException.class)
    protected ResponseEntity<ApiResponse<Void>> handleSobokException(final SobokException exception) {
        return new ResponseEntity<>(
                ApiResponse.error(exception.getErrorCode()),
                exception.getErrorCode().getCode()
        );
    }
}
