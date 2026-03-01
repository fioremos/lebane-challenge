package com.lebane.specifications;


import com.lebane.entities.Departamento;
import com.lebane.enums.Moneda;
import com.lebane.repository.DepartamentoRepository;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertAll;

@SpringBootTest
@Testcontainers
@Transactional
public class DepartamentoSpecificationsTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "create-drop");
    }

    @Autowired
    private DepartamentoRepository repository;

    @Test
    @DisplayName("Debe filtrar por metros cuadrados y precio")
    public void filtrarPorMetrosYPrecio_RealDB() {
        Departamento d1 = new Departamento();
        d1.setTitulo("Studio");
        d1.setPrecio(1000.0);
        d1.setMetrosCuadrados(40.0);
        d1.setMoneda(Moneda.USD);
        repository.save(d1);

        Departamento d2 = new Departamento();
        d2.setTitulo("Mansión");
        d2.setPrecio(9000.0);
        d2.setMetrosCuadrados(250.0);
        d2.setMoneda(Moneda.USD);
        repository.save(d2);

        Specification<Departamento> spec = DepartamentoSpecifications
                .conFiltros(null, null, 2000.0, 35.0, "USD");

        List<Departamento> resultado = repository.findAll(spec);

        assertAll("Validar filtros en Testcontainers",
                () -> assertEquals(1, resultado.size()),
                () -> assertEquals("Studio", resultado.get(0).getTitulo())
        );
    }
}