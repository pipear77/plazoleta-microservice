package com.pragma.plazoleta.model.restaurant.gateways;

public interface UserValidationService {
    boolean isOwnerValid(Long ownerId);
}