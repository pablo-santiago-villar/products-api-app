package com.products.products_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.products")
public class ProductsApiApplication {

  public static void main(String[] args) {
    SpringApplication.run(ProductsApiApplication.class, args);
  }

}
