package com.lebane.controller;

import com.lebane.DepartamentoService;
import com.lebane.entities.Departamento;
import com.lebane.dto.DepartamentoDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/departamentos")
@RequiredArgsConstructor
public class DepartamentoController {
    private final DepartamentoService service;

    @PostMapping
    public ResponseEntity<DepartamentoDTO> crear(@Valid @RequestBody DepartamentoDTO dto) {
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(service.guardar(dto));
    }

}
