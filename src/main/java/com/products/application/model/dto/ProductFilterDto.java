package com.products.application.model.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductFilterDto {

  private LocalDateTime applicationDate;
  private Integer productId;
  private Integer brandId;

}
