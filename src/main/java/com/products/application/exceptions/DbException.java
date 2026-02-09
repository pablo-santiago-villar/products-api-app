package com.products.application.exceptions;

import java.io.Serial;

public class DbException {

  public static class BadExecution extends Exception {

    @Serial
    private static final long serialVersionUID = 960850727716258180L;

    public BadExecution(String msg) {
      super(msg);
    }

  }

  public static class NoData extends Exception {

    @Serial
    private static final long serialVersionUID = 960850727716258181L;

    public NoData(String msg) {
      super(msg);
    }

  }

}
