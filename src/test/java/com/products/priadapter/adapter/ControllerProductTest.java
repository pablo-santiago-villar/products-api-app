package com.products.priadapter.adapter;

import static com.products.priadapter.adapter.mocks.ProductMocks.getTest1Request;
import static com.products.priadapter.adapter.mocks.ProductMocks.getTest1Response;
import static com.products.priadapter.adapter.mocks.ProductMocks.getTest2Request;
import static com.products.priadapter.adapter.mocks.ProductMocks.getTest2Response;
import static com.products.priadapter.adapter.mocks.ProductMocks.getTest3Request;
import static com.products.priadapter.adapter.mocks.ProductMocks.getTest3Response;
import static com.products.priadapter.adapter.mocks.ProductMocks.getTest4Request;
import static com.products.priadapter.adapter.mocks.ProductMocks.getTest4Response;
import static com.products.priadapter.adapter.mocks.ProductMocks.getTest5Request;
import static com.products.priadapter.adapter.mocks.ProductMocks.getTest5Response;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.products.priadapter.model.response.BaseResponse;
import com.products.priadapter.model.response.ProductResponseDto;
import com.products.products_api.ProductsApiApplication;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@SpringBootTest(classes = ProductsApiApplication.class)
class ControllerProductTest {

  private MockMvc mockMvc;

  @Autowired
  private ControllerProducts controllerProducts;

  @Autowired
  private Gson gson;

  @BeforeEach
  public void init() {
    mockMvc = MockMvcBuilders.standaloneSetup(controllerProducts).build();
  }

  @DisplayName("Test 1: petición a las 10:00 del día 14 del producto 35455   para la brand 1 (ZARA)")
  @Test
  void test1_getProductTest() throws Exception {

    String resultado = mockMvc.perform(post("/products/search").contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(gson.toJson(getTest1Request())))
        .andExpect(status().isOk())
        .andReturn().getResponse().getContentAsString();

    BaseResponse<ProductResponseDto> responseObject = gson.fromJson(resultado,
        new TypeToken<BaseResponse<ProductResponseDto>>() {
        }.getType());

    assertEquals(getTest1Response(), responseObject.getData());
  }

  @DisplayName("Test 2: petición a las 16:00 del día 14 del producto 35455   para la brand 1 (ZARA)")
  @Test
  void test2_getProductTest() throws Exception {

    String resultado = mockMvc.perform(post("/products/search").contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(gson.toJson(getTest2Request())))
        .andExpect(status().isOk())
        .andReturn().getResponse().getContentAsString();

    BaseResponse<ProductResponseDto> responseObject = gson.fromJson(resultado,
        new TypeToken<BaseResponse<ProductResponseDto>>() {
        }.getType());

    assertEquals(getTest2Response(), responseObject.getData());
  }

  @DisplayName("Test 3: petición a las 21:00 del día 14 del producto 35455   para la brand 1 (ZARA)")
  @Test
  void test3_getProductTest() throws Exception {

    String resultado = mockMvc.perform(post("/products/search").contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(gson.toJson(getTest3Request())))
        .andExpect(status().isOk())
        .andReturn().getResponse().getContentAsString();

    BaseResponse<ProductResponseDto> responseObject = gson.fromJson(resultado,
        new TypeToken<BaseResponse<ProductResponseDto>>() {
        }.getType());

    assertEquals(getTest3Response(), responseObject.getData());
  }

  @DisplayName("Test 4: petición a las 10:00 del día 15 del producto 35455   para la brand 1 (ZARA)")
  @Test
  void test4_getProductTest() throws Exception {

    String resultado = mockMvc.perform(post("/products/search").contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(gson.toJson(getTest4Request())))
        .andExpect(status().isOk())
        .andReturn().getResponse().getContentAsString();

    BaseResponse<ProductResponseDto> responseObject = gson.fromJson(resultado,
        new TypeToken<BaseResponse<ProductResponseDto>>() {
        }.getType());

    assertEquals(getTest4Response(), responseObject.getData());
  }

  @DisplayName("Test 5: petición a las 21:00 del día 16 del producto 35455   para la brand 1 (ZARA)")
  @Test
  void test5_getProductTest() throws Exception {

    String resultado = mockMvc.perform(post("/products/search").contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(gson.toJson(getTest5Request())))
        .andExpect(status().isOk())
        .andReturn().getResponse().getContentAsString();

    BaseResponse<ProductResponseDto> responseObject = gson.fromJson(resultado,
        new TypeToken<BaseResponse<ProductResponseDto>>() {
        }.getType());

    assertEquals(getTest5Response(), responseObject.getData());

  }

}
