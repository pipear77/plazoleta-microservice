package com.pragma.plazoleta.usecase.dish;

import com.pragma.plazoleta.model.dish.Dish;
import com.pragma.plazoleta.model.dish.gateways.DishRepository;
import com.pragma.plazoleta.model.restaurant.Restaurant;
import com.pragma.plazoleta.model.restaurant.gateways.RestaurantRepository;
import com.pragma.plazoleta.usecase.dish.exception.DishNotFoundException;
import com.pragma.plazoleta.usecase.dish.exception.InvalidDishPriceException;
import com.pragma.plazoleta.usecase.dish.exception.NotRestaurantOwnerException;
import com.pragma.plazoleta.usecase.dish.exception.RestaurantNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DishUseCaseTest {

    @Mock
    private DishRepository dishRepository;

    @Mock
    private RestaurantRepository restaurantRepository;

    @InjectMocks
    private DishUseCase dishUseCase;

    private Restaurant restaurant;

    @BeforeEach
    void setUp() {
        restaurant = Restaurant.builder()
                .id(1L)
                .nombre("Restaurante Test")
                .nit("123456")
                .direccion("Calle 1")
                .telefono("+573005698325")
                .urlLogo("http://logo")
                .idPropietario(10L)
                .build();
    }

    private Dish buildDish() {
        return Dish.builder()
                .nombre("Bandeja Paisa")
                .precio(25000)
                .descripcion("Plato típico")
                .urlImagen("http://imagen")
                .idCategoria(1L)
                .idRestaurante(1L)
                .build();
    }

    // ==================== HU03 createDish ====================

    @Test
    void createDish_success_shouldSaveDishWithActiveTrue() {
        Dish dish = buildDish();
        when(restaurantRepository.findById(1L)).thenReturn(restaurant);
        when(dishRepository.saveDish(any(Dish.class))).thenAnswer(inv -> inv.getArgument(0));

        Dish result = dishUseCase.createDish(dish, 10L);

        assertNotNull(result);
        assertTrue(result.getActivo());
        assertEquals(25000, result.getPrecio());
        verify(dishRepository).saveDish(any(Dish.class));
    }

    @Test
    void createDish_whenRestaurantNotFound_shouldThrowRestaurantNotFoundException() {
        when(restaurantRepository.findById(1L)).thenReturn(null);

        assertThrows(RestaurantNotFoundException.class,
                () -> dishUseCase.createDish(buildDish(), 10L));

        verify(dishRepository, never()).saveDish(any(Dish.class));
    }

    @Test
    void createDish_whenOwnerDoesNotMatch_shouldThrowNotRestaurantOwnerException() {
        when(restaurantRepository.findById(1L)).thenReturn(restaurant);

        assertThrows(NotRestaurantOwnerException.class,
                () -> dishUseCase.createDish(buildDish(), 99L));

        verify(dishRepository, never()).saveDish(any(Dish.class));
    }

    @Test
    void createDish_whenPriceIsZero_shouldThrowInvalidDishPriceException() {
        Dish dish = buildDish().toBuilder().precio(0).build();
        when(restaurantRepository.findById(1L)).thenReturn(restaurant);

        assertThrows(InvalidDishPriceException.class,
                () -> dishUseCase.createDish(dish, 10L));

        verify(dishRepository, never()).saveDish(any(Dish.class));
    }

    @Test
    void createDish_whenPriceIsNegative_shouldThrowInvalidDishPriceException() {
        Dish dish = buildDish().toBuilder().precio(-100).build();
        when(restaurantRepository.findById(1L)).thenReturn(restaurant);

        assertThrows(InvalidDishPriceException.class,
                () -> dishUseCase.createDish(dish, 10L));

        verify(dishRepository, never()).saveDish(any(Dish.class));
    }

    @Test
    void createDish_whenPriceIsNull_shouldThrowInvalidDishPriceException() {
        Dish dish = buildDish().toBuilder().precio(null).build();
        when(restaurantRepository.findById(1L)).thenReturn(restaurant);

        assertThrows(InvalidDishPriceException.class,
                () -> dishUseCase.createDish(dish, 10L));

        verify(dishRepository, never()).saveDish(any(Dish.class));
    }

    // ==================== HU04 updateDish ====================

    @Test
    void updateDish_success_shouldUpdateOnlyPriceAndDescription() {
        Dish dish = buildDish().toBuilder().id(5L).activo(true).build();
        when(dishRepository.findById(5L)).thenReturn(dish);
        when(restaurantRepository.findById(1L)).thenReturn(restaurant);
        when(dishRepository.saveDish(any(Dish.class))).thenAnswer(inv -> inv.getArgument(0));

        Dish result = dishUseCase.updateDish(5L, 30000, "Nueva descripción", 10L);

        assertNotNull(result);
        assertEquals(30000, result.getPrecio());
        assertEquals("Nueva descripción", result.getDescripcion());
        assertEquals(dish.getNombre(), result.getNombre());
        verify(dishRepository).saveDish(any(Dish.class));
    }

    @Test
    void updateDish_whenDishNotFound_shouldThrowDishNotFoundException() {
        when(dishRepository.findById(5L)).thenReturn(null);

        assertThrows(DishNotFoundException.class,
                () -> dishUseCase.updateDish(5L, 30000, "desc", 10L));

        verify(dishRepository, never()).saveDish(any(Dish.class));
    }

    @Test
    void updateDish_whenRestaurantNotFound_shouldThrowRestaurantNotFoundException() {
        Dish dish = buildDish().toBuilder().id(5L).build();
        when(dishRepository.findById(5L)).thenReturn(dish);
        when(restaurantRepository.findById(1L)).thenReturn(null);

        assertThrows(RestaurantNotFoundException.class,
                () -> dishUseCase.updateDish(5L, 30000, "desc", 10L));

        verify(dishRepository, never()).saveDish(any(Dish.class));
    }

    @Test
    void updateDish_whenOwnerDoesNotMatch_shouldThrowNotRestaurantOwnerException() {
        Dish dish = buildDish().toBuilder().id(5L).build();
        when(dishRepository.findById(5L)).thenReturn(dish);
        when(restaurantRepository.findById(1L)).thenReturn(restaurant);

        assertThrows(NotRestaurantOwnerException.class,
                () -> dishUseCase.updateDish(5L, 30000, "desc", 99L));

        verify(dishRepository, never()).saveDish(any(Dish.class));
    }

    // ==================== HU07 changeDishStatus ====================

    @Test
    void changeDishStatus_success_shouldToggleActiveFromTrueToFalse() {
        Dish dish = buildDish().toBuilder().id(5L).activo(true).build();
        when(dishRepository.findById(5L)).thenReturn(dish);
        when(restaurantRepository.findById(1L)).thenReturn(restaurant);

        dishUseCase.changeDishStatus(5L, 10L);

        // Verificamos que se guarda el plato con el estado invertido (false)
        verify(dishRepository).saveDish(argThat(savedDish -> !savedDish.getActivo()));
    }

    @Test
    void changeDishStatus_success_shouldToggleActiveFromFalseToTrue() {
        Dish dish = buildDish().toBuilder().id(5L).activo(false).build();
        when(dishRepository.findById(5L)).thenReturn(dish);
        when(restaurantRepository.findById(1L)).thenReturn(restaurant);

        dishUseCase.changeDishStatus(5L, 10L);

        // Verificamos que se guarda el plato con el estado invertido (true)
        verify(dishRepository).saveDish(argThat(Dish::getActivo));
    }

    @Test
    void changeDishStatus_whenDishNotFound_shouldThrowDishNotFoundException() {
        when(dishRepository.findById(5L)).thenReturn(null);

        assertThrows(DishNotFoundException.class,
                () -> dishUseCase.changeDishStatus(5L, 10L));

        verify(dishRepository, never()).saveDish(any(Dish.class));
    }

    @Test
    void changeDishStatus_whenRestaurantNotFound_shouldThrowRestaurantNotFoundException() {
        Dish dish = buildDish().toBuilder().id(5L).build();
        when(dishRepository.findById(5L)).thenReturn(dish);
        when(restaurantRepository.findById(1L)).thenReturn(null);

        assertThrows(RestaurantNotFoundException.class,
                () -> dishUseCase.changeDishStatus(5L, 10L));

        verify(dishRepository, never()).saveDish(any(Dish.class));
    }

    @Test
    void changeDishStatus_whenOwnerDoesNotMatch_shouldThrowNotRestaurantOwnerException() {
        Dish dish = buildDish().toBuilder().id(5L).build();
        when(dishRepository.findById(5L)).thenReturn(dish);
        when(restaurantRepository.findById(1L)).thenReturn(restaurant); // restaurant propietario es 10L

        assertThrows(NotRestaurantOwnerException.class,
                () -> dishUseCase.changeDishStatus(5L, 99L)); // Se intenta cambiar con propietario 99L

        verify(dishRepository, never()).saveDish(any(Dish.class));
    }

    // ==================== HU10 getDishesPaginated ====================

    @Test
    void getDishesPaginated_success_shouldReturnDishList() {
        // Arrange
        List<Dish> expectedDishes = List.of(buildDish());
        when(restaurantRepository.findById(1L)).thenReturn(restaurant);
        when(dishRepository.findDishesByRestaurantAndCategory(1L, 2L, 0, 10)).thenReturn(expectedDishes);

        // Act
        List<Dish> result = dishUseCase.getDishesPaginated(1L, 2L, 0, 10);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(restaurantRepository).findById(1L);
        verify(dishRepository).findDishesByRestaurantAndCategory(1L, 2L, 0, 10);
    }

    @Test
    void getDishesPaginated_whenRestaurantNotFound_shouldThrowRestaurantNotFoundException() {
        // Arrange
        when(restaurantRepository.findById(1L)).thenReturn(null);

        // Act & Assert
        assertThrows(RestaurantNotFoundException.class,
                () -> dishUseCase.getDishesPaginated(1L, 2L, 0, 10));

        // Verificamos que nunca se llame al repositorio de platos si el restaurante no existe
        verify(dishRepository, never()).findDishesByRestaurantAndCategory(any(), any(), anyInt(), anyInt());
    }
}