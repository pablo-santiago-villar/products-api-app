package com.products.secadapter.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductEntity {

  private Integer brandId;
  private LocalDateTime startDate;
  private LocalDateTime endDate;
  private Integer priceList;
  private Integer productId;
  private Integer priority;
  private BigDecimal price;
  private String currency;

}
