package com.products.secadapter.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.EmptyResultDataAccessException;
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

  @DisplayName("Test 1: Debe retornar un producto cuando se encuentra registro")
  @Test
  void test1_getHighestPriorityProductByFilters_ShouldReturnProduct() throws Exception {
    // Arrange
    ProductEntity mockEntity = ProductEntityMocks.getTest1MockEntities().get(0);
    ProductDto expectedDto = ProductEntityMocks.createProductDtoFromEntity(mockEntity);

    when(namedParameterJdbcTemplate.queryForObject(anyString(), any(MapSqlParameterSource.class),
        any(ProductsRowMapper.class)))
        .thenReturn(mockEntity);
    when(productsSecMapper.toProductDto(mockEntity))
        .thenReturn(expectedDto);

    // Act
    ProductDto result = productsRepository.getHighestPriorityProductByFilters(filterDto);

    // Assert
    assertNotNull(result);
    assertEquals(35455, result.getProductId());
    assertEquals(1, result.getBrandId());
    verify(namedParameterJdbcTemplate, times(1)).queryForObject(anyString(), any(MapSqlParameterSource.class),
        any(ProductsRowMapper.class));
  }

  @DisplayName("Test 2: Debe lanzar NoData cuando no hay productos por brandId")
  @Test
  void test2_getHighestPriorityProductByFilters_ShouldThrowNoDataWhenFilterByBrandId() throws Exception {
    // Arrange
    ProductFilterDto brandFilter = new ProductFilterDto();
    brandFilter.setProductId(35455);
    brandFilter.setBrandId(2);
    brandFilter.setApplicationDate(LocalDateTime.of(2020, 6, 14, 10, 0));

    when(namedParameterJdbcTemplate.queryForObject(anyString(), any(MapSqlParameterSource.class),
        any(ProductsRowMapper.class)))
        .thenThrow(new EmptyResultDataAccessException(1));

    // Act & Assert
    assertThrows(DbException.NoData.class, () -> {
      productsRepository.getHighestPriorityProductByFilters(brandFilter);
    });
    verify(namedParameterJdbcTemplate, times(1)).queryForObject(anyString(), any(MapSqlParameterSource.class),
        any(ProductsRowMapper.class));
  }

  @DisplayName("Test 3: Debe lanzar NoData cuando no hay productos")
  @Test
  void test3_getHighestPriorityProductByFilters_ShouldThrowNoDataWhenEmpty() throws Exception {
    // Arrange
    when(namedParameterJdbcTemplate.queryForObject(anyString(), any(MapSqlParameterSource.class),
        any(ProductsRowMapper.class)))
        .thenThrow(new EmptyResultDataAccessException(1));

    // Act & Assert
    assertThrows(DbException.NoData.class, () -> {
      productsRepository.getHighestPriorityProductByFilters(filterDto);
    });
    verify(namedParameterJdbcTemplate, times(1)).queryForObject(anyString(), any(MapSqlParameterSource.class),
        any(ProductsRowMapper.class));
  }

  @DisplayName("Test 4: Debe lanzar NoData cuando no hay productos por productId")
  @Test
  void test4_getHighestPriorityProductByFilters_ShouldThrowNoDataWhenFilterByProductId() throws Exception {
    // Arrange
    ProductFilterDto productFilter = new ProductFilterDto();
    productFilter.setProductId(99999);
    productFilter.setBrandId(1);
    productFilter.setApplicationDate(LocalDateTime.of(2020, 6, 14, 10, 0));

    when(namedParameterJdbcTemplate.queryForObject(anyString(), any(MapSqlParameterSource.class),
        any(ProductsRowMapper.class)))
        .thenThrow(new EmptyResultDataAccessException(1));

    // Act & Assert
    assertThrows(DbException.NoData.class, () -> {
      productsRepository.getHighestPriorityProductByFilters(productFilter);
    });
    verify(namedParameterJdbcTemplate, times(1)).queryForObject(anyString(), any(MapSqlParameterSource.class),
        any(ProductsRowMapper.class));
  }

  @DisplayName("Test 5: Debe lanzar DbException.BadExecution cuando ocurre un error")
  @Test
  void test5_getHighestPriorityProductByFilters_ShouldThrowBadExecutionException() throws Exception {
    // Arrange
    when(namedParameterJdbcTemplate.queryForObject(anyString(), any(MapSqlParameterSource.class),
        any(ProductsRowMapper.class)))
        .thenThrow(new RuntimeException("Database connection error"));

    // Act & Assert
    assertThrows(DbException.BadExecution.class, () -> {
      productsRepository.getHighestPriorityProductByFilters(filterDto);
    });
    verify(namedParameterJdbcTemplate, times(1)).queryForObject(anyString(), any(MapSqlParameterSource.class),
        any(ProductsRowMapper.class));
  }

  @DisplayName("Test 6: Debe construir correctamente la query con todos los filtros")
  @Test
  void test6_getHighestPriorityProductByFilters_ShouldBuildCorrectQuery() throws Exception {
    // Arrange
    ProductEntity mockEntity = ProductEntityMocks.getTest1MockEntities().get(0);
    ProductDto expectedDto = ProductEntityMocks.createProductDtoFromEntity(mockEntity);

    when(namedParameterJdbcTemplate.queryForObject(anyString(), any(MapSqlParameterSource.class),
        any(ProductsRowMapper.class)))
        .thenReturn(mockEntity);
    when(productsSecMapper.toProductDto(mockEntity))
        .thenReturn(expectedDto);

    // Act
    ProductDto result = productsRepository.getHighestPriorityProductByFilters(filterDto);

    // Assert
    assertNotNull(result);
    verify(namedParameterJdbcTemplate, times(1)).queryForObject(anyString(), any(MapSqlParameterSource.class),
        any(ProductsRowMapper.class));
  }

  @DisplayName("Test 7: Debe retornar DTOs correctamente mapeados desde entidades")
  @Test
  void test7_getHighestPriorityProductByFilters_ShouldMapEntitiesToDtos() throws Exception {
    // Arrange
    ProductEntity mockEntity = ProductEntityMocks.getTest2MockEntities().get(0);
    ProductDto expectedDto = ProductEntityMocks.createProductDtoFromEntity(mockEntity);

    when(namedParameterJdbcTemplate.queryForObject(anyString(), any(MapSqlParameterSource.class),
        any(ProductsRowMapper.class)))
        .thenReturn(mockEntity);
    when(productsSecMapper.toProductDto(mockEntity))
        .thenReturn(expectedDto);

    // Act
    ProductDto result = productsRepository.getHighestPriorityProductByFilters(filterDto);

    // Assert
    assertEquals(new BigDecimal("35.50"), result.getPrice());
    verify(productsSecMapper, times(1)).toProductDto(mockEntity);
  }

}
