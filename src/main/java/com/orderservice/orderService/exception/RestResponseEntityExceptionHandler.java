package com.orderservice.orderService.exception;
import com.orderservice.orderService.external.response.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorResponse> handleCustomException(CustomException exception){
        return new ResponseEntity<>(new ErrorResponse().builder()
                .message(exception.getMessage())
                .errorCode(exception.getErrorCode()).build(), HttpStatus.valueOf(exception.getStatus()));
    }
}
