package com.products.priadapter.model.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Respuesta estándar de la API que encapsula datos y mensajes de error")
public class BaseResponse<T> {

  @Schema(
      description = "Mensaje de error si ocurrió algún problema, vacío si la solicitud fue exitosa",
      example = ""
  )
  private String errorMessage;

  @Schema(
      description = "Datos de la respuesta, nulo si hubo un error"
  )
  private T data;

  public BaseResponse(String errorMessage, T data) {
    this.errorMessage = errorMessage;
    this.data = data;
  }

}
