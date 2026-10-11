package com.pragma.plazoleta.usecase.dish.exception;

public class NotRestaurantOwnerException extends RuntimeException {
    public NotRestaurantOwnerException(String message) {
        super(message);
    }
}