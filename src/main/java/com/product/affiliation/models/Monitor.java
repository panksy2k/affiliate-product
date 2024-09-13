package com.product.affiliation.models;

public class Monitor extends Product {
  private RefreshRate refreshRate;
  private ResponseTime responseTime;
  private ScreenSize screenSize;

  public Monitor(String id, String productType, String modelName) {
    super(id, productType, modelName);
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
}
