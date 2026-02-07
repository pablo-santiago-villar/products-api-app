package com.products.priadapter.adapter;

import com.products.application.model.dto.ProductDto;
import com.products.application.model.dto.ProductFilterDto;
import com.products.application.ports.primary.ProductsUseCase;
import com.products.priadapter.mapper.ProductsPriMapper;
import com.products.priadapter.model.request.ProductFilterRequestDto;
import com.products.priadapter.model.response.BaseResponse;
import com.products.priadapter.model.response.ProductResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("controllerProducts")
@RequestMapping(path = "/products")
@RequiredArgsConstructor
@Tag(name = "Productos",
     description = "Endpoints para gestión y búsqueda de productos")
public class ControllerProducts {

  private final @NonNull ProductsUseCase productsUseCase;

  private final @NonNull ProductsPriMapper productsPriMapper;

  @PostMapping(value = "/search",
               produces = MediaType.APPLICATION_JSON_VALUE)
  @Operation(
      summary = "Buscar producto por filtros",
      description = "Busca un producto específico filtrando por ID de producto, marca y fecha de aplicación. " +
          "Retorna los detalles del producto incluyendo precio, rango de validez y lista de precios."
  )
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200",
          description = "Producto encontrado exitosamente",
          content = @Content(
              mediaType = "application/json",
              schema = @Schema(implementation = BaseResponse.class)
          )
      ),
      @ApiResponse(
          responseCode = "400",
          description = "Solicitud inválida - parámetros requeridos faltantes o formato de fecha incorrecto",
          content = @Content()
      ),
      @ApiResponse(
          responseCode = "404",
          description = "Producto no encontrado para los filtros especificados (ID producto, marca o fecha fuera de rango)",
          content = @Content()
      ),
      @ApiResponse(
          responseCode = "500",
          description = "Error interno del servidor",
          content = @Content()
      )
  })
  public ResponseEntity<BaseResponse<ProductResponseDto>> getProductByFilter(
      @Valid @RequestBody ProductFilterRequestDto request) {
    try {

      ProductFilterDto filter = productsPriMapper.toProductFilterDto(request);
      ProductDto resultService = productsUseCase.getProductByFilter(filter);

      ProductResponseDto result = productsPriMapper.toProductResponseDto(resultService);
      return new ResponseEntity<>(new BaseResponse<>("", result), HttpStatus.OK);

    } catch (Exception e) {
      String msgError = "Error consultando producto";
      return new ResponseEntity<>(new BaseResponse<>(msgError, null), HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

}
