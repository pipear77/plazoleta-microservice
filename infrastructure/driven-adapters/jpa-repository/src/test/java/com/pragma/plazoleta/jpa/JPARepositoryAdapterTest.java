package com.pragma.plazoleta.jpa;

import com.pragma.plazoleta.jpa.entity.RestaurantEntity;
import com.pragma.plazoleta.model.restaurant.Restaurant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.reactivecommons.utils.ObjectMapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JPARepositoryAdapterTest {

    @Mock
    private JPARepository repository;

    @Mock
    private ObjectMapper mapper;

    private JPARepositoryAdapter adapter;

    @BeforeEach
    void setUp() {
        adapter = new JPARepositoryAdapter(repository, mapper);
    }

    @Test
    void saveRestaurant_ReturnsSavedRestaurant() {
        // Arrange
        Restaurant restaurantDomain = new Restaurant(1L, "El Buen Sabor", "123", "Calle 1", "300", "url", 1L);
        RestaurantEntity restaurantEntity = new RestaurantEntity();
        restaurantEntity.setId(1L);

        // Simulamos la conversión de Dominio a Entidad que hace AdapterOperations
        when(mapper.map(restaurantDomain, RestaurantEntity.class)).thenReturn(restaurantEntity);
        // Simulamos el guardado de Spring Data JPA
        when(repository.save(restaurantEntity)).thenReturn(restaurantEntity);
        // Simulamos la conversión de vuelta de Entidad a Dominio
        when(mapper.map(restaurantEntity, Restaurant.class)).thenReturn(restaurantDomain);

        // Act
        Restaurant result = adapter.saveRestaurant(restaurantDomain);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
    }
}