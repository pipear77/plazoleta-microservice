package com.pragma.plazoleta.usecase.restaurant;

import com.pragma.plazoleta.model.restaurant.Restaurant;
import com.pragma.plazoleta.model.restaurant.gateways.RestaurantRepository;
import com.pragma.plazoleta.model.restaurant.gateways.UserValidationService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class RestaurantUseCase {

    private final RestaurantRepository restaurantRepository;
    private final UserValidationService userValidationService;

    public Restaurant createRestaurant(Restaurant restaurant) {
        validateRestaurantRules(restaurant);

        // Aquí usamos la interfaz. La implementación en infraestructura hará el HTTP call real.
        if (!userValidationService.isOwnerValid(restaurant.getIdPropietario())) {
            throw new IllegalArgumentException("El ID proporcionado no pertenece a un Propietario válido.");
        }

        return restaurantRepository.saveRestaurant(restaurant);
    }

    private void validateRestaurantRules(Restaurant restaurant) {
        if (restaurant.getNombre().matches("^[0-9]+$")) {
            throw new IllegalArgumentException("El nombre del restaurante no puede consistir únicamente de números.");
        }
        if (!restaurant.getNit().matches("^\\d+$")) {
            throw new IllegalArgumentException("El NIT debe ser estrictamente numérico.");
        }
        if (!restaurant.getTelefono().matches("^\\+?\\d{1,12}$")) {
            throw new IllegalArgumentException("El teléfono debe ser numérico, máximo 13 caracteres y puede incluir '+'.");
        }
    }
}