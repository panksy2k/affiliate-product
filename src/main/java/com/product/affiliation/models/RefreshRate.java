package com.product.affiliation.models;

import com.obsidiandynamics.concat.Concat;

public class RefreshRate {
  private final RateUnit measure;
  private final Integer value;

  public RefreshRate(RateUnit measure, Integer value) {
    this.measure = measure;
    this.value = value;
  }

  public RateUnit getMeasure() {
    return measure;
  }

  public Integer getValue() {
    return value;
  }

  @Override
  public String toString() {
    return new Concat()
            .whenIsNotNull(this)
            .append(value)
            .append(" ")
            .append(measure.unitString)
            .toString();
  }

  public enum RateUnit {
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
