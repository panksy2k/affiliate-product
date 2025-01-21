package com.product.affiliation.models;

public enum ConditionProduct {
  New, Used;

  @Override
  public String toString() {
    return this.name();
  }
}
