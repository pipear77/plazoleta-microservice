package com.pragma.plazoleta.usecase.restaurant;

import com.pragma.plazoleta.model.restaurant.Restaurant;
import com.pragma.plazoleta.model.restaurant.dto.RestaurantPaginationDto;
import com.pragma.plazoleta.model.restaurant.gateways.RestaurantRepository;
import com.pragma.plazoleta.model.restaurant.gateways.UserValidationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RestaurantUseCaseTest {

    @Mock
    private RestaurantRepository restaurantRepository;

    @Mock
    private UserValidationService userValidationService;

    @InjectMocks
    private RestaurantUseCase restaurantUseCase;

    private Restaurant validRestaurant;

    @BeforeEach
    void setUp() {
        // Configuramos un restaurante válido para usarlo de base en las pruebas
        validRestaurant = Restaurant.builder()
                .id(1L)
                .nombre("El Buen Sabor")
                .nit("900123456")
                .direccion("Calle Falsa 123")
                .telefono("+573001234567")
                .urlLogo("https://mi-logo.com/logo.png")
                .idPropietario(1L)
                .build();
    }

    @Test
    void createRestaurant_ValidDataAndOwner_SavesRestaurant() {
        // Arrange
        when(userValidationService.isOwnerValid(1L)).thenReturn(true);
        when(restaurantRepository.saveRestaurant(any(Restaurant.class))).thenReturn(validRestaurant);

        // Act
        Restaurant result = restaurantUseCase.createRestaurant(validRestaurant);

        // Assert
        assertNotNull(result);
        assertEquals("El Buen Sabor", result.getNombre());
        verify(userValidationService).isOwnerValid(1L);
        verify(restaurantRepository).saveRestaurant(validRestaurant);
    }

    @Test
    void createRestaurant_OwnerInvalid_ThrowsException() {
        // Arrange
        when(userValidationService.isOwnerValid(1L)).thenReturn(false);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            restaurantUseCase.createRestaurant(validRestaurant);
        });

        assertEquals("El ID proporcionado no pertenece a un Propietario válido.", exception.getMessage());
        // Verificamos que NUNCA se llame al repositorio si falla la validación
        verify(restaurantRepository, never()).saveRestaurant(any());
    }

    @Test
    void createRestaurant_NameOnlyNumbers_ThrowsException() {
        // Arrange
        validRestaurant.setNombre("123456"); // Nombre inválido

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            restaurantUseCase.createRestaurant(validRestaurant);
        });
        assertEquals("El nombre del restaurante no puede consistir únicamente de números.", exception.getMessage());
    }

    @Test
    void createRestaurant_InvalidNit_ThrowsException() {
        // Arrange
        validRestaurant.setNit("900123ABC"); // NIT con letras

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            restaurantUseCase.createRestaurant(validRestaurant);
        });
        assertEquals("El NIT debe ser estrictamente numérico.", exception.getMessage());
    }

    @Test
    void createRestaurant_InvalidPhone_ThrowsException() {
        // Arrange
        validRestaurant.setTelefono("telefono123"); // Teléfono inválido

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            restaurantUseCase.createRestaurant(validRestaurant);
        });
        assertEquals("El teléfono debe ser numérico, máximo 13 caracteres y puede incluir '+'.", exception.getMessage());
    }

    @Test
    void getRestaurantsPaginated_shouldReturnPaginatedList() {
        // Arrange
        int page = 0;
        int size = 10;
        List<RestaurantPaginationDto> expectedList = List.of(
                new RestaurantPaginationDto("Asados el Gordo", "http://logo1.png"),
                new RestaurantPaginationDto("Burger King", "http://logo2.png")
        );

        when(restaurantRepository.findAllPaginated(page, size)).thenReturn(expectedList);

        // Act
        List<RestaurantPaginationDto> result = restaurantUseCase.getRestaurantsPaginated(page, size);

        // Assert
        assertEquals(2, result.size());
        assertEquals("Asados el Gordo", result.get(0).getNombre());
        verify(restaurantRepository).findAllPaginated(page, size);
    }
}