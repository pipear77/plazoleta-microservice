package com.pragma.plazoleta.api;

import com.pragma.plazoleta.api.dto.RestaurantRequestDto;
import com.pragma.plazoleta.model.restaurant.Restaurant;
import com.pragma.plazoleta.usecase.restaurant.RestaurantUseCase;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class RestaurantControllerTest {

    @Mock
    private RestaurantUseCase restaurantUseCase;

    @InjectMocks
    private RestaurantController restaurantController;

    @Test
    void createRestaurant_ValidRequest_ReturnsCreatedStatus() {
        // Arrange
        RestaurantRequestDto request = new RestaurantRequestDto(
                "El Buen Sabor",
                "900123456",
                "Calle Falsa 123",
                "+573001234567",
                "https://mi-logo.com/logo.png",
                1L
        );

        // Act
        ResponseEntity<Void> response = restaurantController.createRestaurant(request);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        // Verificamos que el controlador efectivamente llamó al caso de uso
        verify(restaurantUseCase).createRestaurant(any(Restaurant.class));
    }
}