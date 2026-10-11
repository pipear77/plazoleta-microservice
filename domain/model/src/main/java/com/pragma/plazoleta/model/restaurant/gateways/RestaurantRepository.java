package com.pragma.plazoleta.model.restaurant.gateways;

import com.pragma.plazoleta.model.restaurant.Restaurant;
import java.util.Optional;

public interface RestaurantRepository {
    Restaurant saveRestaurant(Restaurant restaurant);
    Restaurant findById(Long id);
}