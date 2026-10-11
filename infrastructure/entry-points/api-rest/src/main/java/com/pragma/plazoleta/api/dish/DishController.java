package com.pragma.plazoleta.api.dish;

import com.pragma.plazoleta.model.dish.Dish;
import com.pragma.plazoleta.usecase.dish.DishUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/dishes")
@RequiredArgsConstructor
public class DishController {

    private final DishUseCase dishUseCase;

    @PostMapping
    public ResponseEntity<Dish> createDish(@Valid @RequestBody DishRequestDto request) {
        Dish dish = Dish.builder()
                .nombre(request.getNombre())
                .precio(request.getPrecio())
                .descripcion(request.getDescripcion())
                .urlImagen(request.getUrlImagen())
                .idCategoria(request.getIdCategoria())
                .idRestaurante(request.getIdRestaurante())
                .build();

        Dish saved = dishUseCase.createDish(dish, request.getIdPropietario());
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Dish> updateDish(
            @PathVariable("id") Long id,
            @Valid @RequestBody DishUpdateDto request) {

        Dish updated = dishUseCase.updateDish(
                id,
                request.getPrecio(),
                request.getDescripcion(),
                request.getIdPropietario()
        );

        return ResponseEntity.ok(updated);
    }
}