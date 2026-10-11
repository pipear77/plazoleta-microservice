package com.pragma.plazoleta.jpa.mapper;

import com.pragma.plazoleta.jpa.entity.DishEntity;
import com.pragma.plazoleta.model.dish.Dish;
import org.springframework.stereotype.Component;

@Component
public class DishEntityMapper {

    public DishEntity toEntity(Dish dish) {
        if (dish == null) {
            return null;
        }
        return DishEntity.builder()
                .id(dish.getId())
                .nombre(dish.getNombre())
                .precio(dish.getPrecio())
                .descripcion(dish.getDescripcion())
                .urlImagen(dish.getUrlImagen())
                .idCategoria(dish.getIdCategoria())
                .idRestaurante(dish.getIdRestaurante())
                .activo(dish.getActivo())
                .build();
    }

    public Dish toDomain(DishEntity entity) {
        if (entity == null) {
            return null;
        }
        return Dish.builder()
                .id(entity.getId())
                .nombre(entity.getNombre())
                .precio(entity.getPrecio())
                .descripcion(entity.getDescripcion())
                .urlImagen(entity.getUrlImagen())
                .idCategoria(entity.getIdCategoria())
                .idRestaurante(entity.getIdRestaurante())
                .activo(entity.getActivo())
                .build();
    }
}