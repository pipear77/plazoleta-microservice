package com.pragma.plazoleta.jpa.adapter;

import com.pragma.plazoleta.jpa.entity.DishEntity;
import com.pragma.plazoleta.jpa.mapper.DishEntityMapper;
import com.pragma.plazoleta.jpa.repository.DishJpaRepository;
import com.pragma.plazoleta.model.dish.Dish;
import com.pragma.plazoleta.model.dish.gateways.DishRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class DishRepositoryAdapter implements DishRepository {

    private final DishJpaRepository dishJpaRepository;
    private final DishEntityMapper mapper;

    @Override
    public Dish saveDish(Dish dish) {
        DishEntity entity = mapper.toEntity(dish);
        DishEntity saved = dishJpaRepository.save(entity);
        return mapper.toDomain(saved);
    }

    @Override
    public Dish findById(Long id) {
        return dishJpaRepository.findById(id).map(mapper::toDomain).orElse(null);
    }
}