package com.products.secadapter.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.products.application.exceptions.DbException;
import com.products.application.model.dto.ProductDto;
import com.products.application.model.dto.ProductFilterDto;
import com.products.products_api.ProductsApiApplication;
import java.math.BigDecimal;
import java.time.LocalDateTime;
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
    ProductDto result = productsRepository.getHighestPriorityProductByFilters(filterDto);

    // Assert
    assertNotNull(result);
    assertEquals(1, result.getPriceList());
    assertEquals(new BigDecimal("35.50"), result.getPrice());
    assertEquals(0, result.getPriority());
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
    ProductDto result = productsRepository.getHighestPriorityProductByFilters(filterDto);

    // Assert
    // A las 21:00 solo aplica la tarifa general (PRICE_LIST 1) porque la PRICE_LIST 2
    // termina a las 18:30:00
    assertNotNull(result);
    assertEquals(1, result.getPriceList());
    assertEquals(new BigDecimal("35.50"), result.getPrice());
  }

  @DisplayName("Integration Test 6: Query real filtro por brandId incorrecto - debe lanzar NoData")
  @Test
  void integrationTest6_ShouldThrowNoDataForWrongBrandId() throws Exception {
    // Arrange
    ProductFilterDto filterDto = new ProductFilterDto();
    filterDto.setProductId(35455);
    filterDto.setBrandId(999); // Brand que no existe
    filterDto.setApplicationDate(LocalDateTime.of(2020, 6, 14, 10, 0));

    // Act & Assert
    assertThrows(DbException.NoData.class, () -> {
      productsRepository.getHighestPriorityProductByFilters(filterDto);
    });
  }

  @DisplayName("Integration Test 7: Query real filtro por productId incorrecto - debe lanzar NoData")
  @Test
  void integrationTest7_ShouldThrowNoDataForWrongProductId() throws Exception {
    // Arrange
    ProductFilterDto filterDto = new ProductFilterDto();
    filterDto.setProductId(99999); // Producto que no existe
    filterDto.setBrandId(1);
    filterDto.setApplicationDate(LocalDateTime.of(2020, 6, 14, 10, 0));

    // Act & Assert
    assertThrows(DbException.NoData.class, () -> {
      productsRepository.getHighestPriorityProductByFilters(filterDto);
    });
  }

  @DisplayName("Integration Test 8: Query real con fecha fuera de todos los rangos - debe lanzar NoData")
  @Test
  void integrationTest8_ShouldThrowNoDataForDateOutOfRange() throws Exception {
    // Arrange
    ProductFilterDto filterDto = new ProductFilterDto();
    filterDto.setProductId(35455);
    filterDto.setBrandId(1);
    filterDto.setApplicationDate(LocalDateTime.of(2021, 1, 1, 10, 0)); // Fuera de rango

    // Act & Assert
    assertThrows(DbException.NoData.class, () -> {
      productsRepository.getHighestPriorityProductByFilters(filterDto);
    });
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
    ProductDto result = productsRepository.getHighestPriorityProductByFilters(filterDto);

    // Assert
    assertNotNull(result);
    assertEquals(new BigDecimal("35.50"), result.getPrice());
  }

  @DisplayName("Integration Test 10: Query real verifica rango exacto de fechas - Límite superior (mayor prioridad)")
  @Test
  void integrationTest10_ShouldReturnHighestPriorityProductAtExactEndDate() throws Exception {
    // Arrange
    ProductFilterDto filterDto = new ProductFilterDto();
    filterDto.setProductId(35455);
    filterDto.setBrandId(1);
    filterDto.setApplicationDate(LocalDateTime.of(2020, 12, 31, 23, 59, 59)); // Exacta END_DATE

    // Act
    ProductDto result = productsRepository.getHighestPriorityProductByFilters(filterDto);

    // Assert
    // A las 23:59:59 del 31/12 hay 2 tarifas vigentes:
    // - PRICE_LIST 1: priority 0 (general)
    // - PRICE_LIST 4: priority 1 (específica) <- Esta debe ser la devuelta
    assertNotNull(result);
    assertEquals(4, result.getPriceList());
    assertEquals(new BigDecimal("38.95"), result.getPrice());
    assertEquals(1, result.getPriority());
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
    ProductDto result = productsRepository.getHighestPriorityProductByFilters(filterDto);

    // Assert
    // A las 15:30 hay 2 tarifas vigentes, pero PRICE_LIST 2 tiene mayor prioridad (1 > 0)
    assertNotNull(result);
    assertEquals(35455, result.getProductId());
    assertEquals(1, result.getBrandId());
    assertEquals(2, result.getPriceList());
    assertEquals(new BigDecimal("25.45"), result.getPrice());
    assertEquals("EUR", result.getCurrency());
    assertEquals(1, result.getPriority());
    assertNotNull(result.getStartDate());
    assertNotNull(result.getEndDate());
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
      productsRepository.getHighestPriorityProductByFilters(filterDto);
    }
    long endTime = System.currentTimeMillis();
    long executionTime = endTime - startTime;

    // Assert
    // Con índice, 1000 queries deben ejecutarse en menos de 5 segundos
    assertTrue(executionTime < 5000,
        "Queries demasiado lentas, probablemente el índice no funciona: " + executionTime + "ms");
  }

}
