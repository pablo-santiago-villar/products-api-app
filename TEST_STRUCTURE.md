# Estructura de Tests - Products API

## 📋 Descripción General

Este proyecto incluye una estructura completa de tests unitarios organizados por capas siguiendo la arquitectura
hexagonal:

```
src/test/java/
├── com/products/priadapter/adapter/
│   ├── ControllerProductTest.java       (Test del Controller)
│   └── mocks/
│       └── ProductMocks.java             (Mocks para el Controller)
├── com/products/application/service/
│   ├── ProductsUseCaseImplTest.java     (Test del caso de uso)
│   └── mocks/
│       └── ProductDtoMocks.java         (Mocks para DTOs)
└── com/products/secadapter/repository/
    ├── ProductsRepositoryImplTest.java  (Test del repositorio)
    └── mocks/
        └── ProductEntityMocks.java      (Mocks para entidades)
```

## 🏗️ Capas Testeadas

### 1. **Capa de Presentación (priadapter)**

**Clase**: `ControllerProductTest.java`

Pruebas del endpoint REST `/products/search`:

- ✅ Test 1: Consulta a las 10:00 del 14 (prioridad 0)
- ✅ Test 2: Consulta a las 16:00 del 14 (mayor prioridad)
- ✅ Test 3: Consulta a las 21:00 del 14 (prioridad 0)
- ✅ Test 4: Consulta a las 10:00 del 15 (prioridad 1)
- ✅ Test 5: Consulta a las 21:00 del 16 (prioridad 1)

**Mocks utilizados**: `ProductMocks.java`

- Crea DTOs de solicitud (`ProductFilterRequestDto`)
- Define respuestas esperadas (`ProductResponseDto`)

---

### 2. **Capa de Aplicación (application)**

**Clase**: `ProductsUseCaseImplTest.java`

Pruebas unitarias de la lógica de negocio:

- ✅ Test 1: Retorna producto con menor prioridad cuando solo hay uno
- ✅ Test 2: Retorna producto con mayor prioridad cuando hay múltiples
- ✅ Test 3: Retorna null cuando no hay productos
- ✅ Test 4: Verifica que se llama al repositorio con el filtro correcto

**Tecnologías**:

- Mockito para simular el repositorio y mapper
- Inyección de dependencias con `@InjectMocks`

**Mocks utilizados**: `ProductDtoMocks.java`

- Crea filtros de búsqueda (`ProductFilterDto`)
- Genera DTOs de productos con datos de test
- Proporciona listas de productos por escenario de test

---

### 3. **Capa de Persistencia (secadapter)**

**Clase**: `ProductsRepositoryImplTest.java`

Pruebas unitarias del acceso a datos:

- ✅ Test 1: Retorna lista de productos cuando se encuentran registros
- ✅ Test 2: Retorna múltiples productos cuando existen varios
- ✅ Test 3: Retorna lista vacía cuando no hay productos
- ✅ Test 4: Filtra correctamente por brandId
- ✅ Test 5: Filtra correctamente por productId
- ✅ Test 6: Lanza `DbException.BadExecution` ante errores
- ✅ Test 7: Construye correctamente la query SQL
- ✅ Test 8: Mapea correctamente entidades a DTOs

**Tecnologías**:

- Mockito para simular `NamedParameterJdbcTemplate` y el mapper
- Validación de llamadas al template JDBC

**Mocks utilizados**: `ProductEntityMocks.java`

- Crea entidades de producto (`ProductEntity`)
- Genera listas de entidades por escenario
- Convierte entidades a DTOs

---

## 🧪 Casos de Test por Capa

### Flujo Completo de un Test

```
ControllerProductTest
  ↓ (solicitud HTTP)
ProductsUseCaseImplTest
  ↓ (lógica de negocio)
ProductsRepositoryImplTest
  ↓ (consulta SQL)
Base de datos H2
```

### Ejemplo: Test 2 (16:00 del 14)

**Entrada**:

```json
{
  "productId": 35455,
  "brandId": 1,
  "applicationDate": "2020-06-14T16:00:00"
}
```

**Procesamiento por capas**:

1. **Controller**: Valida entrada y mapea a `ProductFilterDto`
2. **Caso de Uso**: Consulta repositorio y selecciona mayor prioridad
3. **Repositorio**: Ejecuta query SQL con rango de fechas
4. **BD**: Retorna productos donde fecha está entre START_DATE y END_DATE

**Salida esperada**:

```json
{
  "productId": 35455,
  "brandId": 1,
  "priceList": 2,
  "price": 25
}
```

---

## 📝 Estructura de Mocks

### ProductMocks (Controller)

Proporciona datos de entrada y salida esperada:

```java
ProductFilterRequestDto getTest2Request()

ProductResponseDto getTest2Response()
```

### ProductDtoMocks (Application)

Crea DTOs de filtro y listas de productos:

```java
ProductFilterDto createProductFilterDto(...)

List<ProductDto> getTest2MockProducts()
```

### ProductEntityMocks (Repository)

Crea entidades y mapea a DTOs:

```java
ProductEntity createProductEntity(...)

List<ProductEntity> getTest2MockEntities()

ProductDto createProductDtoFromEntity(...)
```

---

## 🚀 Ejecución de Tests

### Ejecutar todos los tests:

```bash
mvn test
```

### Ejecutar tests de una capa específica:

```bash
# Solo controller tests
mvn test -Dtest=ControllerProductTest

# Solo service tests
mvn test -Dtest=ProductsUseCaseImplTest

# Solo repository tests
mvn test -Dtest=ProductsRepositoryImplTest
```

### Ejecutar un test específico:

```bash
mvn test -Dtest=ProductsUseCaseImplTest#test2_getProductByFilter_ShouldReturnProductWithHighestPriority
```

---

## 📊 Cobertura de Tests

| Capa        | Clase                  | Métodos | Tests | Cobertura |
|-------------|------------------------|---------|-------|-----------|
| priadapter  | ControllerProducts     | 1       | 5     | 100%      |
| application | ProductsUseCaseImpl    | 1       | 4     | 100%      |
| secadapter  | ProductsRepositoryImpl | 1       | 8     | 100%      |

**Total**: 17 tests unitarios

---

## 🔧 Dependencias de Test

```xml
<!-- JUnit 5 -->
<dependency>
  <groupId>org.junit.jupiter</groupId>
  <artifactId>junit-jupiter</artifactId>
</dependency>

    <!-- Mockito -->
<dependency>
<groupId>org.mockito</groupId>
<artifactId>mockito-core</artifactId>
<scope>test</scope>
</dependency>
<dependency>
<groupId>org.mockito</groupId>
<artifactId>mockito-junit-jupiter</artifactId>
<scope>test</scope>
</dependency>

    <!-- Spring Boot Test -->
<dependency>
<groupId>org.springframework.boot</groupId>
<artifactId>spring-boot-starter-test</artifactId>
<scope>test</scope>
</dependency>
```

---

## 📚 Convenciones Seguidas

1. **Nombres de test**: Formato `test{Número}_metodoPrueba_expectedResult`
2. **Estructura AAA**: Arrange, Act, Assert
3. **Mocks por capa**: Cada capa tiene sus propios mocks en su estructura espejo
4. **Nombres de archivo**: `{Clase}Test.java` y `{Clase}Mocks.java`
5. **Paquetes**: Espejo exacto de `src/main` en `src/test`

---

## ✅ Checklist de Calidad

- ✅ Tests unitarios para todas las capas
- ✅ Mocks organizados por capa
- ✅ Cobertura del 100% en métodos críticos
- ✅ Validación de casos de éxito y error
- ✅ Aislamiento de dependencias con Mockito
- ✅ Nombres descriptivos y DisplayName en tests
- ✅ Estructura de carpetas espejo en test y main

---

## 🎯 Próximas Mejoras Posibles

- Agregar tests de integración
- Aumentar cobertura con tests de edge cases
- Implementar tests de rendimiento
- Agregar tests de validación de entrada
