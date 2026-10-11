package com.pragma.plazoleta.jpa.adapter;

import com.pragma.plazoleta.jpa.entity.DishEntity;
import com.pragma.plazoleta.jpa.mapper.DishEntityMapper;
import com.pragma.plazoleta.jpa.repository.DishJpaRepository;
import com.pragma.plazoleta.model.dish.Dish;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DishRepositoryAdapterTest {

    @Mock
    private DishJpaRepository dishJpaRepository;

    private DishRepositoryAdapter adapter;

    @BeforeEach
    void setUp() {
        adapter = new DishRepositoryAdapter(dishJpaRepository, new DishEntityMapper());
    }

    @Test
    void saveDish_success_shouldMapAndPersist() {
        Dish input = Dish.builder()
                .nombre("Bandeja Paisa")
                .precio(25000)
                .descripcion("Deliciosa")
                .urlImagen("http://img")
                .idCategoria(1L)
                .idRestaurante(1L)
                .activo(true)
                .build();

        DishEntity savedEntity = DishEntity.builder()
                .id(1L)
                .nombre("Bandeja Paisa")
                .precio(25000)
                .descripcion("Deliciosa")
                .urlImagen("http://img")
                .idCategoria(1L)
                .idRestaurante(1L)
                .activo(true)
                .build();

        when(dishJpaRepository.save(any(DishEntity.class))).thenReturn(savedEntity);

        Dish result = adapter.saveDish(input);

        assertEquals(1L, result.getId());
        assertEquals("Bandeja Paisa", result.getNombre());
        assertTrue(result.getActivo());
        verify(dishJpaRepository).save(any(DishEntity.class));
    }

    @Test
    void findById_whenExists_shouldReturnMappedDish() {
        DishEntity savedEntity = DishEntity.builder()
                .id(1L)
                .nombre("Bandeja Paisa")
                .precio(25000)
                .activo(true)
                .idRestaurante(1L)
                .build();

        when(dishJpaRepository.findById(1L)).thenReturn(Optional.of(savedEntity));

        // Ahora recibimos el Dish directo
        Dish result = adapter.findById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Bandeja Paisa", result.getNombre());
    }

    @Test
    void findById_whenNotExists_shouldReturnNull() {
        when(dishJpaRepository.findById(99L)).thenReturn(Optional.empty());

        // Ahora evaluamos que retorne null directamente
        Dish result = adapter.findById(99L);

        assertNull(result);
    }

    @Test
    void mapper_toEntity_whenNull_shouldReturnNull() {
        assertNull(new DishEntityMapper().toEntity(null));
    }

    @Test
    void mapper_toDomain_whenNull_shouldReturnNull() {
        assertNull(new DishEntityMapper().toDomain(null));
    }
}