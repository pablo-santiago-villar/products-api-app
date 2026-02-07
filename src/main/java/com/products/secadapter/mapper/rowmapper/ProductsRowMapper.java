package com.products.secadapter.mapper.rowmapper;

import com.products.application.utils.Utils;
import com.products.secadapter.model.ProductEntity;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;

public class ProductsRowMapper implements RowMapper<ProductEntity> {

  @Override
  public ProductEntity mapRow(ResultSet rs, int rowNum) throws SQLException {

    ProductEntity productEntity = new ProductEntity();
    productEntity.setBrandId(rs.getInt("brandId"));
    productEntity.setStartDate(Utils.getLocalDateTimeFromResultSet(rs, "startDate"));
    productEntity.setEndDate(Utils.getLocalDateTimeFromResultSet(rs, "endDate"));
    productEntity.setPriceList(rs.getInt("priceList"));
    productEntity.setProductId(rs.getInt("productId"));
    productEntity.setPriority(rs.getInt("priority"));
    productEntity.setPrice(rs.getBigDecimal("price"));
    productEntity.setCurrency(rs.getString("currency"));

    return productEntity;

  }

}
