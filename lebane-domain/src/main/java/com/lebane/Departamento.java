package com.lebane;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

@Entity
@Table(name = "departamentos")
@Data
public class Departamento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El título es obligatorio")
    private String titulo;

    private String descripcion;

    @PositiveOrZero
    private Double precio;

    @Enumerated(EnumType.STRING)
    private Moneda moneda;

    @Positive
    private Double metrosCuadrados;

    private String direccion;
    private Boolean disponible = true;
}