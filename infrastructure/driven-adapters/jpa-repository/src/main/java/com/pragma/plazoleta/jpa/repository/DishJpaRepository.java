package com.pragma.plazoleta.jpa.repository;

import com.pragma.plazoleta.jpa.entity.DishEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DishJpaRepository extends JpaRepository<DishEntity, Long> {
}