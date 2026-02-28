package com.lebane;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

public class SpringJdbcTest {
    public static void main(String[] args) {
        DriverManagerDataSource ds = new DriverManagerDataSource();
        ds.setDriverClassName("org.postgresql.Driver");
        ds.setUrl("jdbc:postgresql://localhost:5432/departamentos");
        ds.setUsername("postgres");
        ds.setPassword("*****");

        JdbcTemplate jdbcTemplate = new JdbcTemplate(ds);
        Integer result = jdbcTemplate.queryForObject("SELECT 1", Integer.class);
        System.out.println("Conexión Spring OK: " + result);
    }
}