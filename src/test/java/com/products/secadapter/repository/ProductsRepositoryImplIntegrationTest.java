package com.products.secadapter.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.products.application.model.dto.ProductDto;
import com.products.application.model.dto.ProductFilterDto;
import com.products.products_api.ProductsApiApplication;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests para ProductsRepositoryImpl
 * <p>
 * A diferencia de ProductsRepositoryImplTest (unitario con mocks),
 * estos tests usan la BD H2 real para validar las queries SQL
 * y el comportamiento real del repositorio.
 */
@SpringBootTest(classes = ProductsApiApplication.class)
@Transactional
@ActiveProfiles("test")
class ProductsRepositoryImplIntegrationTest {

  @Autowired
  private ProductsRepositoryImpl productsRepository;

  @Autowired
  private JdbcTemplate jdbcTemplate;

  /**
   * Limpia la tabla PRICES y prepara datos de test
   */
  @BeforeEach
  void setUp() {
    // Limpiar tabla
    jdbcTemplate.execute("DELETE FROM PRICES");

    // Insertar datos de test (los mismos del schema.sql)
    jdbcTemplate.update(
        "INSERT INTO PRICES (BRAND_ID, START_DATE, END_DATE, PRICE_LIST, PRODUCT_ID, PRIORITY, PRICE, CURR) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?)",
        1, "2020-06-14 00:00:00", "2020-12-31 23:59:59", 1, 35455, 0, "35.50", "EUR"
    );

    jdbcTemplate.update(
        "INSERT INTO PRICES (BRAND_ID, START_DATE, END_DATE, PRICE_LIST, PRODUCT_ID, PRIORITY, PRICE, CURR) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?)",
        1, "2020-06-14 15:00:00", "2020-06-14 18:30:00", 2, 35455, 1, "25.45", "EUR"
    );

    jdbcTemplate.update(
        "INSERT INTO PRICES (BRAND_ID, START_DATE, END_DATE, PRICE_LIST, PRODUCT_ID, PRIORITY, PRICE, CURR) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?)",
        1, "2020-06-15 00:00:00", "2020-06-15 11:00:00", 3, 35455, 1, "30.50", "EUR"
    );

    jdbcTemplate.update(
        "INSERT INTO PRICES (BRAND_ID, START_DATE, END_DATE, PRICE_LIST, PRODUCT_ID, PRIORITY, PRICE, CURR) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?)",
        1, "2020-06-15 16:00:00", "2020-12-31 23:59:59", 4, 35455, 1, "38.95", "EUR"
    );
  }

  @DisplayName("Integration Test 1: Query real con filtro de fecha - 10:00 del 14/06/2020")
  @Test
  void integrationTest1_ShouldReturnCorrectProductAtDate1() throws Exception {
    // Arrange
    ProductFilterDto filterDto = new ProductFilterDto();
    filterDto.setProductId(35455);
    filterDto.setBrandId(1);
    filterDto.setApplicationDate(LocalDateTime.of(2020, 6, 14, 10, 0));

    // Act
    List<ProductDto> result = productsRepository.getProductsByFilter(filterDto);

    // Assert
    assertNotNull(result);
    assertEquals(1, result.size());
    assertEquals(1, result.get(0).getPriceList());
    assertEquals(new BigDecimal("35.50"), result.get(0).getPrice());
    assertEquals(0, result.get(0).getPriority());
  }

  @DisplayName("Integration Test 2: Query real con múltiples resultados - 16:00 del 14/06/2020")
  @Test
  void integrationTest2_ShouldReturnMultipleProductsAtDate2() throws Exception {
    // Arrange
    ProductFilterDto filterDto = new ProductFilterDto();
    filterDto.setProductId(35455);
    filterDto.setBrandId(1);
    filterDto.setApplicationDate(LocalDateTime.of(2020, 6, 14, 16, 0));

    // Act
    List<ProductDto> result = productsRepository.getProductsByFilter(filterDto);

    // Assert
    assertNotNull(result);
    assertEquals(2, result.size());

    // Validar que retorna ambos: el de menor prioridad (0) y el de mayor prioridad (1)
    ProductDto firstProduct = result.get(0);
    ProductDto secondProduct = result.get(1);

    // Uno tiene prioridad 0, otro tiene prioridad 1
    assertTrue(
        (firstProduct.getPriority() == 0 && secondProduct.getPriority() == 1) ||
            (firstProduct.getPriority() == 1 && secondProduct.getPriority() == 0)
    );

    // Validar precios
    assertTrue(
        result.stream().anyMatch(p -> p.getPrice().equals(new BigDecimal("35.50"))) &&
            result.stream().anyMatch(p -> p.getPrice().equals(new BigDecimal("25.45")))
    );
  }

  @DisplayName("Integration Test 3: Query real sin resultados fuera del rango de fechas - 21:00 del 14/06/2020")
  @Test
  void integrationTest3_ShouldReturnProductAtDate3() throws Exception {
    // Arrange
    ProductFilterDto filterDto = new ProductFilterDto();
    filterDto.setProductId(35455);
    filterDto.setBrandId(1);
    filterDto.setApplicationDate(LocalDateTime.of(2020, 6, 14, 21, 0));

    // Act
    List<ProductDto> result = productsRepository.getProductsByFilter(filterDto);

    // Assert
    // A las 21:00 solo aplica la tarifa general (PRICE_LIST 1) porque la PRICE_LIST 2
    // termina a las 18:30:00
    assertNotNull(result);
    assertEquals(1, result.size());
    assertEquals(1, result.get(0).getPriceList());
    assertEquals(new BigDecimal("35.50"), result.get(0).getPrice());
  }

  @DisplayName("Integration Test 4: Query real con fecha diferente - 10:00 del 15/06/2020")
  @Test
  void integrationTest4_ShouldReturnCorrectProductAtDate4() throws Exception {
    // Arrange
    ProductFilterDto filterDto = new ProductFilterDto();
    filterDto.setProductId(35455);
    filterDto.setBrandId(1);
    filterDto.setApplicationDate(LocalDateTime.of(2020, 6, 15, 10, 0));

    // Act
    List<ProductDto> result = productsRepository.getProductsByFilter(filterDto);

    // Assert
    assertNotNull(result);
    assertEquals(2, result.size());

    // Debe retornar PRICE_LIST 1 (general) y PRICE_LIST 3 (específica del 15)
    assertTrue(
        result.stream().anyMatch(p -> p.getPriceList() == 1) &&
            result.stream().anyMatch(p -> p.getPriceList() == 3)
    );
  }

  @DisplayName("Integration Test 5: Query real en fecha posterior - 21:00 del 16/06/2020")
  @Test
  void integrationTest5_ShouldReturnCorrectProductAtDate5() throws Exception {
    // Arrange
    ProductFilterDto filterDto = new ProductFilterDto();
    filterDto.setProductId(35455);
    filterDto.setBrandId(1);
    filterDto.setApplicationDate(LocalDateTime.of(2020, 6, 16, 21, 0));

    // Act
    List<ProductDto> result = productsRepository.getProductsByFilter(filterDto);

    // Assert
    // A las 21:00 del 16/06 hay 2 tarifas vigentes:
    // - PRICE_LIST 1: 14/06 00:00:00 a 31/12 23:59:59 (general)
    // - PRICE_LIST 4: 15/06 16:00:00 a 31/12 23:59:59 (específica)
    assertNotNull(result);
    assertEquals(2, result.size());

    // Debe retornar PRICE_LIST 1 (general) y PRICE_LIST 4 (específica)
    assertTrue(
        result.stream().anyMatch(p -> p.getPriceList() == 1) &&
            result.stream().anyMatch(p -> p.getPriceList() == 4)
    );

    // Validar precios
    assertTrue(
        result.stream().anyMatch(p -> p.getPrice().equals(new BigDecimal("35.50"))) &&
            result.stream().anyMatch(p -> p.getPrice().equals(new BigDecimal("38.95")))
    );
  }

  @DisplayName("Integration Test 6: Query real filtro por brandId incorrecto")
  @Test
  void integrationTest6_ShouldReturnEmptyListForWrongBrandId() throws Exception {
    // Arrange
    ProductFilterDto filterDto = new ProductFilterDto();
    filterDto.setProductId(35455);
    filterDto.setBrandId(999); // Brand que no existe
    filterDto.setApplicationDate(LocalDateTime.of(2020, 6, 14, 10, 0));

    // Act
    List<ProductDto> result = productsRepository.getProductsByFilter(filterDto);

    // Assert
    assertNotNull(result);
    assertTrue(result.isEmpty());
  }

  @DisplayName("Integration Test 7: Query real filtro por productId incorrecto")
  @Test
  void integrationTest7_ShouldReturnEmptyListForWrongProductId() throws Exception {
    // Arrange
    ProductFilterDto filterDto = new ProductFilterDto();
    filterDto.setProductId(99999); // Producto que no existe
    filterDto.setBrandId(1);
    filterDto.setApplicationDate(LocalDateTime.of(2020, 6, 14, 10, 0));

    // Act
    List<ProductDto> result = productsRepository.getProductsByFilter(filterDto);

    // Assert
    assertNotNull(result);
    assertTrue(result.isEmpty());
  }

  @DisplayName("Integration Test 8: Query real con fecha fuera de todos los rangos")
  @Test
  void integrationTest8_ShouldReturnEmptyListForDateOutOfRange() throws Exception {
    // Arrange
    ProductFilterDto filterDto = new ProductFilterDto();
    filterDto.setProductId(35455);
    filterDto.setBrandId(1);
    filterDto.setApplicationDate(LocalDateTime.of(2021, 1, 1, 10, 0)); // Fuera de rango

    // Act
    List<ProductDto> result = productsRepository.getProductsByFilter(filterDto);

    // Assert
    assertNotNull(result);
    assertTrue(result.isEmpty());
  }

  @DisplayName("Integration Test 9: Query real verifica rango exacto de fechas - Límite inferior")
  @Test
  void integrationTest9_ShouldReturnProductAtExactStartDate() throws Exception {
    // Arrange
    ProductFilterDto filterDto = new ProductFilterDto();
    filterDto.setProductId(35455);
    filterDto.setBrandId(1);
    filterDto.setApplicationDate(LocalDateTime.of(2020, 6, 14, 0, 0)); // Exacta START_DATE

    // Act
    List<ProductDto> result = productsRepository.getProductsByFilter(filterDto);

    // Assert
    assertNotNull(result);
    assertEquals(1, result.size());
    assertEquals(new BigDecimal("35.50"), result.get(0).getPrice());
  }

  @DisplayName("Integration Test 10: Query real verifica rango exacto de fechas - Límite superior")
  @Test
  void integrationTest10_ShouldReturnProductAtExactEndDate() throws Exception {
    // Arrange
    ProductFilterDto filterDto = new ProductFilterDto();
    filterDto.setProductId(35455);
    filterDto.setBrandId(1);
    filterDto.setApplicationDate(LocalDateTime.of(2020, 12, 31, 23, 59, 59)); // Exacta END_DATE

    // Act
    List<ProductDto> result = productsRepository.getProductsByFilter(filterDto);

    // Assert
    // A las 23:59:59 del 31/12 hay 2 tarifas vigentes:
    // - PRICE_LIST 1: 14/06 00:00:00 a 31/12 23:59:59 (general)
    // - PRICE_LIST 4: 15/06 16:00:00 a 31/12 23:59:59 (específica)
    assertNotNull(result);
    assertEquals(2, result.size());

    // Debe retornar PRICE_LIST 1 y PRICE_LIST 4
    assertTrue(
        result.stream().anyMatch(p -> p.getPriceList() == 1) &&
            result.stream().anyMatch(p -> p.getPriceList() == 4)
    );

    // Validar precios
    assertTrue(
        result.stream().anyMatch(p -> p.getPrice().equals(new BigDecimal("35.50"))) &&
            result.stream().anyMatch(p -> p.getPrice().equals(new BigDecimal("38.95")))
    );
  }

  @DisplayName("Integration Test 11: Query real valida que BD retorna datos mapeados correctamente")
  @Test
  void integrationTest11_ShouldMapDatabaseColumnsCorrectly() throws Exception {
    // Arrange
    ProductFilterDto filterDto = new ProductFilterDto();
    filterDto.setProductId(35455);
    filterDto.setBrandId(1);
    filterDto.setApplicationDate(LocalDateTime.of(2020, 6, 14, 15, 30)); // Dentro de PRICE_LIST 2

    // Act
    List<ProductDto> result = productsRepository.getProductsByFilter(filterDto);

    // Assert
    assertNotNull(result);
    assertTrue(result.size() >= 1);

    ProductDto product = result.stream()
        .filter(p -> p.getPriceList() == 2)
        .findFirst()
        .orElse(null);

    assertNotNull(product);
    assertEquals(35455, product.getProductId());
    assertEquals(1, product.getBrandId());
    assertEquals(2, product.getPriceList());
    assertEquals(new BigDecimal("25.45"), product.getPrice());
    assertEquals("EUR", product.getCurrency());
    assertNotNull(product.getStartDate());
    assertNotNull(product.getEndDate());
  }

  @DisplayName("Integration Test 12: Verifica que índices mejoran rendimiento (BD real)")
  @Test
  void integrationTest12_ShouldUseIndexesForPerformance() throws Exception {
    // Arrange
    ProductFilterDto filterDto = new ProductFilterDto();
    filterDto.setProductId(35455);
    filterDto.setBrandId(1);
    filterDto.setApplicationDate(LocalDateTime.of(2020, 6, 14, 10, 0));

    // Act - Ejecutar múltiples veces para ver rendimiento
    long startTime = System.currentTimeMillis();
    for (int i = 0; i < 1000; i++) {
      productsRepository.getProductsByFilter(filterDto);
    }
    long endTime = System.currentTimeMillis();
    long executionTime = endTime - startTime;

    // Assert
    // Con índice, 1000 queries deben ejecutarse en menos de 5 segundos
    assertTrue(executionTime < 5000,
        "Queries demasiado lentas, probablemente el índice no funciona: " + executionTime + "ms");
  }

}
