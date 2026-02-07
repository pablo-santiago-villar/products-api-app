package com.products.secadapter.repository;

import com.products.application.exceptions.DbException;
import com.products.application.model.dto.ProductDto;
import com.products.application.model.dto.ProductFilterDto;
import com.products.application.ports.secondary.ProductsRepository;
import com.products.secadapter.mapper.rowmapper.ProductsRowMapper;
import com.products.secadapter.mapper.rowmapper.ProductsSecMapper;
import com.products.secadapter.model.ProductEntity;
import java.text.MessageFormat;
import java.util.List;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ProductsRepositoryImpl implements ProductsRepository {

  private final @NonNull NamedParameterJdbcTemplate namedParameterJdbcTemplate;

  private final @NonNull ProductsSecMapper productsSecMapper;

  private static final String QUERY_SELECT_TOP_REFERENCE_MAIN = """
      SELECT 
          BRAND_ID as brandId, 
          START_DATE as startDate, 
          END_DATE as endDate, 
          PRICE_LIST as priceList,
          PRODUCT_ID as productId, 
          PRIORITY as priority, 
          PRICE as price, 
          CURR as currency
      FROM PRICES 
      WHERE 1 = 1""";

  @Override
  public List<ProductDto> getProductsByFilter(ProductFilterDto productFilterDto) throws DbException.BadExecution {

    try {

      StringBuilder clauses = new StringBuilder();
      if (productFilterDto.getProductId() != null) {
        clauses.append(" AND PRODUCT_ID = :productId ");
      }

      if (productFilterDto.getBrandId() != null) {
        clauses.append(" AND BRAND_ID = :brandId ");
      }

      if (productFilterDto.getApplicationDate() != null) {
        clauses.append(" AND :applicationDate BETWEEN START_DATE AND END_DATE ");
      }

      MapSqlParameterSource namedParameters = new MapSqlParameterSource();
      namedParameters.addValue("productId", productFilterDto.getProductId());
      namedParameters.addValue("brandId", productFilterDto.getBrandId());
      namedParameters.addValue("applicationDate", productFilterDto.getApplicationDate());

      String query = MessageFormat.format("{0} {1}", QUERY_SELECT_TOP_REFERENCE_MAIN, clauses.toString());
      List<ProductEntity> productsEntity = namedParameterJdbcTemplate.query(query, namedParameters,
          new ProductsRowMapper());

      return productsSecMapper.toProductDto(productsEntity);

    } catch (Exception e) {
      throw new DbException.BadExecution(e.getMessage());
    }

  }

}
