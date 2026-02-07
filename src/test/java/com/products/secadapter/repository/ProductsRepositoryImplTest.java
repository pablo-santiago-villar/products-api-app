package com.products.secadapter.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.products.application.exceptions.DbException;
import com.products.application.model.dto.ProductDto;
import com.products.application.model.dto.ProductFilterDto;
import com.products.secadapter.mapper.rowmapper.ProductsRowMapper;
import com.products.secadapter.mapper.rowmapper.ProductsSecMapper;
import com.products.secadapter.model.ProductEntity;
import com.products.secadapter.repository.mocks.ProductEntityMocks;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

@ExtendWith(MockitoExtension.class)
class ProductsRepositoryImplTest {

  @Mock
  private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

  @Mock
  private ProductsSecMapper productsSecMapper;

  @InjectMocks
  private ProductsRepositoryImpl productsRepository;

  private ProductFilterDto filterDto;

  @BeforeEach
  void setUp() {
    filterDto = new ProductFilterDto();
    filterDto.setProductId(35455);
    filterDto.setBrandId(1);
    filterDto.setApplicationDate(LocalDateTime.of(2020, 6, 14, 10, 0));
  }

  @DisplayName("Test 1: Debe retornar lista de productos cuando se encuentra registros")
  @Test
  void test1_getProductsByFilter_ShouldReturnProductList() throws Exception {
    // Arrange
    List<ProductEntity> mockEntities = ProductEntityMocks.getTest1MockEntities();
    List<ProductDto> mockDtos = List.of(
        ProductEntityMocks.createProductDtoFromEntity(mockEntities.get(0))
    );

    when(namedParameterJdbcTemplate.query(anyString(), any(MapSqlParameterSource.class), any(ProductsRowMapper.class)))
        .thenReturn(mockEntities);
    when(productsSecMapper.toProductDto(mockEntities))
        .thenReturn(mockDtos);

    // Act
    List<ProductDto> result = productsRepository.getProductsByFilter(filterDto);

    // Assert
    assertNotNull(result);
    assertEquals(1, result.size());
    assertEquals(35455, result.get(0).getProductId());
    assertEquals(1, result.get(0).getBrandId());
    verify(namedParameterJdbcTemplate, times(1)).query(anyString(), any(MapSqlParameterSource.class),
        any(ProductsRowMapper.class));
  }

  @DisplayName("Test 2: Debe retornar múltiples productos cuando existen varios en rango de fechas")
  @Test
  void test2_getProductsByFilter_ShouldReturnMultipleProducts() throws Exception {
    // Arrange
    ProductFilterDto test2Filter = new ProductFilterDto();
    test2Filter.setProductId(35455);
    test2Filter.setBrandId(1);
    test2Filter.setApplicationDate(LocalDateTime.of(2020, 6, 14, 16, 0));

    List<ProductEntity> mockEntities = ProductEntityMocks.getTest2MockEntities();
    List<ProductDto> mockDtos = mockEntities.stream()
        .map(ProductEntityMocks::createProductDtoFromEntity)
        .toList();

    when(namedParameterJdbcTemplate.query(anyString(), any(MapSqlParameterSource.class), any(ProductsRowMapper.class)))
        .thenReturn(mockEntities);
    when(productsSecMapper.toProductDto(mockEntities))
        .thenReturn(mockDtos);

    // Act
    List<ProductDto> result = productsRepository.getProductsByFilter(test2Filter);

    // Assert
    assertNotNull(result);
    assertEquals(2, result.size());
    assertEquals(1, result.get(0).getPriceList());
    assertEquals(2, result.get(1).getPriceList());
    verify(namedParameterJdbcTemplate, times(1)).query(anyString(), any(MapSqlParameterSource.class),
        any(ProductsRowMapper.class));
  }

  @DisplayName("Test 3: Debe retornar lista vacía cuando no hay productos")
  @Test
  void test3_getProductsByFilter_ShouldReturnEmptyList() throws Exception {
    // Arrange
    List<ProductEntity> mockEntities = ProductEntityMocks.getEmptyEntities();
    List<ProductDto> mockDtos = List.of();

    when(namedParameterJdbcTemplate.query(anyString(), any(MapSqlParameterSource.class), any(ProductsRowMapper.class)))
        .thenReturn(mockEntities);
    when(productsSecMapper.toProductDto(mockEntities))
        .thenReturn(mockDtos);

    // Act
    List<ProductDto> result = productsRepository.getProductsByFilter(filterDto);

    // Assert
    assertNotNull(result);
    assertTrue(result.isEmpty());
    verify(namedParameterJdbcTemplate, times(1)).query(anyString(), any(MapSqlParameterSource.class),
        any(ProductsRowMapper.class));
  }

  @DisplayName("Test 4: Debe filtrar correctamente por brandId")
  @Test
  void test4_getProductsByFilter_ShouldFilterByBrandId() throws Exception {
    // Arrange
    ProductFilterDto brandFilter = new ProductFilterDto();
    brandFilter.setProductId(35455);
    brandFilter.setBrandId(2);
    brandFilter.setApplicationDate(LocalDateTime.of(2020, 6, 14, 10, 0));

    List<ProductEntity> mockEntities = List.of();
    List<ProductDto> mockDtos = List.of();

    when(namedParameterJdbcTemplate.query(anyString(), any(MapSqlParameterSource.class), any(ProductsRowMapper.class)))
        .thenReturn(mockEntities);
    when(productsSecMapper.toProductDto(mockEntities))
        .thenReturn(mockDtos);

    // Act
    List<ProductDto> result = productsRepository.getProductsByFilter(brandFilter);

    // Assert
    assertTrue(result.isEmpty());
    verify(namedParameterJdbcTemplate, times(1)).query(anyString(), any(MapSqlParameterSource.class),
        any(ProductsRowMapper.class));
  }

  @DisplayName("Test 5: Debe filtrar correctamente por productId")
  @Test
  void test5_getProductsByFilter_ShouldFilterByProductId() throws Exception {
    // Arrange
    ProductFilterDto productFilter = new ProductFilterDto();
    productFilter.setProductId(99999);
    productFilter.setBrandId(1);
    productFilter.setApplicationDate(LocalDateTime.of(2020, 6, 14, 10, 0));

    List<ProductEntity> mockEntities = List.of();
    List<ProductDto> mockDtos = List.of();

    when(namedParameterJdbcTemplate.query(anyString(), any(MapSqlParameterSource.class), any(ProductsRowMapper.class)))
        .thenReturn(mockEntities);
    when(productsSecMapper.toProductDto(mockEntities))
        .thenReturn(mockDtos);

    // Act
    List<ProductDto> result = productsRepository.getProductsByFilter(productFilter);

    // Assert
    assertTrue(result.isEmpty());
    verify(namedParameterJdbcTemplate, times(1)).query(anyString(), any(MapSqlParameterSource.class),
        any(ProductsRowMapper.class));
  }

  @DisplayName("Test 6: Debe lanzar DbException.BadExecution cuando ocurre un error")
  @Test
  void test6_getProductsByFilter_ShouldThrowBadExecutionException() throws Exception {
    // Arrange
    when(namedParameterJdbcTemplate.query(anyString(), any(MapSqlParameterSource.class), any(ProductsRowMapper.class)))
        .thenThrow(new RuntimeException("Database connection error"));

    // Act & Assert
    assertThrows(DbException.BadExecution.class, () -> {
      productsRepository.getProductsByFilter(filterDto);
    });
    verify(namedParameterJdbcTemplate, times(1)).query(anyString(), any(MapSqlParameterSource.class),
        any(ProductsRowMapper.class));
  }

  @DisplayName("Test 7: Debe construir correctamente la query con todos los filtros")
  @Test
  void test7_getProductsByFilter_ShouldBuildCorrectQuery() throws Exception {
    // Arrange
    List<ProductEntity> mockEntities = ProductEntityMocks.getTest1MockEntities();
    List<ProductDto> mockDtos = List.of(
        ProductEntityMocks.createProductDtoFromEntity(mockEntities.get(0))
    );

    when(namedParameterJdbcTemplate.query(anyString(), any(MapSqlParameterSource.class), any(ProductsRowMapper.class)))
        .thenReturn(mockEntities);
    when(productsSecMapper.toProductDto(mockEntities))
        .thenReturn(mockDtos);

    // Act
    List<ProductDto> result = productsRepository.getProductsByFilter(filterDto);

    // Assert
    assertNotNull(result);
    verify(namedParameterJdbcTemplate, times(1)).query(anyString(), any(MapSqlParameterSource.class),
        any(ProductsRowMapper.class));
  }

  @DisplayName("Test 8: Debe retornar DTOs correctamente mapeados desde entidades")
  @Test
  void test8_getProductsByFilter_ShouldMapEntitiesToDtos() throws Exception {
    // Arrange
    List<ProductEntity> mockEntities = ProductEntityMocks.getTest2MockEntities();
    ProductDto dto1 = ProductEntityMocks.createProductDtoFromEntity(mockEntities.get(0));
    ProductDto dto2 = ProductEntityMocks.createProductDtoFromEntity(mockEntities.get(1));
    List<ProductDto> mockDtos = List.of(dto1, dto2);

    when(namedParameterJdbcTemplate.query(anyString(), any(MapSqlParameterSource.class), any(ProductsRowMapper.class)))
        .thenReturn(mockEntities);
    when(productsSecMapper.toProductDto(mockEntities))
        .thenReturn(mockDtos);

    // Act
    List<ProductDto> result = productsRepository.getProductsByFilter(filterDto);

    // Assert
    assertEquals(2, result.size());
    assertEquals(new BigDecimal("35.50"), result.get(0).getPrice());
    assertEquals(new BigDecimal("25.45"), result.get(1).getPrice());
    verify(productsSecMapper, times(1)).toProductDto(mockEntities);
  }

}
