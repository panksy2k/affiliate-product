package com.product.affiliation.models;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.obsidiandynamics.concat.Concat;

public class ProductWarranty {
  @JsonSetter
  private Integer warrantyValue;
  @JsonSetter
  private Warranty warrantyUnit;

  public ProductWarranty() {
    this.warrantyValue = null;
    this.warrantyUnit = null;
  }

  public ProductWarranty(Integer warrantyValue, Warranty warrantyUnit) {
    this.warrantyValue = warrantyValue;
    this.warrantyUnit = warrantyUnit;
  }

  public Integer getWarrantyValue() {
    return warrantyValue;
  }

  public void setWarrantyValue(Integer warrantyValue) {
    this.warrantyValue = warrantyValue;
  }

  public Warranty getWarrantyUnit() {
    return warrantyUnit;
  }

  public void setWarrantyUnit(Warranty warrantyUnit) {
    this.warrantyUnit = warrantyUnit;
  }

  @Override
  public String toString() {
    return new Concat()
      .whenIsNotNull(this)
      .append(warrantyValue)
      .append(" ")
      .append(warrantyUnit.name())
      .toString();
  }

  public enum Warranty {
    Months, Years
  }
}
