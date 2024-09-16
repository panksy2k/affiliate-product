package com.product.affiliation.models;

import com.obsidiandynamics.concat.Concat;

public class ResponseTime {
  private Float value;
  private Measurement measurement;

  public ResponseTime(Float value, Measurement measurement) {
    this.value = value;
    this.measurement = measurement;
  }

  public Float getValue() {
    return value;
  }

  public Measurement getMeasurement() {
    return measurement;
  }

  enum Measurement {
    Milliseconds;
  }

  @Override
  public String toString() {
    return new Concat()
            .whenIsNotNull(this)
            .append(value)
            .append(" ")
            .append(measurement.name())
            .toString();
  }
}
