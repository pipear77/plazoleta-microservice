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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
}