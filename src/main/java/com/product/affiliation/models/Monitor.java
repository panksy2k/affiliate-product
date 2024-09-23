package com.product.affiliation.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Monitor extends Product {
  @JsonProperty
  private RefreshRate refreshRate;
  @JsonProperty
  private ResponseTime responseTime;
  @JsonProperty
  private ScreenSize screenSize;

  public Monitor() {
    super(null, null);
  }

  public Monitor(String id, String modelName) {
    super(id, modelName);
  }

  @Override
  public String getProductType() {
    return "MONITOR";
  }

  public RefreshRate getRefreshRate() {
    return refreshRate;
  }

  public void setRefreshRate(RefreshRate refreshRate) {
    this.refreshRate = refreshRate;
  }

  public ResponseTime getResponseTime() {
    return responseTime;
  }

  public void setResponseTime(ResponseTime responseTime) {
    this.responseTime = responseTime;
  }

  public ScreenSize getScreenSize() {
    return screenSize;
  }

  public void setScreenSize(ScreenSize screenSize) {
    this.screenSize = screenSize;
  }

  public static Monitor withId(String id, Monitor transientObject) {
    Monitor temp = new Monitor(id, transientObject.getModelName());
    temp.setRefreshRate(transientObject.getRefreshRate());
    temp.setResponseTime(transientObject.getResponseTime());
    temp.setScreenSize(transientObject.getScreenSize());

    return temp;
  }

  @Override
  public String toString() {
    return "Monitor{" +
      "refreshRate=" + refreshRate +
      ", responseTime=" + responseTime +
      ", screenSize=" + screenSize +
      "} " + super.toString();
  }
}
