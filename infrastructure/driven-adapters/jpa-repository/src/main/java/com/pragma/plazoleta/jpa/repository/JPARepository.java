package com.pragma.plazoleta.jpa.repository;

import com.pragma.plazoleta.jpa.entity.RestaurantEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;

public interface JPARepository extends JpaRepository<RestaurantEntity, Long>, QueryByExampleExecutor<RestaurantEntity> {
}