package com.pragma.plazoleta.usecase.dish;

import com.pragma.plazoleta.model.dish.Dish;
import com.pragma.plazoleta.model.dish.gateways.DishRepository;
import com.pragma.plazoleta.model.restaurant.Restaurant;
import com.pragma.plazoleta.model.restaurant.gateways.RestaurantRepository;
import com.pragma.plazoleta.usecase.dish.exception.DishNotFoundException;
import com.pragma.plazoleta.usecase.dish.exception.InvalidDishPriceException;
import com.pragma.plazoleta.usecase.dish.exception.NotRestaurantOwnerException;
import com.pragma.plazoleta.usecase.dish.exception.RestaurantNotFoundException;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DishUseCase {

    private final DishRepository dishRepository;
    private final RestaurantRepository restaurantRepository;

    // HU03 - Crear Plato
    public Dish createDish(Dish dish, Long idPropietario) {
        Restaurant restaurant = restaurantRepository.findById(dish.getIdRestaurante());

        // Cambio clave: Chequeo manual de null en lugar de orElseThrow
        if (restaurant == null) {
            throw new RestaurantNotFoundException("El restaurante no existe");
        }

        if (!restaurant.getIdPropietario().equals(idPropietario)) {
            throw new NotRestaurantOwnerException("El usuario no es el propietario del restaurante");
        }

        validatePrice(dish.getPrecio());

        Dish newDish = dish.toBuilder()
                .activo(true)
                .build();

        return dishRepository.saveDish(newDish);
    }

    private void validatePrice(Integer precio) {
        if (precio == null || precio <= 0) {
            throw new InvalidDishPriceException("El precio debe ser un número entero mayor a 0");
        }
    }
}