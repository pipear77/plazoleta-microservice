package com.pragma.plazoleta.api.dish;

import com.pragma.plazoleta.model.dish.Dish;
import com.pragma.plazoleta.usecase.dish.DishUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;

import java.util.List;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@ExtendWith(MockitoExtension.class)
class DishControllerTest {

    @Mock
    private DishUseCase dishUseCase;

    @InjectMocks
    private DishController controller;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    // ==================== POST (Create Dish) Tests ====================

    @Test
    void createDish_success_shouldReturnCreated() throws Exception {
        String json = "{\"nombre\":\"Bandeja Paisa\",\"precio\":25000,"
                + "\"descripcion\":\"Deliciosa\",\"urlImagen\":\"http://img\","
                + "\"idCategoria\":1,\"idRestaurante\":1,\"idPropietario\":10}";

        Dish saved = Dish.builder()
                .id(1L)
                .nombre("Bandeja Paisa")
                .precio(25000)
                .descripcion("Deliciosa")
                .urlImagen("http://img")
                .idCategoria(1L)
                .idRestaurante(1L)
                .activo(true)
                .build();

        when(dishUseCase.createDish(any(Dish.class), eq(10L))).thenReturn(saved);

        mockMvc.perform(post("/api/v1/dishes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated());

        verify(dishUseCase).createDish(any(Dish.class), eq(10L));
    }

    @Test
    void createDish_whenPriceNotPositive_shouldReturnBadRequest() throws Exception {
        String json = "{\"nombre\":\"Bandeja Paisa\",\"precio\":0,"
                + "\"descripcion\":\"Deliciosa\",\"urlImagen\":\"http://img\","
                + "\"idCategoria\":1,\"idRestaurante\":1,\"idPropietario\":10}";

        mockMvc.perform(post("/api/v1/dishes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());

        verify(dishUseCase, never()).createDish(any(Dish.class), anyLong());
    }

    @Test
    void createDish_whenNameBlank_shouldReturnBadRequest() throws Exception {
        String json = "{\"nombre\":\"\",\"precio\":25000,"
                + "\"descripcion\":\"Deliciosa\",\"urlImagen\":\"http://img\","
                + "\"idCategoria\":1,\"idRestaurante\":1,\"idPropietario\":10}";

        mockMvc.perform(post("/api/v1/dishes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());

        verify(dishUseCase, never()).createDish(any(Dish.class), anyLong());
    }

    // ==================== PUT (Update Dish) Tests ====================

    @Test
    void updateDish_success_shouldReturnOk() throws Exception {
        String json = "{\"precio\":32000,"
                + "\"descripcion\":\"Nueva descripcion\","
                + "\"idPropietario\":1}";

        Dish updatedDish = Dish.builder()
                .id(1L)
                .nombre("Bandeja Paisa")
                .precio(32000)
                .descripcion("Nueva descripcion")
                .urlImagen("http://img")
                .idCategoria(1L)
                .idRestaurante(1L)
                .activo(true)
                .build();

        when(dishUseCase.updateDish(eq(1L), eq(32000), eq("Nueva descripcion"), eq(1L)))
                .thenReturn(updatedDish);

        mockMvc.perform(put("/api/v1/dishes/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk());

        verify(dishUseCase).updateDish(eq(1L), eq(32000), eq("Nueva descripcion"), eq(1L));
    }

    @Test
    void updateDish_whenPriceInvalid_shouldReturnBadRequest() throws Exception {
        String json = "{\"precio\":0,"
                + "\"descripcion\":\"Nueva descripcion\","
                + "\"idPropietario\":1}";

        mockMvc.perform(put("/api/v1/dishes/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());

        verify(dishUseCase, never()).updateDish(anyLong(), any(), any(), anyLong());
    }

    @Test
    void updateDish_whenDescriptionBlank_shouldReturnBadRequest() throws Exception {
        String json = "{\"precio\":32000,"
                + "\"descripcion\":\"\","
                + "\"idPropietario\":1}";

        mockMvc.perform(put("/api/v1/dishes/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());

        verify(dishUseCase, never()).updateDish(anyLong(), any(), any(), anyLong());
    }

    // ==================== PATCH (Change Status) Tests ====================

    @Test
    void changeDishStatus_success_shouldReturnOk() throws Exception {
        // Mock SecurityContextHolder para simular la extracción del ID del propietario desde el token
        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getDetails()).thenReturn(10L); // El ID del propietario mockeado
        SecurityContextHolder.setContext(securityContext);

        // Act & Assert
        mockMvc.perform(patch("/api/v1/dishes/1/status")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        // Verificamos que se llame al caso de uso con el ID del plato (1) y el ID del propietario extraído (10)
        verify(dishUseCase).changeDishStatus(eq(1L), eq(10L));

        // Limpieza del contexto
        SecurityContextHolder.clearContext();
    }

    // ==================== GET (List Dishes HU10) Tests ====================

    @Test
    void getDishesByRestaurant_withCategoryId_shouldReturnOkAndList() throws Exception {
        // Arrange
        List<Dish> mockDishes = List.of(
                Dish.builder().id(1L).nombre("Plato Filtrado 1").build(),
                Dish.builder().id(2L).nombre("Plato Filtrado 2").build()
        );

        // Simulamos la llamada con idRestaurante=1 y categoryId=2
        when(dishUseCase.getDishesPaginated(1L, 2L, 0, 10)).thenReturn(mockDishes);

        // Act & Assert
        mockMvc.perform(get("/api/v1/dishes/restaurant/1")
                        .param("categoryId", "2")
                        .param("page", "0")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].nombre").value("Plato Filtrado 1"));

        verify(dishUseCase).getDishesPaginated(1L, 2L, 0, 10);
    }

    @Test
    void getDishesByRestaurant_withoutCategoryId_shouldReturnOkAndList() throws Exception {
        // Arrange
        List<Dish> mockDishes = List.of(
                Dish.builder().id(1L).nombre("Plato General 1").build()
        );

        // Simulamos la llamada con categoryId nulo (sin filtro)
        when(dishUseCase.getDishesPaginated(1L, null, 0, 10)).thenReturn(mockDishes);

        // Act & Assert
        mockMvc.perform(get("/api/v1/dishes/restaurant/1")
                        .param("page", "0")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].nombre").value("Plato General 1"));

        verify(dishUseCase).getDishesPaginated(1L, null, 0, 10);
    }
}