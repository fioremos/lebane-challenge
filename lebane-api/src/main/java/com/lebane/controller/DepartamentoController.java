package com.lebane.controller;

import com.lebane.DepartamentoService;
import com.lebane.dto.DepartamentoDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/departamentos")
@RequiredArgsConstructor
public class DepartamentoController {
    private final DepartamentoService service;

    @PostMapping
    public ResponseEntity<DepartamentoDTO> crear(@Valid @RequestBody DepartamentoDTO dto) {
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(service.guardar(dto));
    }

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

}
