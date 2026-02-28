package com.lebane.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DepartamentoDTO {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @NotBlank(message = "El título no puede estar vacío")
    @Size(min = 10, max = 100, message = "El título debe tener entre 10 y 100 caracteres")
    private String titulo;

    @NotBlank(message = "La descripción es requerida")
    private String descripcion;

    @NotNull(message = "El precio es obligatorio")
    @DecimalMin(value = "0.0", inclusive = false, message = "El precio debe ser mayor a 0")
    private Double precio;

    @NotNull(message = "La moneda es obligatoria")
    private String moneda;

    @Positive(message = "Los metros cuadrados deben ser un número positivo")
    private Double metrosCuadrados;

    @NotBlank(message = "La dirección es obligatoria")
    private String direccion;

    private Boolean disponible;
}