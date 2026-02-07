package com.products.application.ports.secondary;

import com.products.application.exceptions.DbException;
import com.products.application.model.dto.ProductDto;
import com.products.application.model.dto.ProductFilterDto;
import java.util.List;

public interface ProductsRepository {

  List<ProductDto> getProductsByFilter(ProductFilterDto productFilterDto) throws DbException.BadExecution;

}
