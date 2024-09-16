package com.product.affiliation.models;

public abstract class Product {

  protected final String id;
  protected final String modelName;

  protected Product(String id, String model) {
    this.id = id;
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
