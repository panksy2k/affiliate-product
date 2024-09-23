package com.product.affiliation.models;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.obsidiandynamics.concat.Concat;

public class RefreshRate {
  @JsonSetter("measure")
  private final RateUnit measure;
  @JsonSetter("value")
  private final Integer value;

  public RefreshRate(RateUnit measure, Integer value) {
    this.measure = measure;
    this.value = value;
  }

  public RefreshRate() {
    this.measure = null;
    this.value = null;
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

  public static RefreshRate parse(String refreshRateString) {
    if (refreshRateString == null) {
      return new RefreshRate();
    }

    String[] splittedRefreshRateValue = refreshRateString.split(" ");
    if (splittedRefreshRateValue.length == 1) {
      return new RefreshRate(null, Integer.parseInt(splittedRefreshRateValue[0]));
    }

    if (splittedRefreshRateValue.length == 2) {
      return new RefreshRate(RateUnit.fromValue(splittedRefreshRateValue[1]),
        Integer.parseInt(splittedRefreshRateValue[0]));
    }

    return new RefreshRate();
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

    public static RateUnit fromValue(String valueStr) {
      for (RateUnit r : values()) {
        if (r.unitString.equals(valueStr)) {
          return r;
        }
      }

      return null;
    }
  }
}
