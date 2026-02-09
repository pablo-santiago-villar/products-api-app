package com.products.priadapter.config.validation;

import com.products.application.utils.Utils;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.util.StringUtils;

public class DateFormatValidator implements ConstraintValidator<ValidDateFormat, String> {

  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    if (!StringUtils.hasText(value)) {
      return true; // @NotBlank se encargará de validar si está vacío
    }
    try {
      Utils.stringToLocalDateTime(value);
      return true;
    } catch (Exception e) {
      return false;
    }
  }
}
