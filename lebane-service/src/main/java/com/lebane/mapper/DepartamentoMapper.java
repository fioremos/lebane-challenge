package com.lebane.mapper;

import com.lebane.entities.Departamento;
import com.lebane.dto.DepartamentoDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DepartamentoMapper {
    Departamento toEntity(DepartamentoDTO dto);
    DepartamentoDTO toDTO(Departamento entity);
}