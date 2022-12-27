package com.rsr.integrationtest;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.rsr.integrationtest.exception.ErrorResponse;

/**
 * ErrorResponseUseCaseTest
 */
public class ErrorResponseUseCaseTest {

    private ErrorResponse errorResponse;

    @Test
    public void addValidationErrorToResponse(){
        // given
        errorResponse = new ErrorResponse(400, "Bad Request", "Validation Error", "/api");

        // when
        errorResponse.addValidationError("name", "Should not be empty");

        // then
        assertEquals(1, errorResponse.getErrors().size());
        assertEquals(false, errorResponse.getErrors().isEmpty());

    }

}