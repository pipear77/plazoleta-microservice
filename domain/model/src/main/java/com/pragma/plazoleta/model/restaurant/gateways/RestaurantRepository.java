package com.pragma.plazoleta.model.restaurant.gateways;
import com.pragma.plazoleta.model.restaurant.Restaurant;

public interface RestaurantRepository {
    Restaurant saveRestaurant(Restaurant restaurant);
}