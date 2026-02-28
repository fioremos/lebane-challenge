package com.lebane;

import com.lebane.dto.DepartamentoDTO;
import com.lebane.entities.Departamento;
import com.lebane.enums.Moneda;
import com.lebane.mapper.DepartamentoMapper;
import com.lebane.repository.DepartamentoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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

}