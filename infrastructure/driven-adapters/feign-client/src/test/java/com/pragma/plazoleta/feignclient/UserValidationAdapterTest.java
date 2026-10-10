package com.pragma.plazoleta.feignclient;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestClient;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserValidationAdapterTest {

    @InjectMocks
    private com.pragma.plazoleta.feign.UserValidationAdapter adapter;

    // Usamos RETURNS_DEEP_STUBS para poder encadenar los métodos get().uri().retrieve().body()
    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private RestClient restClient;

    @BeforeEach
    void setUp() {
        // Interceptamos la variable privada inicializada en el constructor y le inyectamos nuestro mock
        ReflectionTestUtils.setField(adapter, "restClient", restClient);
    }

    @Test
    void isOwnerValid_UserIsPropietario_ReturnsTrue() {
        // Arrange
        UserResponseDto mockResponse = new UserResponseDto(1L, "PROPIETARIO");

        when(restClient.get()
                .uri("/{id}", 1L)
                .retrieve()
                .body(UserResponseDto.class))
                .thenReturn(mockResponse);

        // Act
        boolean result = adapter.isOwnerValid(1L);

        // Assert
        assertTrue(result);
    }

    @Test
    void isOwnerValid_UserIsNotPropietario_ReturnsFalse() {
        // Arrange
        UserResponseDto mockResponse = new UserResponseDto(1L, "ADMINISTRADOR");

        when(restClient.get()
                .uri("/{id}", 1L)
                .retrieve()
                .body(UserResponseDto.class))
                .thenReturn(mockResponse);

        // Act
        boolean result = adapter.isOwnerValid(1L);

        // Assert
        assertFalse(result);
    }

    @Test
    void isOwnerValid_ApiThrowsException_ReturnsFalse() {
        // Arrange: Simulamos una caída de red o un 404
        when(restClient.get()
                .uri("/{id}", 1L)
                .retrieve()
                .body(UserResponseDto.class))
                .thenThrow(new RuntimeException("Connection refused"));

        // Act
        boolean result = adapter.isOwnerValid(1L);

        // Assert
        assertFalse(result); // Debe entrar al bloque catch y retornar false
    }
}