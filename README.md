# Lebane Challenge - Gestión de Departamentos

API REST para la gestión y filtrado dinámico de unidades inmobiliarias, desarrollada con **Java 17**, **Spring Boot 3** y **PostgreSQL**.

---

## Instalación y Ejecución con Docker

La aplicación está completamente dockerizada, lo que garantiza que funcione en cualquier entorno sin necesidad de instalar Java o PostgreSQL localmente.

### Requisitos previos
* **Docker Compose**.

### Pasos para levantar el proyecto
1.  Clona el repositorio en tu máquina local.
2.  Ubícate en la raíz del proyecto (donde está el archivo `docker-compose.yml`).
3.  Ejecuta el siguiente comando:
    ```bash
    docker-compose up --build
    ```
4.  La aplicación estará disponible en `http://localhost:8080`.

> **💡 Tip de Configuración:** Si el puerto 8080 está ocupado, se puede cambiar mediante variables de entorno sin tocar el código:
> `HOST_PORT=9090 APP_PORT=9090 docker-compose up --build`

---

## Documentación de la API (Swagger)

Se incluyó **OpenAPI 3** para facilitar la prueba de los endpoints de forma visual e interactiva. Una vez que la aplicación esté corriendo, accedé a:

**[http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)**

---

## Suite de Tests

Se implementó una estrategia de testing integral para asegurar la calidad en cada capa del sistema, desde la lógica de negocio hasta la persistencia real.

### Ejecución de los tests
Se puede ejecutar la suite completa con:
```bash
./gradlew test
```

### Tipos de Tests incluidos:

* Tests Unitarios (Mockito): Validación de lógica de negocio en el Service.

* Tests de Integración de Capa Web (@WebMvcTest): Validación de contratos API, códigos de estado HTTP y serialización JSON.

* Tests de Persistencia con Testcontainers: Se utiliza un contenedor real de PostgreSQL para validar que las JPA Specifications generen el SQL correcto y que las constraints de base de datos se respeten.

## Decisiones de Arquitectura

Para este challenge se aplicaron los siguientes patrones y criterios:

* JPA Specifications: Se optó por este patrón para el filtrado dinámico. Esto evita la proliferación de métodos en el repositorio y permite combinar múltiples filtros opcionales de forma limpia y escalable.

* Arquitectura Multimódulo / Capas: Separación clara entre Controller (Entrada/Salida), Service (Lógica de Negocio) y Repository (Persistencia).

* Manejo Global de Excepciones: Uso de @RestControllerAdvice para capturar errores como EntityNotFoundException y devolver respuestas estandarizadas al cliente con el código HTTP adecuado (404, 400).

* Estrategia de Dockerización:

    - Multi-stage Build: El Dockerfile construye el JAR y luego lo mueve a una imagen JRE liviana para optimizar el tamaño.

    - Healthchecks: El servicio de la App espera a que PostgreSQL esté 100% listo antes de iniciar, garantizando resiliencia en el despliegue.

    - Persistencia de datos: Se configuraron volúmenes para que los datos no se pierdan al reiniciar los contenedores.

---

## Arquitectura del Proyecto

El proyecto está diseñado como una aplicación **multi-módulo Gradle** para promover la separación de responsabilidades y facilitar la escalabilidad y reutilización. Los módulos principales son:

* `lebane-domain`: Contiene las entidades JPA, enumeraciones y objetos de valor. No tiene dependencias de Spring.
* `lebane-repository`: Implementa los repositorios **Spring Data JPA**, las especificaciones de filtrado y las consultas a la base de datos.
* `lebane-service`: Incluye la lógica de negocio, las interfaces de servicio y los casos de uso. Aquí se ejecutan las validaciones y transformaciones entre DTO y entidades.
* `lebane-api`: Exposición de la API REST, controladores, DTOs, mappings automáticos (MapStruct) y la configuración de Swagger y seguridad.

Cada módulo está declarado en `settings.gradle` y compila como un artefacto independiente, permitiendo que, por ejemplo, `lebane-service` pueda ser utilizado por otra aplicación en el futuro.

### Flujo de una petición típica

1. Un cliente HTTP realiza una petición al endpoint expuesto por `DepartamentoController` en `lebane-api`.
2. El controlador deserializa la petición a DTOs y delega al servicio correspondiente (`DepartamentoService`).
3. El `Service` aplica las reglas de negocio, construye las especificaciones de filtrado (usando `DepartmentSpecifications`) y llama al repositorio.
4. El repositorio, basado en `JpaSpecificationExecutor`, genera la consulta SQL dinámica y ejecuta contra PostgreSQL.
5. El resultado se convierte en DTOs y se devuelve al cliente con el status HTTP adecuado.

### Dependencias y tecnologías clave

* **Java 17** y **Spring Boot 3**.
* **Gradle** con múltiples módulos.
* **Spring Data JPA** + Hibernate.
* **MapStruct** para mapeo de DTOs.
* **Testcontainers** para pruebas de integración con PostgreSQL real.
* **Swagger / OpenAPI 3**.
* **Docker** con Compose y multi-stage builds.

### Organización de carpetas

El repositorio raíz contiene el `docker-compose.yml` y algunos scripts de configuración. Cada módulo (`lebane-api`, `lebane-service`, `lebane-repository`, `lebane-domain`) sigue la estructura estándar de un proyecto Spring Boot (`src/main/java`, `src/test/java`).

### Manejo de errores y validaciones

Se implementó un `GlobalExceptionHandler` utilizando `@RestControllerAdvice` que captura excepciones comunes y transforma en respuestas JSON estandarizadas. Se usan validaciones de Bean (`@Valid`) en los DTO de entrada.
