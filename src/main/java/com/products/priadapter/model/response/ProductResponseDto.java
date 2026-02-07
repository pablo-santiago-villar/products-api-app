package com.products.priadapter.model.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "DTO de respuesta con los datos del producto encontrado")
public class ProductResponseDto {

  @Schema(
      description = "ID del producto",
      example = "35455"
  )
  private Integer productId;

  @Schema(
      description = "ID de la marca",
      example = "1"
  )
  private Integer brandId;

  @Schema(
      description = "ID de la lista de precios aplicable",
      example = "2"
  )
  private Integer priceList;

  @Schema(
      description = "Fecha y hora de inicio de validez del precio (formato: yyyy-MM-dd'T'HH:mm:ss)",
      example = "2024-01-14T00:00:00"
  )
  private String startDate;

  @Schema(
      description = "Fecha y hora de fin de validez del precio (formato: yyyy-MM-dd'T'HH:mm:ss)",
      example = "2024-01-16T23:59:59"
  )
  private String endDate;

  @Schema(
      description = "Precio aplicable para el período especificado (en céntimos)",
      example = "3550"
  )
  private Long price;

}
