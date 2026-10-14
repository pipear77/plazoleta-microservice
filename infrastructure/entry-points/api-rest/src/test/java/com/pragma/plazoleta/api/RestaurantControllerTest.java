package com.pragma.plazoleta.api;

import com.pragma.plazoleta.api.restaurant.dto.RestaurantRequestDto;
import com.pragma.plazoleta.api.restaurant.RestaurantController;
import com.pragma.plazoleta.model.restaurant.Restaurant;
import com.pragma.plazoleta.model.restaurant.dto.RestaurantPaginationDto;
import com.pragma.plazoleta.usecase.restaurant.RestaurantUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.http.MediaType;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class RestaurantControllerTest {

    @Mock
    private RestaurantUseCase restaurantUseCase;

    @InjectMocks
    private RestaurantController restaurantController;

    private MockMvc mockMvc;

    // AÑADIDO: Inicialización obligatoria de MockMvc antes de cada prueba
    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(restaurantController).build();
    }

    @Test
    void createRestaurant_ValidRequest_ReturnsCreatedStatus() {
        // Arrange
        RestaurantRequestDto request = new RestaurantRequestDto(
                "El Buen Sabor",
                "900123456",
                "Calle Falsa 123",
                "+573001234567",
                "https://mi-logo.com/logo.png",
                1L
        );

        // Act
        ResponseEntity<Void> response = restaurantController.createRestaurant(request);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        // Verificamos que el controlador efectivamente llamó al caso de uso
        verify(restaurantUseCase).createRestaurant(any(Restaurant.class));
    }

    @Test
    void getRestaurants_shouldReturnOkAndList() throws Exception {
        // Arrange
        List<RestaurantPaginationDto> expectedList = List.of(
                new RestaurantPaginationDto("Asados el Gordo", "http://logo1.png"),
                new RestaurantPaginationDto("Burger King", "http://logo2.png")
        );

        when(restaurantUseCase.getRestaurantsPaginated(0, 10)).thenReturn(expectedList);

        // Act & Assert
        mockMvc.perform(get("/api/v1/restaurants")
                        .param("page", "0")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].nombre").value("Asados el Gordo"))
                .andExpect(jsonPath("$[0].urlLogo").value("http://logo1.png"))
                .andExpect(jsonPath("$[1].nombre").value("Burger King"));

        verify(restaurantUseCase).getRestaurantsPaginated(0, 10);
    }
}