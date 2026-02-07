package com.products.priadapter.adapter.mocks;

import com.products.priadapter.model.request.ProductFilterRequestDto;
import com.products.priadapter.model.response.ProductResponseDto;

public class ProductMocks {

  public static ProductFilterRequestDto getTest1Request() {
    ProductFilterRequestDto filterRequestDto = new ProductFilterRequestDto();
    filterRequestDto.setProductId(35455);
    filterRequestDto.setBrandId(1);
    filterRequestDto.setApplicationDate("2020-06-14T10:00:00");
    return filterRequestDto;
  }

  public static ProductFilterRequestDto getTest2Request() {
    ProductFilterRequestDto filterRequestDto = new ProductFilterRequestDto();
    filterRequestDto.setProductId(35455);
    filterRequestDto.setBrandId(1);
    filterRequestDto.setApplicationDate("2020-06-14T16:00:00");
    return filterRequestDto;
  }

  public static ProductFilterRequestDto getTest3Request() {
    ProductFilterRequestDto filterRequestDto = new ProductFilterRequestDto();
    filterRequestDto.setProductId(35455);
    filterRequestDto.setBrandId(1);
    filterRequestDto.setApplicationDate("2020-06-14T21:00:00");
    return filterRequestDto;
  }

  public static ProductFilterRequestDto getTest4Request() {
    ProductFilterRequestDto filterRequestDto = new ProductFilterRequestDto();
    filterRequestDto.setProductId(35455);
    filterRequestDto.setBrandId(1);
    filterRequestDto.setApplicationDate("2020-06-15T10:00:00");
    return filterRequestDto;
  }

  public static ProductFilterRequestDto getTest5Request() {
    ProductFilterRequestDto filterRequestDto = new ProductFilterRequestDto();
    filterRequestDto.setProductId(35455);
    filterRequestDto.setBrandId(1);
    filterRequestDto.setApplicationDate("2020-06-16T21:00:00");
    return filterRequestDto;
  }

  public static ProductResponseDto getTest1Response() {
    ProductResponseDto response = new ProductResponseDto();
    response.setProductId(35455);
    response.setBrandId(1);
    response.setPriceList(1);
    response.setStartDate("14/06/2020 00:00:00");
    response.setEndDate("31/12/2020 23:59:59");
    response.setPrice(35L);
    return response;
  }

  public static ProductResponseDto getTest2Response() {
    ProductResponseDto response = new ProductResponseDto();
    response.setProductId(35455);
    response.setBrandId(1);
    response.setPriceList(2);
    response.setStartDate("14/06/2020 15:00:00");
    response.setEndDate("14/06/2020 18:30:00");
    response.setPrice(25L);
    return response;
  }

  public static ProductResponseDto getTest3Response() {
    ProductResponseDto response = new ProductResponseDto();
    response.setProductId(35455);
    response.setBrandId(1);
    response.setPriceList(1);
    response.setStartDate("14/06/2020 00:00:00");
    response.setEndDate("31/12/2020 23:59:59");
    response.setPrice(35L);
    return response;
  }

  public static ProductResponseDto getTest4Response() {
    ProductResponseDto response = new ProductResponseDto();
    response.setProductId(35455);
    response.setBrandId(1);
    response.setPriceList(3);
    response.setStartDate("15/06/2020 00:00:00");
    response.setEndDate("15/06/2020 11:00:00");
    response.setPrice(30L);
    return response;
  }

  public static ProductResponseDto getTest5Response() {
    ProductResponseDto response = new ProductResponseDto();
    response.setProductId(35455);
    response.setBrandId(1);
    response.setPriceList(4);
    response.setStartDate("15/06/2020 16:00:00");
    response.setEndDate("31/12/2020 23:59:59");
    response.setPrice(38L);
    return response;
  }

}
