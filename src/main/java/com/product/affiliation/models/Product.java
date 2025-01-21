package com.product.affiliation.models;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;

public abstract class Product {
  @JsonSetter
  protected final String id;
  @JsonSetter
  protected final String modelName;
  @JsonSetter
  protected final ProductType productType;
  @JsonSetter
  protected final ProductPrice price;

  protected Product(String id, String modelName, ProductPrice price, ProductType productType) {
    this.id = id;
    this.modelName = modelName;
    this.price = price;
    this.productType = productType;
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
  protected abstract ProductType getProductType();

  @JsonGetter
  protected abstract ProductPrice getPrice();
}
