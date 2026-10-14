package com.pragma.plazoleta.api.dish;

import com.pragma.plazoleta.model.dish.Dish;
import com.pragma.plazoleta.usecase.dish.DishUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    // ENDPOINT: HU07
    @PatchMapping("/{id}/status")
    public ResponseEntity<Void> changeDishStatus(@PathVariable("id") Long id) {
        // Extraemos el ID del propietario logueado desde el token JWT (que guardaste en el filtro)
        Long idPropietario = (Long) SecurityContextHolder.getContext().getAuthentication().getDetails();

        dishUseCase.changeDishStatus(id, idPropietario);

        return ResponseEntity.ok().build();
    }

    // ENDPOINT: HU10
    @GetMapping("/restaurant/{idRestaurante}")
    public ResponseEntity<List<Dish>> getDishesByRestaurant(
            @PathVariable("idRestaurante") Long idRestaurante,
            @RequestParam(name = "categoryId", required = false) Long categoryId,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size) {

        List<Dish> dishes = dishUseCase.getDishesPaginated(idRestaurante, categoryId, page, size);
        return ResponseEntity.ok(dishes);
    }
}