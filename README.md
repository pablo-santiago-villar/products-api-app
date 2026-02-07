# Products API

API REST para gestión y búsqueda de productos con filtrado temporal y validación de disponibilidad por marca.

## 📋 Tabla de Contenidos

- [Características](#características)
- [Requisitos Previos](#requisitos-previos)
- [Instalación](#instalación)
- [Configuración](#configuración)
- [Uso](#uso)
- [API Endpoints](#api-endpoints)
- [Testing](#testing)
- [Arquitectura](#arquitectura)
- [Decisiones Técnicas](#decisiones-técnicas)
- [Contribuir](#contribuir)
- [Licencia](#licencia)

## ✨ Características

- ✅ Búsqueda avanzada de productos con filtros por marca y timestamp
- ✅ Respuestas tipificadas con envelope `BaseResponse`
- ✅ Validación robusta de entrada de datos
- ✅ Pruebas automatizadas completas (5 escenarios)
- ✅ Código limpio siguiendo principios SOLID
- ✅ Separación de responsabilidades con arquitectura en capas

## 📦 Requisitos Previos

- **Java 17+**
- **Maven 3.8+**
- **Spring Boot 3.x**
- Docker (opcional, para ambiente containerizado)

## 🚀 Instalación

```bash
# Clonar el repositorio
git clone <repository>
cd products-api

# Compilar e instalar dependencias
mvn clean install

# O solo compilar sin ejecutar tests
mvn clean install -DskipTests
```

## ⚙️ Configuración

### Perfiles de Ambiente

**H2 (Desarrollo & Tests)**:

```yaml
# src/main/resources/application.yml
spring:
  datasource:
    url: jdbc:h2:mem:productsdb
    driverClassName: org.h2.Driver
```

**MySQL (Producción)**:

```yaml
# src/main/resources/application-prod.yml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/products
    username: root
    password: root
```

**Tests**:

```yaml
# src/test/resources/application-test.yml
spring:
  datasource:
    url: jdbc:h2:mem:testdb
    driverClassName: org.h2.Driver
```

### Variables de Entorno

Crear archivo `src/main/resources/application.yml` (ya incluido):

```yaml
spring:
  application:
    name: products-api

  datasource:
    url: jdbc:h2:mem:productsdb
    driverClassName: org.h2.Driver
    username: sa
    password:

  h2:
    console:
      enabled: true

server:
  port: 8080
  servlet:
    context-path: /api

logging:
  level:
    root: INFO
    com.products: DEBUG
```

## 🔧 Uso

### Ejecución Local

```bash
# Con Maven
mvn spring-boot:run

# Con JAR compilado
java -jar target/products-api-1.0.0.jar
```

La API estará disponible en: `http://localhost:8080`

## 📚 Documentación API (Swagger)

Una vez levantada la aplicación, accede a la documentación interactiva:

| Recurso          | URL                                   |
|------------------|---------------------------------------|
| **Swagger UI**   | http://localhost:8080/swagger-ui.html |
| **OpenAPI JSON** | http://localhost:8080/v3/api-docs     |
| **Health Check** | http://localhost:8080/actuator/health |

> 💡 **Tip**: Al iniciar la aplicación, las URLs aparecerán en consola.

## 📡 API Endpoints

### POST /products/search

Busca productos con validación de disponibilidad temporal por marca.

**Request:**

```json
{
  "productId": 35455,
  "brandId": 1,
  "applicationDate": "2020-06-14T10:00:00"
}
```

**Response (200 OK):**

```json
{
  "errorMessage": "",
  "data": {
    "productId": 35455,
    "brandId": 1,
    "priceList": 1,
    "startDate": "14/06/2020 00:00:00",
    "endDate": "31/12/2020 23:59:59",
    "price": 3550
  }
}
```

**Response (404 Not Found):**

```json
{
  "errorMessage": "Producto no encontrado",
  "data": null
}
```

## 🧪 Testing

### Ejecutar Tests

```bash
# Ejecutar todos los tests
mvn test

# Ejecutar test específico
mvn test -Dtest=ProductsApiApplicationTests

# Con cobertura de código
mvn test jacoco:report
```

### Cobertura

- **Mínimo requerido**: 80%
- **Reporte**: `target/site/jacoco/index.html`

### Casos de Prueba

El proyecto incluye 5 escenarios de test que validan:

| Test   | Hora  | Día | Producto | Brand    | Propósito                                  |
|--------|-------|-----|----------|----------|--------------------------------------------|
| Test 1 | 10:00 | 14  | 35455    | 1 (ZARA) | Búsqueda exitosa en horario activo         |
| Test 2 | 16:00 | 14  | 35455    | 1 (ZARA) | Búsqueda exitosa en horario diferente      |
| Test 3 | 21:00 | 14  | 35455    | 1 (ZARA) | Búsqueda en horario nocturno               |
| Test 4 | 10:00 | 15  | 35455    | 1 (ZARA) | Búsqueda al día siguiente                  |
| Test 5 | 21:00 | 16  | 35455    | 1 (ZARA) | Búsqueda con validación temporal extendida |

## 🏗️ Arquitectura

El proyecto implementa una **arquitectura hexagonal simplificada** diseñada para ser mantenible, escalable y fácil de
evolucionar sin complejidad innecesaria de módulos separados.

### Estructura de Carpetas

```
products-api/
├── src/
│   ├── main/java/com/products/
│   │   ├── products_api/                  # Punto de entrada
│   │   │   └── ProductsApiApplication.java
│   │   │
│   │   ├── priadapter/                    # Adaptadores Primarios (Entrada)
│   │   │   ├── adapter/
│   │   │   │   └── ControllerProducts.java    # REST Controller
│   │   │   ├── mapper/
│   │   │   │   └── ProductsPriMapper.java     # DTO ↔ Domain
│   │   │   └── model/
│   │   │       ├── request/
│   │   │       │   └── ProductFilterRequestDto.java
│   │   │       └── response/
│   │   │           ├── BaseResponse.java
│   │   │           └── ProductResponseDto.java
│   │   │
│   │   ├── application/                   # Núcleo de Negocio
│   │   │   ├── service/
│   │   │   │   └── ProductsUseCaseImpl.java    # Lógica de negocio
│   │   │   ├── ports/
│   │   │   │   ├── primary/
│   │   │   │   │   └── ProductsUseCase.java     # Puerto entrada
│   │   │   │   └── secondary/
│   │   │   │       └── ProductsRepository.java  # Puerto salida
│   │   │   ├── model/
│   │   │   │   ├── co/
│   │   │   │   │   ├── ProductCo.java
│   │   │   │   │   └── ProductsCo.java
│   │   │   │   └── dto/
│   │   │   │       ├── ProductDto.java
│   │   │   │       └── ProductFilterDto.java
│   │   │   ├── mapper/
│   │   │   │   └── ProductsMapper.java         # Domain ↔ DTO
│   │   │   ├── exceptions/
│   │   │   │   └── DbException.java
│   │   │   └── utils/
│   │   │       └── Utils.java
│   │   │
│   │   └── secadapter/                    # Adaptadores Secundarios (Salida)
│   │       ├── repository/
│   │       │   └── ProductsRepositoryImpl.java  # Implementación JDBC
│   │       ├── mapper/
│   │       │   └── rowmapper/
│   │       │       ├── ProductsRowMapper.java
│   │       │       └── ProductsSecMapper.java
│   │       └── model/
│   │           └── ProductEntity.java         # Entity JDBC
│   │
│   ├── main/resources/
│   │   ├── schema.sql
│   │   ├── data.sql
│   │   ├── application.yml
│   │   └── application-test.yml
│   │
│   └── test/java/com/products/
│       ├── priadapter/adapter/
│       │   ├── ControllerProductTest.java (5 tests)
│       │   └── mocks/ProductMocks.java
│       ├── application/service/
│       │   ├── ProductsUseCaseImplTest.java (4 tests)
│       │   └── mocks/ProductDtoMocks.java
│       └── secadapter/repository/
│           ├── ProductsRepositoryImplTest.java (8 unit tests)
│           ├── ProductsRepositoryImplIntegrationTest.java (12 integration tests)
│           └── mocks/ProductEntityMocks.java
│
├── pom.xml
├── README.md
├── TESTING.md
└── .gitignore
```

### Capas Arquitectónicas

#### 1️⃣ **Adaptadores Primarios (PRI-ADAPTER)**

*Punto de entrada del sistema - Expone la funcionalidad*

- **Responsabilidad**: Recibir peticiones HTTP y traducirlas al dominio
- **Componentes**:
    - `ControllerProducts`: REST endpoint POST /products/search
    - `ProductsPriMapper`: Convierte DTO ↔ Domain Objects
    - `ProductFilterRequestDto`: DTO de entrada
    - `ProductResponseDto`: DTO de salida
    - `BaseResponse`: Envelope patrón para respuestas

**Ventajas**:

- Desacoplamiento de la API REST
- Fácil cambio a GraphQL u otro protocolo en el futuro
- DTOs específicos del contrato API

#### 2️⃣ **Aplicación & Dominio (APPLICATION)**

*Corazón del negocio - Lógica pura e independiente de frameworks*

- **Responsabilidad**: Orquestar la lógica de negocio (selección de prioridad)
- **Componentes**:
    - `ProductsUseCaseImpl`: Implementación del caso de uso
    - `ProductsUseCase`: Puerto primario (interfaz)
    - `ProductsCo`, `ProductCo`: Objetos de negocio
    - `ProductDto`, `ProductFilterDto`: DTOs internos
    - `ProductsRepository`: Puerto secundario (interfaz)
    - `ProductsMapper`: Transformaciones internas
    - `Utils`: Utilidades (conversión de fechas, etc.)

**Ventajas**:

- Independencia de frameworks (testeable sin contexto Spring)
- Reglas de negocio centralizadas
- Lógica de selección de prioridad clara y mantenible

#### 3️⃣ **Adaptadores Secundarios (SEC-ADAPTER)**

*Punto de salida del sistema - Implementaciones técnicas*

- **Responsabilidad**: Implementar acceso a datos mediante JDBC
- **Componentes**:
    - `ProductsRepositoryImpl`: Implementación de ProductsRepository
    - `ProductEntity`: Mapeo de resultados JDBC
    - `ProductsRowMapper`: Mapeo automático ResultSet → Entity
    - `ProductsSecMapper`: Convierte Entity → DTO

**Ventajas**:

- Aisla la complejidad de JDBC
- Fácil cambiar de BD (H2 → MySQL → PostgreSQL, etc.)
- Queries SQL optimizadas con índices

### Flujo de Datos (Hexagonal)

```
┌─────────────────────────────────────────────────────────┐
│         POST /products/search (JSON)                     │
└──────────────────────┬──────────────────────────────────┘
                       │
                       ▼
        ┌──────────────────────────────────┐
        │   PRI-ADAPTER                    │
        │ ControllerProducts               │
        │ + ProductsPriMapper              │
        │ + ProductFilterRequestDto        │
        └──────────────┬────────────────────┘
                       │ (ProductFilterDto)
                       ▼
        ┌──────────────────────────────────┐
        │   APPLICATION (CORE)             │
        │ ProductsUseCaseImpl.              │
        │   getProductByFilter()           │
        │                                  │
        │ 1. Llamar repositorio            │
        │ 2. Seleccionar mayor prioridad   │
        │ 3. Retornar ProductDto           │
        └──────────────┬────────────────────┘
                       │ (ProductFilterDto)
                       ▼
        ┌──────────────────────────────────┐
        │   SEC-ADAPTER                    │
        │ ProductsRepositoryImpl            │
        │                                  │
        │ Construir SQL dinámico:          │
        │ SELECT * FROM PRICES             │
        │ WHERE BRAND_ID = ?               │
        │   AND PRODUCT_ID = ?             │
        │   AND applicationDate BETWEEN    │
        │       START_DATE AND END_DATE    │
        └──────────────┬────────────────────┘
                       │
                       ▼
        ┌──────────────────────────────────┐
        │   DATABASE (H2 / MySQL)          │
        │ Ejecutar query SQL JDBC          │
        │ (con índice IDX_PRICES_DATES)    │
        └──────────────┬────────────────────┘
                       │
                       ▼
        ┌──────────────────────────────────┐
        │   SEC-ADAPTER (ResultSet)        │
        │ ProductsRowMapper                │
        │ ProductEntity[] → List<Entity>   │
        └──────────────┬────────────────────┘
                       │
                       ▼
        ┌──────────────────────────────────┐
        │   SEC-ADAPTER                    │
        │ ProductsSecMapper                │
        │ Entity[] → List<ProductDto>      │
        └──────────────┬────────────────────┘
                       │ (List<ProductDto>)
                       ▼
        ┌──────────────────────────────────┐
        │   APPLICATION (CORE)             │
        │ ProductsCo.                      │
        │   getHighestPriorityProduct()    │
        │ Selecciona mayor prioridad       │
        └──────────────┬────────────────────┘
                       │ (ProductDto)
                       ▼
        ┌──────────────────────────────────┐
        │   PRI-ADAPTER                    │
        │ ProductsPriMapper                │
        │ ProductDto → ProductResponseDto  │
        │ + BaseResponse envelope          │
        └──────────────┬────────────────────┘
                       │
                       ▼
        ┌──────────────────────────────────┐
        │   HTTP RESPONSE 200 (JSON)       │
        │   BaseResponse<ProductResponseDto>│
        │ {                                │
        │   errorMessage: "",              │
        │   data: { ... }                  │
        │ }                                │
        └──────────────────────────────────┘
```

### Principios Arquitectónicos

| Principio                           | Implementación                                     |
|-------------------------------------|----------------------------------------------------|
| **Inversión de Dependencias**       | Interfaces en `application/ports`                  |
| **Separación de Responsabilidades** | Cada capa tiene un rol claro                       |
| **Testabilidad**                    | Domain layer sin dependencias de Spring            |
| **Escalabilidad**                   | Fácil agregar nuevos adaptadores sin tocar el core |
| **Mantenibilidad**                  | Un único módulo = menos complejidad, más cohesión  |

### Por qué un Único Módulo Maven

✅ **Ventajas**:

- Menos overhead de configuración
- Compartir dependencias más simple
- Build y deploy más rápido
- Gradle/Maven menos complicado

⚠️ **Cuando considerar módulos separados**:

- 500+ clases de código
- Equipos de múltiples perfiles trabajando en paralelo
- Necesidad de versionado independiente

## 🔧 Decisiones Técnicas

### Framework: Spring Boot

- **Ventajas**: Ecosistema robusto, comunidad activa, configuración sensata por defecto
- **Versión mínima**: 3.0+
- **Uso**: Inyección de dependencias, configuración, web

### Testing: 29 Tests (Unit + Integration)

- **Unit Tests**: 17 tests con mocks (rápidos, ~200ms)
- **Integration Tests**: 12 tests con BD H2 real (realistas, ~8s)
- **Cobertura**: 100% de métodos críticos
- **Documentación**: Ver [`TESTING.md`](TESTING.md) para guía completa

Quick start:

```bash
mvn clean test                    # Todos los tests (29)
mvn test -Dtest=\!*IntegrationTest  # Solo unit tests (rápido)
mvn test -Dtest=*IntegrationTest    # Solo integration tests
```

### Serialización: Gson

- **Uso**: Conversión JSON ↔ Objetos Java
- **Ventajas**: Flexible, menos verbose que Jackson en algunos casos

### Respuestas: Envelope Pattern (BaseResponse)

```java
public class BaseResponse<T> {
  private T data;
  private boolean success;
  private String message;
}
```

**Beneficios**:

- Respuestas consistentes
- Información de error centralizada
- Facilita versionado de API

### Arquitectura Hexagonal Simplificada

- **Razón**: Balanceamos flexibilidad con pragmatismo
- **Sin módulos separados**: Evita complejidad innecesaria en fase inicial
- **Puertos/Adaptadores claros**: Escalable sin refactor traumático

## 🔐 Seguridad

### Validación de Entrada

- ✅ Validación de parámetros en controller
- ✅ Manejo de excepciones centralizado
- ✅ Respuestas tipificadas evitan exposición de detalles internos

### Recomendaciones Futuras

- [ ] Integrar Spring Security para autenticación
- [ ] Agregar CORS según necesidades
- [ ] Implementar rate limiting
- [ ] Auditoría de cambios en base de datos

## 👥 Contribuir

### Workflow de Desarrollo

1. **Crear rama feature**:
   ```bash
   git checkout -b feature/nombre-descriptivo
   ```

2. **Hacer cambios** siguiendo estándares de código

3. **Validar antes de commit**:
   ```bash
   mvn clean verify
   ```

4. **Commit con mensaje descriptivo**:
   ```bash
   git commit -m "feat: descripción clara del cambio"
   ```

5. **Push y Pull Request**:
   ```bash
   git push origin feature/nombre-descriptivo
   ```

### Estándares de Código

| Aspecto           | Estándar                                       |
|-------------------|------------------------------------------------|
| **Naming**        | camelCase variables/métodos, PascalCase clases |
| **Cobertura**     | Mínimo 80% de código testeado                  |
| **Líneas**        | Máximo 120 caracteres por línea                |
| **Métodos**       | Máximo 20 líneas de código                     |
| **Documentación** | JavaDoc para métodos públicos                  |

### Guía de Desarrollo por Capa

#### Agregar Nuevo Endpoint

1. Crear `controller` en `priadapter/adapter/`
2. Crear `RequestDto` en `priadapter/model/request/`
3. Crear `ResponseDto` en `priadapter/model/response/`
4. Crear `Mapper` en `priadapter/mapper/`
5. Crear método en `ProductService` en `application/service/`
6. Crear `Entity` en `secadapter/model/`
7. Crear `RepositoryImpl` en `secadapter/repository/`
8. Agregar tests en `/test/`

#### Agregar Nueva Regla de Negocio

1. Implementar en `application/service/ProductService`
2. Crear validador si es necesario en `application/utils/`
3. Lanzar excepciones en `application/exceptions/`
4. Testear sin contexto Spring

## 📚 Recursos Adicionales

- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [JUnit 5 User Guide](https://junit.org/junit5/docs/current/user-guide/)
- [Arquitectura Hexagonal por Alistair Cockburn](https://www.google.com/search?q=hexagonal+architecture)
- [Clean Code by Robert C. Martin](https://www.amazon.com/Clean-Code-Handbook-Software-Craftsmanship/dp/0132350882)

## 📄 Licencia

MIT License - Ver archivo LICENSE para detalles

---

**Última actualización**: Febrero 2025

**Mantenedor**: Equipo de Tecnología

Para preguntas o soporte, contactar al equipo de desarrollo.

