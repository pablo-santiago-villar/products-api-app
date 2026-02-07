package com.products.secadapter.repository.mocks;

import com.products.application.model.dto.ProductDto;
import com.products.secadapter.model.ProductEntity;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ProductEntityMocks {

  /**
   * Crea una entidad de producto con datos específicos
   */
  public static ProductEntity createProductEntity(Integer brandId, Integer productId, Integer priceList,
      LocalDateTime startDate, LocalDateTime endDate,
      BigDecimal price, Integer priority) {
    ProductEntity entity = new ProductEntity();
    entity.setBrandId(brandId);
    entity.setProductId(productId);
    entity.setPriceList(priceList);
    entity.setStartDate(startDate);
    entity.setEndDate(endDate);
    entity.setPrice(price);
    entity.setPriority(priority);
    entity.setCurrency("EUR");
    return entity;
  }

  /**
   * Crea un DTO de producto a partir de una entidad
   */
  public static ProductDto createProductDtoFromEntity(ProductEntity entity) {
    ProductDto dto = new ProductDto();
    dto.setBrandId(entity.getBrandId());
    dto.setProductId(entity.getProductId());
    dto.setPriceList(entity.getPriceList());
    dto.setStartDate(entity.getStartDate());
    dto.setEndDate(entity.getEndDate());
    dto.setPrice(entity.getPrice());
    dto.setPriority(entity.getPriority());
    dto.setCurrency(entity.getCurrency());
    return dto;
  }

  /**
   * Retorna lista de entidades para simular Test 1 (10:00 del 14)
   */
  public static List<ProductEntity> getTest1MockEntities() {
    List<ProductEntity> entities = new ArrayList<>();
    entities.add(createProductEntity(1, 35455, 1,
        LocalDateTime.of(2020, 6, 14, 0, 0),
        LocalDateTime.of(2020, 12, 31, 23, 59, 59),
        new BigDecimal("35.50"), 0));
    return entities;
  }

  /**
   * Retorna lista de entidades para simular Test 2 (16:00 del 14)
   */
  public static List<ProductEntity> getTest2MockEntities() {
    List<ProductEntity> entities = new ArrayList<>();
    entities.add(createProductEntity(1, 35455, 1,
        LocalDateTime.of(2020, 6, 14, 0, 0),
        LocalDateTime.of(2020, 12, 31, 23, 59, 59),
        new BigDecimal("35.50"), 0));
    entities.add(createProductEntity(1, 35455, 2,
        LocalDateTime.of(2020, 6, 14, 15, 0),
        LocalDateTime.of(2020, 6, 14, 18, 30),
        new BigDecimal("25.45"), 1));
    return entities;
  }

  /**
   * Retorna lista de entidades para simular Test 3 (21:00 del 14)
   */
  public static List<ProductEntity> getTest3MockEntities() {
    List<ProductEntity> entities = new ArrayList<>();
    entities.add(createProductEntity(1, 35455, 1,
        LocalDateTime.of(2020, 6, 14, 0, 0),
        LocalDateTime.of(2020, 12, 31, 23, 59, 59),
        new BigDecimal("35.50"), 0));
    return entities;
  }

  /**
   * Retorna lista de entidades para simular Test 4 (10:00 del 15)
   */
  public static List<ProductEntity> getTest4MockEntities() {
    List<ProductEntity> entities = new ArrayList<>();
    entities.add(createProductEntity(1, 35455, 1,
        LocalDateTime.of(2020, 6, 14, 0, 0),
        LocalDateTime.of(2020, 12, 31, 23, 59, 59),
        new BigDecimal("35.50"), 0));
    entities.add(createProductEntity(1, 35455, 3,
        LocalDateTime.of(2020, 6, 15, 0, 0),
        LocalDateTime.of(2020, 6, 15, 11, 0),
        new BigDecimal("30.50"), 1));
    return entities;
  }

  /**
   * Retorna lista de entidades para simular Test 5 (21:00 del 16)
   */
  public static List<ProductEntity> getTest5MockEntities() {
    List<ProductEntity> entities = new ArrayList<>();
    entities.add(createProductEntity(1, 35455, 4,
        LocalDateTime.of(2020, 6, 15, 16, 0),
        LocalDateTime.of(2020, 12, 31, 23, 59, 59),
        new BigDecimal("38.95"), 1));
    return entities;
  }

  /**
   * Retorna lista vacía de entidades
   */
  public static List<ProductEntity> getEmptyEntities() {
    return new ArrayList<>();
  }

}
