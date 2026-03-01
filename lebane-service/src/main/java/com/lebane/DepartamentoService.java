package com.lebane;

import com.lebane.dto.DepartamentoDTO;
import com.lebane.entities.Departamento;
import com.lebane.enums.Moneda;
import com.lebane.mapper.DepartamentoMapper;
import com.lebane.repository.DepartamentoRepository;
import com.lebane.specifications.DepartamentoSpecifications;
import jakarta.persistence.EntityNotFoundException;
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

    @Transactional
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

    @Transactional
    public DepartamentoDTO actualizar(Long id, DepartamentoDTO dto) {
        Departamento dptoExistente = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Departamento no encontrado con ID: " + id));

        Moneda.valueOf(dto.getMoneda().toUpperCase());

        dptoExistente.setTitulo(dto.getTitulo());
        dptoExistente.setPrecio(dto.getPrecio());
        dptoExistente.setMoneda(Moneda.valueOf(dto.getMoneda().toUpperCase()));
        dptoExistente.setMetrosCuadrados(dto.getMetrosCuadrados());
        dptoExistente.setDisponible(dto.getDisponible());

        return mapper.toDTO(repository.save(dptoExistente));
    }

}
