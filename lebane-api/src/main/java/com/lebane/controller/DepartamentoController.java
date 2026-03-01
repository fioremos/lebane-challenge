package com.lebane.controller;

import com.lebane.DepartamentoService;
import com.lebane.dto.DepartamentoDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/departamentos")
@RequiredArgsConstructor
@Tag(name = "Departamentos", description = "Operaciones de gestión de unidades inmobiliarias")
public class DepartamentoController {
    private final DepartamentoService service;

    @Operation(
            summary = "Crear un nuevo departamento",
            description = "Registra un departamento en el sistema. Retorna 202 Accepted mientras se procesa la persistencia."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "Departamento aceptado para creación"),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos")
    })
    @PostMapping
    public ResponseEntity<DepartamentoDTO> crear(@Valid @RequestBody DepartamentoDTO dto) {
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(service.guardar(dto));
    }

    @Operation(
            summary = "Listar departamentos con filtros",
            description = "Obtiene una lista de departamentos. Todos los filtros son opcionales y combinables."
    )
    @ApiResponse(responseCode = "200", description = "Lista obtenida exitosamente")
    @GetMapping
    public ResponseEntity<List<DepartamentoDTO>> obtener(
            @RequestParam(required = false) Boolean disponible,
            @RequestParam(required = false) Double precioMin,
            @RequestParam(required = false) Double precioMax,
            @RequestParam(required = false) Double metrosCuadrados,
            @RequestParam(required = false) String moneda) {

        List<DepartamentoDTO> departamentos = service.listarConFiltros(
                disponible,
                precioMin,
                precioMax,
                metrosCuadrados,
                moneda
        );

        return ResponseEntity.ok(departamentos);
    }

    @Operation(
            summary = "Actualizar un departamento existente",
            description = "Modifica los datos de un departamento basado en su ID único."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Actualización exitosa"),
            @ApiResponse(responseCode = "404", description = "El ID proporcionado no existe"),
            @ApiResponse(responseCode = "400", description = "Datos de actualización inválidos")
    })
    @PutMapping("/{id}")
    public ResponseEntity<DepartamentoDTO> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody DepartamentoDTO dptoDTO) {

        DepartamentoDTO actualizado = service.actualizar(id, dptoDTO);

        return ResponseEntity.ok(actualizado);
    }

}
