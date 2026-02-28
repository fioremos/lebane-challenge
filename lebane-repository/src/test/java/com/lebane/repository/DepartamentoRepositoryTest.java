package com.lebane.repository;

import com.lebane.entities.Departamento;
import com.lebane.enums.Moneda;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(classes={DepartamentoRepository.class})
@Testcontainers
class DepartamentoRepositoryTest {

//    @Container
//    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine")
//            .withDatabaseName("lebane_db")
//            .withUsername("test")
//            .withPassword("test");
//
//    @DynamicPropertySource
//    static void configureProperties(DynamicPropertyRegistry registry) {
//        registry.add("spring.datasource.url", postgres::getJdbcUrl);
//        registry.add("spring.datasource.username", postgres::getUsername);
//        registry.add("spring.datasource.password", postgres::getPassword);
//        registry.add("spring.jpa.hibernate.ddl-auto", () -> "create-drop");
//    }
//
//    @Autowired
//    private DepartamentoRepository repository;
//
//    @Test
//    @DisplayName("Debe guardar correctamente en la base")
//    void guardarEnBase() {
//        Departamento depto = new Departamento();
//        depto.setTitulo("Depto Test Containers");
//        depto.setPrecio(2000.0);
//        depto.setMoneda(Moneda.USD);
//
//        Departamento guardado = repository.save(depto);
//
//        assertNotNull(guardado.getId());
//        assertEquals("Depto Test Containers", guardado.getTitulo());
//    }
}