package com.pragma.plazoleta.jpa;

import com.pragma.plazoleta.jpa.entity.RestaurantEntity;
import com.pragma.plazoleta.jpa.helper.AdapterOperations;
import com.pragma.plazoleta.model.restaurant.Restaurant;
import com.pragma.plazoleta.model.restaurant.gateways.RestaurantRepository;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;

@Repository
public class JPARepositoryAdapter extends AdapterOperations<Restaurant, RestaurantEntity, Long, JPARepository>
        implements RestaurantRepository {

    public JPARepositoryAdapter(JPARepository repository, ObjectMapper mapper) {
        super(repository, mapper, d -> mapper.map(d, Restaurant.class));
    }

    @Override
    public Restaurant saveRestaurant(Restaurant restaurant) {
        // super.save() ya mapea de Restaurant (Dominio) a RestaurantEntity (BD) automáticamente
        return super.save(restaurant);
    }
}