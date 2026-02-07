package com.products.priadapter.model.request;

import com.products.application.utils.Utils;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "DTO para filtrar búsqueda de productos por ID de producto, marca y fecha de aplicación")
public class ProductFilterRequestDto {

  @NotBlank(message = "La fecha de aplicación es requerida")
  @Schema(
      description = "Fecha y hora de aplicación para validar el rango de precio (formato: yyyy-MM-dd'T'HH:mm:ss)",
      example = "2024-01-14T10:00:00",
      requiredMode = Schema.RequiredMode.REQUIRED
  )
  private String applicationDate;

  @NotNull(message = "Product ID es requerido")
  @Positive(message = "Product ID debe ser positivo")
  @Schema(
      description = "ID del producto a buscar",
      example = "35455",
      requiredMode = Schema.RequiredMode.REQUIRED
  )
  private Integer productId;

  @NotNull(message = "Brand ID es requerido")
  @Positive(message = "Brand ID debe ser positivo")
  @Schema(
      description = "ID de la marca",
      example = "1",
      requiredMode = Schema.RequiredMode.REQUIRED
  )
  private Integer brandId;

  @AssertTrue(message = "Formato fecha de aplicación no válido")
  boolean isValidApplicationDate() {

    try {
      if (!StringUtils.hasText(applicationDate)) {
        return true;
      }
      Utils.stringToLocalDateTime(applicationDate);
      return true;
    } catch (Exception e) {
      return false;
    }
  }

}