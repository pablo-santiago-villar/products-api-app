package com.products.application.model.co;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductsCo {
  private List<ProductCo> products;

  public Optional<ProductCo> getHighestPriorityProduct() {

    return products.stream()
        .max(Comparator.comparingInt(ProductCo::getPriority));

  }

}
