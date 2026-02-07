package com.products.application.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.products.application.mapper.ProductsMapper;
import com.products.application.model.co.ProductCo;
import com.products.application.model.co.ProductsCo;
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

  @Mock
  private ProductsMapper productsMapper;

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
    ProductCo expectedProductCo = new ProductCo();
    expectedProductCo.setBrandId(1);
    expectedProductCo.setProductId(35455);
    expectedProductCo.setPriceList(1);
    expectedProductCo.setPrice(new BigDecimal("35.50"));
    expectedProductCo.setPriority(0);

    when(productsRepository.getProductsByFilter(any(ProductFilterDto.class)))
        .thenReturn(mockProducts);
    when(productsMapper.toProductsCo(mockProducts))
        .thenReturn(new ProductsCo(List.of(expectedProductCo)));
    when(productsMapper.toProductDto(expectedProductCo))
        .thenReturn(mockProducts.get(0));

    // Act
    ProductDto result = productsUseCaseImpl.getProductByFilter(filterDto);

    // Assert
    assertNotNull(result);
    assertEquals(35455, result.getProductId());
    assertEquals(1, result.getPriceList());
    assertEquals(new BigDecimal("35.50"), result.getPrice());
    assertEquals(0, result.getPriority());
    verify(productsRepository, times(1)).getProductsByFilter(any(ProductFilterDto.class));
  }

  @DisplayName("Test 2: Debe retornar producto con mayor prioridad a las 16:00 del día 14")
  @Test
  void test2_getProductByFilter_ShouldReturnProductWithHighestPriority() throws Exception {
    // Arrange
    ProductFilterDto test2Filter = ProductDtoMocks.createProductFilterDto(35455, 1,
        LocalDateTime.of(2020, 6, 14, 16, 0));

    List<ProductDto> mockProducts = ProductDtoMocks.getTest2MockProducts();

    ProductCo expectedProductCo = new ProductCo();
    expectedProductCo.setBrandId(1);
    expectedProductCo.setProductId(35455);
    expectedProductCo.setPriceList(2);
    expectedProductCo.setPrice(new BigDecimal("25.45"));
    expectedProductCo.setPriority(1);

    when(productsRepository.getProductsByFilter(any(ProductFilterDto.class)))
        .thenReturn(mockProducts);
    when(productsMapper.toProductsCo(mockProducts))
        .thenReturn(new ProductsCo(List.of(
            new ProductCo(1, LocalDateTime.of(2020, 6, 14, 0, 0), LocalDateTime.of(2020, 12, 31, 23, 59, 59), 1, 35455,
                0, new BigDecimal("35.50"), "EUR"),
            expectedProductCo
        )));
    when(productsMapper.toProductDto(expectedProductCo))
        .thenReturn(mockProducts.get(1));

    // Act
    ProductDto result = productsUseCaseImpl.getProductByFilter(test2Filter);

    // Assert
    assertNotNull(result);
    assertEquals(35455, result.getProductId());
    assertEquals(2, result.getPriceList());
    assertEquals(new BigDecimal("25.45"), result.getPrice());
    assertEquals(1, result.getPriority());
    verify(productsRepository, times(1)).getProductsByFilter(any(ProductFilterDto.class));
  }

  @DisplayName("Test 3: Debe retornar null cuando no hay productos")
  @Test
  void test3_getProductByFilter_ShouldReturnNullWhenNoProducts() throws Exception {
    // Arrange
    when(productsRepository.getProductsByFilter(any(ProductFilterDto.class)))
        .thenReturn(List.of());
    when(productsMapper.toProductsCo(List.of()))
        .thenReturn(new ProductsCo(List.of()));

    // Act
    ProductDto result = productsUseCaseImpl.getProductByFilter(filterDto);

    // Assert
    assertNull(result);
    verify(productsRepository, times(1)).getProductsByFilter(any(ProductFilterDto.class));
  }

  @DisplayName("Test 4: Debe llamar al repositorio con el filtro correcto")
  @Test
  void test4_getProductByFilter_ShouldCallRepositoryWithCorrectFilter() throws Exception {
    // Arrange
    List<ProductDto> mockProducts = ProductDtoMocks.getTest1MockProducts();
    ProductCo expectedProductCo = new ProductCo();
    expectedProductCo.setPriority(0);

    when(productsRepository.getProductsByFilter(any(ProductFilterDto.class)))
        .thenReturn(mockProducts);
    when(productsMapper.toProductsCo(mockProducts))
        .thenReturn(new ProductsCo(List.of(expectedProductCo)));
    when(productsMapper.toProductDto(expectedProductCo))
        .thenReturn(mockProducts.get(0));

    // Act
    productsUseCaseImpl.getProductByFilter(filterDto);

    // Assert
    verify(productsRepository, times(1)).getProductsByFilter(filterDto);
  }

}
