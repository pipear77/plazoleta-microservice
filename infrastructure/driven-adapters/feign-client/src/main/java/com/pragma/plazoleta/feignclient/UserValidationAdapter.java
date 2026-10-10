package com.pragma.plazoleta.feign;

import com.pragma.plazoleta.feignclient.UserResponseDto;
import com.pragma.plazoleta.model.restaurant.gateways.UserValidationService;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class UserValidationAdapter implements UserValidationService {

    private final RestClient restClient;

    public UserValidationAdapter() {
        // Inicializamos el cliente nativo apuntando a tu microservicio de usuarios
        this.restClient = RestClient.builder()
                .baseUrl("http://localhost:8081/api/v1/users")
                .build();
    }

    @Override
    public boolean isOwnerValid(Long ownerId) {
        try {
            UserResponseDto user = restClient.get()
                    .uri("/{id}", ownerId)
                    .retrieve()
                    .body(UserResponseDto.class);

            return user != null && "PROPIETARIO".equalsIgnoreCase(user.role());
        } catch (Exception e) {
            // Si retorna 404 u otro error, el usuario no es válido
            return false;
        }
    }
}