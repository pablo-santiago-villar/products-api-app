package com.products.application.ports.secondary;

import com.products.application.exceptions.DbException;
import com.products.application.model.dto.ProductDto;
import com.products.application.model.dto.ProductFilterDto;

public interface ProductsRepository {

  ProductDto getHighestPriorityProductByFilters(ProductFilterDto productFilterDto)
      throws DbException.BadExecution, DbException.NoData;

}
