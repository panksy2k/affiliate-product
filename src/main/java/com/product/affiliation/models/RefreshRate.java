package com.product.affiliation.models;

public class RefreshRate {
  private final RateUnit measure;
  private final Short value;

  public RefreshRate(RateUnit measure, Short value) {
    this.measure = measure;
    this.value = value;
  }

  public RateUnit getMeasure() {
    return measure;
  }

  public Short getValue() {
    return value;
  }

  enum RateUnit {
    HERTZ("Hz");

    private final String unitString;

    RateUnit(String unitString) {
      this.unitString = unitString;
    }

    @Override
    public String toString() {
      return this.unitString;
    }
  }
}
