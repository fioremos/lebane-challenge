package com.lebane;

import com.lebane.dto.DepartamentoDTO;
import com.lebane.entities.Departamento;
import com.lebane.enums.Moneda;
import com.lebane.mapper.DepartamentoMapper;
import com.lebane.repository.DepartamentoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DepartamentoService {

    private final DepartamentoRepository repository;

    private final DepartamentoMapper mapper;

    @Transactional
    public DepartamentoDTO guardar(DepartamentoDTO dptoDTO) {
            Moneda.valueOf(dptoDTO.getMoneda().toUpperCase());
            Departamento dpto = mapper.toEntity(dptoDTO);

            Departamento dptoSaved = repository.save(dpto);
            return mapper.toDTO(dptoSaved);
    }
}
