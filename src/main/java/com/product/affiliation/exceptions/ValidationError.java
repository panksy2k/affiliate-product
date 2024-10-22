package com.product.affiliation.exceptions;

public class ValidationError extends RuntimeException {
  public ValidationError(String msg) {
    super(msg);
  }
}
