package com.lebane;

import com.lebane.dto.DepartamentoDTO;
import com.lebane.entities.Departamento;
import com.lebane.enums.Moneda;
import com.lebane.mapper.DepartamentoMapper;
import com.lebane.repository.DepartamentoRepository;
import com.lebane.specifications.DepartamentoSpecifications;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

    public List<DepartamentoDTO> listarConFiltros(Boolean disponible, Double min, Double max, Double metrosCuadrados, String moneda) {
        if (min != null && max != null && max < min) {
            throw new IllegalArgumentException("El precio máximo no puede ser menor al mínimo");
        }

        Specification<Departamento> spec = DepartamentoSpecifications.conFiltros(disponible, min, max, metrosCuadrados, moneda);

        return repository.findAll(spec)
                .stream()
                .map(mapper::toDTO)
                .toList();
    }
}
