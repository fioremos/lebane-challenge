package com.lebane;

import com.lebane.dto.DepartamentoDTO;
import com.lebane.entities.Departamento;
import com.lebane.enums.Moneda;
import com.lebane.mapper.DepartamentoMapper;
import com.lebane.repository.DepartamentoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DepartamentoServiceTest {

    @Mock
    private DepartamentoRepository repository;

    @Mock
    private DepartamentoMapper mapper;

    @InjectMocks
    private DepartamentoService service;

    private Departamento departamentoEntity;
    private DepartamentoDTO departamentoDTO;

    @BeforeEach
    void setUp() {
        departamentoEntity = new Departamento();
        departamentoEntity.setId(1L);
        departamentoEntity.setTitulo("Depto de Prueba");
        departamentoEntity.setPrecio(1500.0);
        departamentoEntity.setMoneda(Moneda.USD);

        departamentoDTO = new DepartamentoDTO();
        departamentoDTO.setTitulo("Depto de Prueba");
        departamentoDTO.setPrecio(1500.0);
        departamentoDTO.setMoneda("USD");
    }

    @Test
    @DisplayName("Debe guardar un departamento y retornar el DTO con ID")
    void guardarDeptoOk() {
        when(mapper.toEntity(any(DepartamentoDTO.class))).thenReturn(departamentoEntity);
        when(repository.save(any(Departamento.class))).thenReturn(departamentoEntity);
        when(mapper.toDTO(any(Departamento.class))).thenReturn(departamentoDTO);

        DepartamentoDTO resultado = service.guardar(departamentoDTO);

        assertNotNull(resultado);
        assertEquals("Depto de Prueba", resultado.getTitulo());
        verify(repository, times(1)).save(any(Departamento.class));
    }

    @Test
    @DisplayName("Debe retornar IlleagalArgument cuando la moneda no es valida")
    void guardarDpto_MonedaInvalida() {

        departamentoDTO.setTitulo("Depto Test");
        departamentoDTO.setMoneda("BR");

        assertThrows(IllegalArgumentException.class, () -> {
            service.guardar(departamentoDTO);
        });

        verify(repository, never()).save(any());
    }

    @Test
    @DisplayName("Debe retornar una lista de DTOs cuando el repositorio encuentra datos")
    void listarConFiltros_Exitoso() {
        List<Departamento> entidades = List.of(departamentoEntity);

        when(repository.findAll(any(Specification.class))).thenReturn(entidades);
        when(mapper.toDTO(any(Departamento.class))).thenReturn(departamentoDTO);

        List<DepartamentoDTO> resultado = service.listarConFiltros(true, 100.0, 1500.0, 50.0, null);

        assertAll("Validar resultado del listado",
                () -> assertNotNull(resultado),
                () -> assertEquals(1, resultado.size()),
                () -> assertEquals("Depto de Prueba", resultado.get(0).getTitulo())
        );

        verify(repository, times(1)).findAll(any(Specification.class));
        verify(mapper, times(1)).toDTO(any(Departamento.class));
    }

    @Test
    @DisplayName("Debe retornar lista vacía cuando el repositorio no encuentra coincidencias")
    void listarConFiltros_Vacio() {
        when(repository.findAll(any(Specification.class))).thenReturn(Collections.emptyList());

        List<DepartamentoDTO> resultado = service.listarConFiltros(false, 0.0, 10.0, 10.0, null);

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());

        verify(mapper, never()).toDTO(any());
    }

    @Test
    @DisplayName("Debe lanzar IllegalArgumentException cuando el precio max es menor al min")
    void listar_ErrorRangoPrecios() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                service.listarConFiltros(true, 500.0, 100.0, 50.0, "USD")
        );

        assertEquals("El precio máximo no puede ser menor al mínimo", ex.getMessage());

        verify(repository, never()).findAll(any(Specification.class));
    }

    @Test
    @DisplayName("Debe actualizar correctamente cuando el ID existe")
    void actualizar_Exitoso() {
        when(repository.findById(1L)).thenReturn(Optional.of(departamentoEntity));
        when(repository.save(any(Departamento.class))).thenReturn(departamentoEntity);
        when(mapper.toDTO(any(Departamento.class))).thenReturn(departamentoDTO);

        DepartamentoDTO resultado = service.actualizar(1L, departamentoDTO);

        assertNotNull(resultado);
        assertEquals("Depto de Prueba", resultado.getTitulo());

        verify(repository).findById(1L);
        verify(repository).save(departamentoEntity);
    }

    @Test
    @DisplayName("Debe lanzar EntityNotFoundException cuando el ID no existe")
    void actualizar_NoEncontrado() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> {
            service.actualizar(99L, departamentoDTO);
        });

        verify(repository, never()).save(any());
    }

    @Test
    @DisplayName("Debe fallar si el ID existe pero la moneda nueva es inválida")
    void actualizar_MonedaInvalida() {
        when(repository.findById(1L)).thenReturn(Optional.of(departamentoEntity));
        departamentoDTO.setMoneda("BITCOIN");

        assertThrows(IllegalArgumentException.class, () -> {
            service.actualizar(1L, departamentoDTO);
        });

        verify(repository).findById(1L);
        verify(repository, never()).save(any());
    }

}