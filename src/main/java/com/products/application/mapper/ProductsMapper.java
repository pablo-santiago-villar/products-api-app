package com.products.application.mapper;

import com.products.application.model.co.ProductCo;
import com.products.application.model.co.ProductsCo;
import com.products.application.model.dto.ProductDto;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueMappingStrategy;

@Mapper(componentModel = "spring",
        nullValueMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT)
public interface ProductsMapper {

  default ProductsCo toProductsCo(List<ProductDto> list) {
    ProductsCo productCo = new ProductsCo();
    productCo.setProducts(toProductCo(list));
    return productCo;
  }

  ProductCo toProductCo(ProductDto dto);

  List<ProductCo> toProductCo(List<ProductDto> list);

  ProductDto toProductDto(ProductCo co);

}
