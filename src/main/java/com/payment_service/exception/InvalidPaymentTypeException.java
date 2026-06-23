package com.payment_service.exception;

public class InvalidPaymentTypeException extends RuntimeException{
    public InvalidPaymentTypeException(String message){
        super(message);
    }
}
