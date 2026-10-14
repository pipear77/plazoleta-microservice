package com.pragma.plazoleta.model.dish.gateways;

import com.pragma.plazoleta.model.dish.Dish;

import java.util.List;
import java.util.Optional;

public interface DishRepository {
    Dish saveDish(Dish dish);
    Dish findById(Long id);
    List<Dish> findDishesByRestaurantAndCategory(Long idRestaurante, Long idCategoria, int page, int size);
}