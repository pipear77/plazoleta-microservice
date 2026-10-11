package com.pragma.plazoleta.usecase.dish.exception;

public class InvalidDishPriceException extends RuntimeException {
    public InvalidDishPriceException(String message) {
        super(message);
    }
}