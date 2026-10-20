package com.pragma.plazoleta;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class MainApplicationTest {

    @Test
    void main_StartsSuccessfully() {
        assertDoesNotThrow(() -> MainApplication.main(new String[]{}));
    }
}