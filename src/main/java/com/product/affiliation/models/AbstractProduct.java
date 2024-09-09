package com.product.affiliation.models;

import java.util.Collections;
import java.util.Map;

public abstract class AbstractProduct {

  protected final String id;
  protected final String productType;
  protected final Map<String, ?> productAttributes;

  protected AbstractProduct(String id, String productType) {
    this.id = id;
    this.productType = productType;
    this.productAttributes = Collections.emptyMap();
  }

  protected AbstractProduct(String id, String productType, Map<String, ?> attributes) {
    this.id = id;
    this.productType = productType;
    this.productAttributes = attributes;
  }

  protected abstract String getProductType();
  protected abstract Map<String, ?> getProductAttributes();
}
