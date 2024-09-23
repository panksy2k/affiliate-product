package com.product.affiliation.models;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.obsidiandynamics.concat.Concat;

public class ScreenSize {
  @JsonSetter("size")
  private Float size;
  @JsonSetter("unit")
  private ScreenUnit unit;

  public ScreenSize() {
  }

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

  public static ScreenSize parse(String screenSizeString) {
    if (screenSizeString == null) {
      return new ScreenSize();
    }

    String[] splittedResponseTimeValue = screenSizeString.split(" ");
    if (splittedResponseTimeValue.length == 1) {
      return new ScreenSize(Float.valueOf(splittedResponseTimeValue[0]), null);
    }

    if (splittedResponseTimeValue.length == 2) {
      return new ScreenSize(Float.valueOf(splittedResponseTimeValue[0]),
        ScreenSize.ScreenUnit.fromValue(splittedResponseTimeValue[1]));
    }

    return new ScreenSize();
  }

  @Override
  public String toString() {
    return new Concat()
            .whenIsNotNull(this)
            .append(size)
            .append(" ")
            .append(unit.name())
            .toString();
  }

  public enum ScreenUnit {
    Inches;

    public static ScreenUnit fromValue(String valueStr) {
      for (ScreenUnit r : ScreenUnit.values()) {
        if (r.name().equals(valueStr)) {
          return r;
        }
      }

      return null;
    }
  }
}
