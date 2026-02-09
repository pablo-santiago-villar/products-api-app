package com.products.secadapter.repository;

import com.products.application.exceptions.DbException;
import com.products.application.model.dto.ProductDto;
import com.products.application.model.dto.ProductFilterDto;
import com.products.application.ports.secondary.ProductsRepository;
import com.products.secadapter.mapper.rowmapper.ProductsRowMapper;
import com.products.secadapter.mapper.rowmapper.ProductsSecMapper;
import com.products.secadapter.model.ProductEntity;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ProductsRepositoryImpl implements ProductsRepository {

  private final @NonNull NamedParameterJdbcTemplate namedParameterJdbcTemplate;

  private final @NonNull ProductsSecMapper productsSecMapper;

  private static final String QUERY_SELECT_HIGHEST_PRIORITY_PRODUCT_BY_FILTERS = """
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
      WHERE PRODUCT_ID = :productId
      AND BRAND_ID = :brandId
      AND :applicationDate BETWEEN START_DATE AND END_DATE
      ORDER BY PRIORITY DESC LIMIT 1
      """;

  @Override
  public ProductDto getHighestPriorityProductByFilters(ProductFilterDto productFilterDto)
      throws DbException.BadExecution, DbException.NoData {

    try {

      MapSqlParameterSource namedParameters = new MapSqlParameterSource();
      namedParameters.addValue("productId", productFilterDto.getProductId());
      namedParameters.addValue("brandId", productFilterDto.getBrandId());
      namedParameters.addValue("applicationDate", productFilterDto.getApplicationDate());

      ProductEntity productsEntity = namedParameterJdbcTemplate
          .queryForObject(QUERY_SELECT_HIGHEST_PRIORITY_PRODUCT_BY_FILTERS, namedParameters, new ProductsRowMapper());

      return productsSecMapper.toProductDto(productsEntity);

    } catch (EmptyResultDataAccessException e) {
      throw new DbException.NoData(e.getMessage());
    } catch (Exception e) {
      throw new DbException.BadExecution(e.getMessage());
    }

  }

}
