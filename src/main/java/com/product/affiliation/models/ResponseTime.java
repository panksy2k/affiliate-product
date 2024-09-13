package com.product.affiliation.models;

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
}
