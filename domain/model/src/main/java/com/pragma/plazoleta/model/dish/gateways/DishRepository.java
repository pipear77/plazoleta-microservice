package com.pragma.plazoleta.model.dish.gateways;

import com.pragma.plazoleta.model.dish.Dish;
import java.util.Optional;

public interface DishRepository {
    Dish saveDish(Dish dish);
    Optional<Dish> findById(Long id);
}