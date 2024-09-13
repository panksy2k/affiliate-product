package com.product.affiliation.models;

public class ScreenSize {
  private Float size;
  private ScreenUnit unit;

  public ScreenSize(Float size, ScreenUnit unit) {
    this.size = size;
    this.unit = unit;
  }

  public Float getSize() {
    return size;
  }

  public ScreenUnit getUnit() {
    return unit;
  }

  enum ScreenUnit {
    Inches;
  }
}
