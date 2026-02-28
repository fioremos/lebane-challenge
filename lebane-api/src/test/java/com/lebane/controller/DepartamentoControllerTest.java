package com.lebane.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lebane.DepartamentoService;
import com.lebane.dto.DepartamentoDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(DepartamentoController.class)
class DepartamentoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DepartamentoService departamentoService;

    @Autowired
    private ObjectMapper objectMapper;

    private DepartamentoDTO deptoDTO;

    @BeforeEach
    void setUp() {
        deptoDTO = DepartamentoDTO.builder()
                .titulo("Departamento de prueba")
                .descripcion("Una descripción de más de diez caracteres")
                .precio(1500.0)
                .moneda("USD")
                .direccion("Calle Falsa 123")
                .disponible(true)
                .build();
    }

    @Test
    @DisplayName("POST /api/departamentos debe retornar 202 Accepted")
    void crearDepartamento_DebeRetornar202() throws Exception {
        when(departamentoService.guardar(any(DepartamentoDTO.class))).thenReturn(deptoDTO);

        mockMvc.perform(post("/api/departamentos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(deptoDTO)))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.titulo").value("Departamento de prueba"))
                .andExpect(jsonPath("$.precio").value(1500.0));
    }

    @Test
    @DisplayName("POST /api/departamentos con precio negativo debe retornar 400 Bad Request")
    void crearDepartamento_PrecioInvalido_DebeRetornar400() throws Exception {
        deptoDTO.setPrecio(-10.0);

        mockMvc.perform(post("/api/departamentos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(deptoDTO)))
                .andExpect(status().isBadRequest());

        verify(departamentoService, never()).guardar(any());
    }
}