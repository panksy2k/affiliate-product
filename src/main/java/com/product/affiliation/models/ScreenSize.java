package com.product.affiliation.models;

import com.obsidiandynamics.concat.Concat;

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

  public enum ScreenUnit {
    Inches;
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
}
