package com.pragma.plazoleta.api.restaurant;

import com.pragma.plazoleta.api.restaurant.dto.RestaurantRequestDto;
import com.pragma.plazoleta.model.restaurant.Restaurant;
import com.pragma.plazoleta.usecase.restaurant.RestaurantUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

@RestController
@RequestMapping(value = "/api/v1/restaurants")
@RequiredArgsConstructor
public class RestaurantController {

    private final RestaurantUseCase restaurantUseCase;

    @PostMapping
    public ResponseEntity<Void> createRestaurant(@Valid @RequestBody RestaurantRequestDto request) {
        Restaurant restaurant = Restaurant.builder()
                .nombre(request.nombre())
                .nit(request.nit())
                .direccion(request.direccion())
                .telefono(request.telefono())
                .urlLogo(request.urlLogo())
                .idPropietario(request.idPropietario())
                .build();

        restaurantUseCase.createRestaurant(restaurant);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}