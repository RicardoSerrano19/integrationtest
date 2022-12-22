package com.rsr.integrationtest.exception;

public class DocumentNotFoundException extends RuntimeException{
    
    public DocumentNotFoundException(String message){
        super(message);
    }
}
