package com.lebane.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lebane.DepartamentoService;
import com.lebane.dto.DepartamentoDTO;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


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

    @Test
    @DisplayName("GET /api/departamentos filtrar todos los datos")
    void listar_ConFiltros_DebeRetornar200() throws Exception {
        when(departamentoService.listarConFiltros(true, 100.0, 500.0, null, null))
                .thenReturn(List.of(deptoDTO));

        mockMvc.perform(get("/api/departamentos")
                        .param("disponible", "true")
                        .param("precioMin", "100")
                        .param("precioMax", "500"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].disponible").value(true));
    }

    @Test
    @DisplayName("GET /api/departamentos Retornar lista vacía (200 OK) cuando no hay coincidencias")
    void listar_SinCoincidencias_DebeRetornarListaVacia() throws Exception {
        when(departamentoService.listarConFiltros(true, 99999.0, 100000.0, null, null))
                .thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/departamentos")
                        .param("disponible", "true")
                        .param("precioMin", "99999")
                        .param("precioMax", "100000"))
                .andExpect(status().isOk()) // El status sigue siendo 200
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(0))
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    @DisplayName("PUT /api/departamentos/{id} actualización es exitosa")
    void actualizar_Exitoso() throws Exception {
        Long idExistente = 1L;
        when(departamentoService.actualizar(eq(idExistente), any(DepartamentoDTO.class)))
                .thenReturn(deptoDTO);

        mockMvc.perform(put("/api/departamentos/{id}", idExistente)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(deptoDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.titulo").value(deptoDTO.getTitulo()));
    }

    @Test
    @DisplayName("PUT /api/departamentos/{id} 404 cuando el depto no existe")
    void actualizar_NoEncontrado() throws Exception {
        Long idInexistente = 99L;
        when(departamentoService.actualizar(eq(idInexistente), any(DepartamentoDTO.class)))
                .thenThrow(new EntityNotFoundException("No encontrado"));

        mockMvc.perform(put("/api/departamentos/{id}", idInexistente)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(deptoDTO)))
                .andExpect(status().isNotFound());
    }
}