package com.lebane.mapper;

import com.lebane.dto.DepartamentoDTO;
import com.lebane.entities.Departamento;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DepartamentoMapper {
    Departamento toEntity(DepartamentoDTO dto);
    DepartamentoDTO toDTO(Departamento entity);
}