package com.products.application.service.mocks;

import com.products.application.model.dto.ProductDto;
import com.products.application.model.dto.ProductFilterDto;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ProductDtoMocks {

  /**
   * Crea un DTO de filtro para búsqueda de productos
   */
  public static ProductFilterDto createProductFilterDto(Integer productId, Integer brandId,
      LocalDateTime applicationDate) {
    ProductFilterDto filterDto = new ProductFilterDto();
    filterDto.setProductId(productId);
    filterDto.setBrandId(brandId);
    filterDto.setApplicationDate(applicationDate);
    return filterDto;
  }

  /**
   * Crea un DTO de producto con datos específicos
   */
  public static ProductDto createProductDto(Integer brandId, Integer productId, Integer priceList,
      LocalDateTime startDate, LocalDateTime endDate,
      BigDecimal price, Integer priority) {
    ProductDto dto = new ProductDto();
    dto.setBrandId(brandId);
    dto.setProductId(productId);
    dto.setPriceList(priceList);
    dto.setStartDate(startDate);
    dto.setEndDate(endDate);
    dto.setPrice(price);
    dto.setPriority(priority);
    dto.setCurrency("EUR");
    return dto;
  }

  /**
   * Retorna lista de productos para simular Test 1 (10:00 del 14)
   * Debería retornar el producto con prioridad 0 (PRICE_LIST 1)
   */
  public static List<ProductDto> getTest1MockProducts() {
    List<ProductDto> products = new ArrayList<>();
    products.add(createProductDto(1, 35455, 1,
        LocalDateTime.of(2020, 6, 14, 0, 0),
        LocalDateTime.of(2020, 12, 31, 23, 59, 59),
        new BigDecimal("35.50"), 0));
    return products;
  }

  /**
   * Retorna lista de productos para simular Test 2 (16:00 del 14)
   * Debería retornar el producto con mayor prioridad (PRICE_LIST 2, prioridad 1)
   */
  public static List<ProductDto> getTest2MockProducts() {
    List<ProductDto> products = new ArrayList<>();
    products.add(createProductDto(1, 35455, 1,
        LocalDateTime.of(2020, 6, 14, 0, 0),
        LocalDateTime.of(2020, 12, 31, 23, 59, 59),
        new BigDecimal("35.50"), 0));
    products.add(createProductDto(1, 35455, 2,
        LocalDateTime.of(2020, 6, 14, 15, 0),
        LocalDateTime.of(2020, 6, 14, 18, 30),
        new BigDecimal("25.45"), 1));
    return products;
  }

  /**
   * Retorna lista de productos para simular Test 3 (21:00 del 14)
   * Debería retornar el producto con prioridad 0 (PRICE_LIST 1)
   */
  public static List<ProductDto> getTest3MockProducts() {
    List<ProductDto> products = new ArrayList<>();
    products.add(createProductDto(1, 35455, 1,
        LocalDateTime.of(2020, 6, 14, 0, 0),
        LocalDateTime.of(2020, 12, 31, 23, 59, 59),
        new BigDecimal("35.50"), 0));
    return products;
  }

  /**
   * Retorna lista de productos para simular Test 4 (10:00 del 15)
   * Debería retornar el producto con prioridad 1 (PRICE_LIST 3)
   */
  public static List<ProductDto> getTest4MockProducts() {
    List<ProductDto> products = new ArrayList<>();
    products.add(createProductDto(1, 35455, 1,
        LocalDateTime.of(2020, 6, 14, 0, 0),
        LocalDateTime.of(2020, 12, 31, 23, 59, 59),
        new BigDecimal("35.50"), 0));
    products.add(createProductDto(1, 35455, 3,
        LocalDateTime.of(2020, 6, 15, 0, 0),
        LocalDateTime.of(2020, 6, 15, 11, 0),
        new BigDecimal("30.50"), 1));
    return products;
  }

  /**
   * Retorna lista de productos para simular Test 5 (21:00 del 16)
   * Debería retornar el producto con prioridad 1 (PRICE_LIST 4)
   */
  public static List<ProductDto> getTest5MockProducts() {
    List<ProductDto> products = new ArrayList<>();
    products.add(createProductDto(1, 35455, 4,
        LocalDateTime.of(2020, 6, 15, 16, 0),
        LocalDateTime.of(2020, 12, 31, 23, 59, 59),
        new BigDecimal("38.95"), 1));
    return products;
  }

}
