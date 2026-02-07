package com.products.application.utils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.springframework.util.StringUtils;

public class Utils {

  private static final DateTimeFormatter LOCAL_DATE_TIME_FORMATTER =
      DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

  private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

  public static LocalDateTime getLocalDateTimeFromResultSet(ResultSet rs, String field) throws SQLException {
    Timestamp ts = rs.getTimestamp(field);
    if (ts != null) {
      return ts.toLocalDateTime();
    }
    return null;
  }

  public static LocalDateTime stringToLocalDateTime(String value) {

    if (StringUtils.hasText(value)) {
      return LocalDateTime.parse(value, LOCAL_DATE_TIME_FORMATTER);
    }
    return null;

  }

  public static String localDateTimeToString(LocalDateTime dateTime) {

    if (dateTime == null) {
      return "";
    }

    return dateTime.format(DATE_TIME_FORMATTER);

  }

}
