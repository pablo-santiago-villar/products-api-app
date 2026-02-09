package com.products.priadapter.mapper;

import com.products.application.model.dto.ProductDto;
import com.products.application.model.dto.ProductFilterDto;
import com.products.application.utils.Utils;
import com.products.priadapter.model.response.ProductResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueMappingStrategy;

@Mapper(componentModel = "spring",
        uses = Utils.class,
        nullValueMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT)
public interface ProductsPriMapper {

  ProductFilterDto toProductFilterDto(Integer brandId, Integer productId, String applicationDate);

  ProductResponseDto toProductResponseDto(ProductDto dto);

}