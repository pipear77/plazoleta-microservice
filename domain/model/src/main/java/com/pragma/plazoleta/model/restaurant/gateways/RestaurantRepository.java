package com.pragma.plazoleta.model.restaurant.gateways;

import com.pragma.plazoleta.model.restaurant.Restaurant;
import com.pragma.plazoleta.model.restaurant.dto.RestaurantPaginationDto;

import java.util.List;

public interface RestaurantRepository {
    Restaurant saveRestaurant(Restaurant restaurant);
    Restaurant findById(Long id);

    // MÉTODO PARA HU09: Paginación ordenada alfabéticamente
    List<RestaurantPaginationDto> findAllPaginated(int page, int size);
}