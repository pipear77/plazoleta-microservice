package com.pragma.plazoleta.jpa;

import com.pragma.plazoleta.jpa.entity.RestaurantEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;

public interface JPARepository extends CrudRepository<RestaurantEntity, Long>, QueryByExampleExecutor<RestaurantEntity> {
}