package com.products.application.service;

import com.products.application.exceptions.DbException;
import com.products.application.model.dto.ProductDto;
import com.products.application.model.dto.ProductFilterDto;
import com.products.application.ports.primary.ProductsUseCase;
import com.products.application.ports.secondary.ProductsRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductsUseCaseImpl implements ProductsUseCase {

  private final @NonNull ProductsRepository productsRepository;

  @Override
  public ProductDto getProductByFilter(ProductFilterDto productFilterDto)
      throws DbException.BadExecution, DbException.NoData {

    return productsRepository.getHighestPriorityProductByFilters(productFilterDto);

  }

}
