package com.rsr.integrationtest.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;

@ControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(DocumentNotFoundException.class)
    public ResponseEntity<ErrorResponse> documentNotFoundExceptionHandler(DocumentNotFoundException ex, ServletWebRequest req){
        return buildErrorResponse(ex, HttpStatus.NOT_FOUND, req);
    }

    private ResponseEntity<ErrorResponse> buildErrorResponse(Exception ex, HttpStatus httpStatus, ServletWebRequest req){
        ErrorResponse errorResponse = new ErrorResponse(
            httpStatus.value(), 
            httpStatus.getReasonPhrase(), 
            ex.getMessage(), 
            req.getRequest().getRequestURI()
        );

        return ResponseEntity.status(httpStatus).body(errorResponse);
    }
}
