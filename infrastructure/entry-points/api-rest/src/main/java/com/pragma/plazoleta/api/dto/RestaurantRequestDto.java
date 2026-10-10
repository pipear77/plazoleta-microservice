package com.pragma.plazoleta.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record RestaurantRequestDto(
        @NotBlank(message = "El nombre es obligatorio")
        @Pattern(regexp = "^(?!\\d+$).*$", message = "El nombre del restaurante no puede consistir únicamente de números.")
        String nombre,

        @NotBlank(message = "El NIT es obligatorio")
        @Pattern(regexp = "^\\d+$", message = "El NIT debe ser estrictamente numérico.")
        String nit,

        @NotBlank(message = "La dirección es obligatoria")
        String direccion,

        @NotBlank(message = "El teléfono es obligatorio")
        @Pattern(regexp = "^\\+?\\d{1,12}$", message = "El teléfono debe ser numérico, máximo 13 caracteres y puede incluir '+'.")
        String telefono,

        @NotBlank(message = "La URL del logo es obligatoria")
        String urlLogo,

        @NotNull(message = "El ID del propietario es obligatorio")
        Long idPropietario
) {}