package com.pragma.plazoleta.jpa.adapter;

import com.pragma.plazoleta.jpa.entity.RestaurantEntity;
import com.pragma.plazoleta.jpa.helper.AdapterOperations;
import com.pragma.plazoleta.jpa.repository.JPARepository;
import com.pragma.plazoleta.model.restaurant.Restaurant;
import com.pragma.plazoleta.model.restaurant.dto.RestaurantPaginationDto;
import com.pragma.plazoleta.model.restaurant.gateways.RestaurantRepository;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class JPARepositoryAdapter
        extends AdapterOperations<Restaurant, RestaurantEntity, Long, JPARepository>
        implements RestaurantRepository {

    public JPARepositoryAdapter(JPARepository repository, ObjectMapper mapper) {
        super(repository, mapper, d -> mapper.map(d, Restaurant.class));
    }

    @Override
    public Restaurant saveRestaurant(Restaurant restaurant) {
        return super.save(restaurant);
    }

    @Override
    public List<RestaurantPaginationDto> findAllPaginated(int page, int size) {
        // Configuramos la página, el tamaño y forzamos el orden ascendente (A-Z) por el campo "nombre"
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "nombre"));

        // Buscamos en base de datos devolviendo una página
        Page<RestaurantEntity> entityPage = repository.findAll(pageRequest);

        // Transformamos las entidades al DTO simplificado (Solo nombre y urlLogo)
        return entityPage.stream()
                .map(entity -> RestaurantPaginationDto.builder()
                        .nombre(entity.getNombre())
                        .urlLogo(entity.getUrlLogo())
                        .build())
                .toList();
    }
}