package com.product.affiliation.models;

public abstract class Product {

  protected final String id;
  protected final String productType;
  protected final String modelName;

  protected Product(String id, String productType, String model) {
    this.id = id;
    this.productType = productType;
    this.modelName = model;
  }

  public String getId() {
    return id;
  }

  public String getModelName() {
    return modelName;
  }

  protected abstract String getProductType();
}
