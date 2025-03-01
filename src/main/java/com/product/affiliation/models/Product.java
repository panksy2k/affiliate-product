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
  @JsonSetter
  protected final String description;

  protected Product(String id, String modelName, ProductPrice price, ProductType productType, String desc) {
    this.id = id;
    this.modelName = modelName;
    this.price = price;
    this.productType = productType;
    this.description = desc;
  }

  @JsonGetter("productType")
  protected abstract ProductType getProductType();

  @JsonGetter
  public String getId() {
    return id;
  }

  @JsonGetter
  public String getModelName() {
    return modelName;
  }

  @JsonGetter
  public ProductPrice getPrice() {
    return this.price;
  }

  @JsonGetter
  public String getDescription() {
    return this.description;
  }
}
