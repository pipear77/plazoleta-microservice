package com.pragma.plazoleta.jpa.repository;

import com.pragma.plazoleta.jpa.entity.DishEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DishJpaRepository extends JpaRepository<DishEntity, Long> {

    // Filtro 1: Sin categoría (trae todos los platos activos del restaurante)
    Page<DishEntity> findByIdRestauranteAndActivoTrue(Long idRestaurante, Pageable pageable);

    // Filtro 2: Con categoría (trae platos activos de una categoría específica del restaurante)
    Page<DishEntity> findByIdRestauranteAndIdCategoriaAndActivoTrue(Long idRestaurante, Long idCategoria, Pageable pageable);
}