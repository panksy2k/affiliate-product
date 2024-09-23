package com.product.affiliation.models;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.obsidiandynamics.concat.Concat;

public class ResponseTime {
  @JsonSetter("value")
  private Float value;
  @JsonSetter("measurement")
  private Measurement measurement;

  public ResponseTime(Float value, Measurement measurement) {
    this.value = value;
    this.measurement = measurement;
  }

  public ResponseTime() {
  }

  public Float getValue() {
    return value;
  }

  public Measurement getMeasurement() {
    return measurement;
  }

  public static ResponseTime parse(String responseTimeString) {
    if (responseTimeString == null) {
      return new ResponseTime();
    }

    String[] splittedResponseTimeValue = responseTimeString.split(" ");
    if (splittedResponseTimeValue.length == 1) {
      return new ResponseTime(Float.valueOf(splittedResponseTimeValue[0]), null);
    }

    if (splittedResponseTimeValue.length == 2) {
      return new ResponseTime(Float.valueOf(splittedResponseTimeValue[0]),
        ResponseTime.Measurement.fromValue(splittedResponseTimeValue[1]));
    }

    return new ResponseTime();
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

  public enum Measurement {
    Milliseconds;

    public static Measurement fromValue(String valueStr) {
      for (Measurement r : Measurement.values()) {
        if (r.name().equals(valueStr)) {
          return r;
        }
      }

      return null;
    }
  }
}
