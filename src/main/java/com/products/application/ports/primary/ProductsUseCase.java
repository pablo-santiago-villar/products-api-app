package com.products.application.ports.primary;

import com.products.application.exceptions.DbException;
import com.products.application.model.dto.ProductDto;
import com.products.application.model.dto.ProductFilterDto;

public interface ProductsUseCase {

  ProductDto getProductByFilter(ProductFilterDto productFilterDto) throws DbException.BadExecution;

}
