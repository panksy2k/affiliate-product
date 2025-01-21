package com.product.affiliation.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.obsidiandynamics.concat.Concat;
import java.util.Objects;

public class ProductPrice {
  @JsonProperty
  private Double cost;
  @JsonProperty
  private ProductCurrency productCurrency;

  public ProductPrice() {
    super();
  }

  public ProductPrice(Double cost, ProductCurrency productCurrency) {
    this.cost = Objects.requireNonNull(cost);
    this.productCurrency = Objects.requireNonNull(productCurrency);
  }

  public static ProductPrice parse(String productPrice) {
    if (productPrice == null) {
      return null;
    }

    String[] splittedProductPriceValue = productPrice.split(" ");
    if (splittedProductPriceValue.length == 1) {
      return null;
    }

    if (splittedProductPriceValue.length == 2) {
      return new ProductPrice(
        Double.parseDouble(splittedProductPriceValue[1]),
        ProductPrice.ProductCurrency.fromValue(splittedProductPriceValue[0]));
    }

    return null;
  }

  public Double getCost() {
    return cost;
  }

  public void setCost(Double cost) {
    this.cost = cost;
  }

  public ProductCurrency getProductCurrency() {
    return productCurrency;
  }

  public void setProductCurrency(ProductCurrency productCurrency) {
    this.productCurrency = productCurrency;
  }

  @Override
  public String toString() {
    return new Concat()
      .whenIsNotNull(this)
      .append(productCurrency.name())
      .append(" ")
      .append(cost)
      .toString();
  }

  public enum ProductCurrency {
    GBP, EUR, USD, CAD;

    public static ProductPrice.ProductCurrency fromValue(String valueStr) {
      for (ProductPrice.ProductCurrency r : ProductPrice.ProductCurrency.values()) {
        if (r.name().equals(valueStr)) {
          return r;
        }
      }

      return null;
    }
  }
}
