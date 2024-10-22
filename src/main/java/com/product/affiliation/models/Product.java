package com.product.affiliation.models;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;

public abstract class Product {
  @JsonSetter
  protected final String id;
  @JsonSetter
  protected final String modelName;

  protected String productType;


  protected Product(String id, String modelName) {
    this.id = id;
    this.modelName = modelName;
  }

  @JsonGetter
  public String getId() {
    return id;
  }

  @JsonGetter
  public String getModelName() {
    return modelName;
  }

  @JsonGetter("productType")
  protected abstract String getProductType();
  @JsonSetter("productType")
  protected abstract void setProductType(String productType);
}
