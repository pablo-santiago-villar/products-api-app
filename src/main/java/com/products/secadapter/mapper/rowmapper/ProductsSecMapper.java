package com.products.secadapter.mapper.rowmapper;

import com.products.application.model.dto.ProductDto;
import com.products.secadapter.model.ProductEntity;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueMappingStrategy;

@Mapper(componentModel = "spring",
        nullValueMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT)
public interface ProductsSecMapper {

  ProductDto toProductDto(ProductEntity entity);

  List<ProductDto> toProductDto(List<ProductEntity> list);

}
