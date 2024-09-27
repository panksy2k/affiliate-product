package com.product.affiliation.models;

import com.fasterxml.jackson.annotation.JsonSetter;

public class ProductQuery {
  @JsonSetter("k")
  private String key;
  @JsonSetter("v")
  private Object value;
  @JsonSetter("operator")
  private Operator operation;

  public String getKey() {
    return key;
  }

  public void setKey(String key) {
    this.key = key;
  }

  public Object getValue() {
    return value;
  }

  public void setValue(Object value) {
    this.value = value;
  }

  public Operator getOperation() {
    return operation;
  }

  public void setOperation(Operator operation) {
    this.operation = operation;
  }

  public enum Operator {
    GT("$gt"), LT("$lt"), IS("");
    final String value;

    Operator(String mongoOperator) {
      value = mongoOperator;
    }

    public String getValue() {
      return value;
    }
  }
}
