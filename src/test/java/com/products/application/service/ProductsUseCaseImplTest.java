package com.products.application.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.products.application.exceptions.DbException;
import com.products.application.model.dto.ProductDto;
import com.products.application.model.dto.ProductFilterDto;
import com.products.application.ports.secondary.ProductsRepository;
import com.products.application.service.mocks.ProductDtoMocks;
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

@ExtendWith(MockitoExtension.class)
class ProductsUseCaseImplTest {

  @Mock
  private ProductsRepository productsRepository;

  @InjectMocks
  private ProductsUseCaseImpl productsUseCaseImpl;

  private ProductFilterDto filterDto;

  @BeforeEach
  void setUp() {
    filterDto = ProductDtoMocks.createProductFilterDto(35455, 1,
        LocalDateTime.of(2020, 6, 14, 10, 0));
  }

  @DisplayName("Test 1: Debe retornar producto con prioridad 0 a las 10:00 del día 14")
  @Test
  void test1_getProductByFilter_ShouldReturnProductWithLowestPriority() throws Exception {
    // Arrange
    List<ProductDto> mockProducts = ProductDtoMocks.getTest1MockProducts();
    ProductDto expectedDto = mockProducts.get(0);

    when(productsRepository.getHighestPriorityProductByFilters(any(ProductFilterDto.class)))
        .thenReturn(expectedDto);

    // Act
    ProductDto result = productsUseCaseImpl.getProductByFilter(filterDto);

    // Assert
    assertNotNull(result);
    assertEquals(35455, result.getProductId());
    assertEquals(1, result.getPriceList());
    assertEquals(new BigDecimal("35.50"), result.getPrice());
    assertEquals(0, result.getPriority());
    verify(productsRepository, times(1)).getHighestPriorityProductByFilters(any(ProductFilterDto.class));
  }

  @DisplayName("Test 2: Debe retornar producto con mayor prioridad a las 16:00 del día 14")
  @Test
  void test2_getProductByFilter_ShouldReturnProductWithHighestPriority() throws Exception {
    // Arrange
    ProductFilterDto test2Filter = ProductDtoMocks.createProductFilterDto(35455, 1,
        LocalDateTime.of(2020, 6, 14, 16, 0));

    List<ProductDto> mockProducts = ProductDtoMocks.getTest2MockProducts();
    ProductDto expectedDto = mockProducts.get(1);

    when(productsRepository.getHighestPriorityProductByFilters(any(ProductFilterDto.class)))
        .thenReturn(expectedDto);

    // Act
    ProductDto result = productsUseCaseImpl.getProductByFilter(test2Filter);

    // Assert
    assertNotNull(result);
    assertEquals(35455, result.getProductId());
    assertEquals(2, result.getPriceList());
    assertEquals(new BigDecimal("25.45"), result.getPrice());
    assertEquals(1, result.getPriority());
    verify(productsRepository, times(1)).getHighestPriorityProductByFilters(any(ProductFilterDto.class));
  }

  @DisplayName("Test 3: Debe lanzar NoData cuando no hay productos")
  @Test
  void test3_getProductByFilter_ShouldThrowNoDataWhenNoProducts() throws Exception {
    // Arrange
    when(productsRepository.getHighestPriorityProductByFilters(any(ProductFilterDto.class)))
        .thenThrow(new DbException.NoData("No se encontró producto con los filtros especificados"));

    // Act & Assert
    assertThrows(DbException.NoData.class, () -> {
      productsUseCaseImpl.getProductByFilter(filterDto);
    });
    verify(productsRepository, times(1)).getHighestPriorityProductByFilters(any(ProductFilterDto.class));
  }

  @DisplayName("Test 4: Debe llamar al repositorio con el filtro correcto")
  @Test
  void test4_getProductByFilter_ShouldCallRepositoryWithCorrectFilter() throws Exception {
    // Arrange
    List<ProductDto> mockProducts = ProductDtoMocks.getTest1MockProducts();
    ProductDto expectedDto = mockProducts.get(0);

    when(productsRepository.getHighestPriorityProductByFilters(any(ProductFilterDto.class)))
        .thenReturn(expectedDto);

    // Act
    productsUseCaseImpl.getProductByFilter(filterDto);

    // Assert
    verify(productsRepository, times(1)).getHighestPriorityProductByFilters(filterDto);
  }

}
