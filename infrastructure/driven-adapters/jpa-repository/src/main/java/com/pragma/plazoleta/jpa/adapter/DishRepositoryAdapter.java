package com.pragma.plazoleta.jpa.adapter;

import com.pragma.plazoleta.jpa.entity.DishEntity;
import com.pragma.plazoleta.jpa.mapper.DishEntityMapper;
import com.pragma.plazoleta.jpa.repository.DishJpaRepository;
import com.pragma.plazoleta.model.dish.Dish;
import com.pragma.plazoleta.model.dish.gateways.DishRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import java.util.List;
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

    @Override
    public List<Dish> findDishesByRestaurantAndCategory(Long idRestaurante, Long idCategoria, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<DishEntity> entityPage;

        // Evaluamos si el cliente envió una categoría para filtrar[cite: 16]
        if (idCategoria != null) {
            entityPage = dishJpaRepository.findByIdRestauranteAndIdCategoriaAndActivoTrue(idRestaurante, idCategoria, pageRequest);
        } else {
            entityPage = dishJpaRepository.findByIdRestauranteAndActivoTrue(idRestaurante, pageRequest);
        }

        return entityPage.stream().map(mapper::toDomain).toList();
    }
}