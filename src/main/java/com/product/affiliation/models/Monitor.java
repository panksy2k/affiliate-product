package com.product.affiliation.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.obsidiandynamics.concat.Concat;
import java.util.Set;

public class Monitor extends Product {
  @JsonProperty
  private RefreshRate refreshRate;
  @JsonProperty
  private ResponseTime responseTime;
  @JsonProperty
  private ScreenSize screenSize;
  @JsonProperty
  private String affiliateLink;
  @JsonProperty
  private ConditionProduct productCondition;
  @JsonProperty
  private ProductWarranty warranty;
  @JsonProperty
  private String brandName;
  @JsonProperty("displayResolution")
  private String maxDisplayResolution;
  @JsonProperty("specialFeatures")
  private Set<String> specialFeatures;

  public Monitor() {
    super(null, null, null, null, null);
  }

  public Monitor(String id, String modelName, ProductPrice price, ProductType productType, String description) {
    super(id, modelName, price, productType, description);
  }

  public static Monitor withId(String id, Monitor transientObject) {
    Monitor temp =
      new Monitor(id, transientObject.getModelName(), transientObject.getPrice(), transientObject.getProductType(),
        transientObject.getDescription());
    temp.setRefreshRate(transientObject.getRefreshRate());
    temp.setResponseTime(transientObject.getResponseTime());
    temp.setScreenSize(transientObject.getScreenSize());
    temp.setAffiliateLink(transientObject.getAffiliateLink());
    temp.setProductCondition(transientObject.getProductCondition());
    temp.setWarranty(transientObject.getWarranty());
    temp.setBrandName(transientObject.getBrandName());
    temp.setMaxDisplayResolution(transientObject.getMaxDisplayResolution());
    temp.setSpecialFeatures(transientObject.getSpecialFeatures());

    return temp;
  }

  public Set<String> getSpecialFeatures() {
    return specialFeatures;
  }

  public void setSpecialFeatures(Set<String> specialFeatures) {
    this.specialFeatures = specialFeatures;
  }

  @Override
  public ProductType getProductType() {
    return ProductType.MONITOR;
  }

  public String getAffiliateLink() {
    return affiliateLink;
  }

  public void setAffiliateLink(String affiliateLink) {
    this.affiliateLink = affiliateLink;
  }

  public ConditionProduct getProductCondition() {
    return productCondition;
  }

  public void setProductCondition(ConditionProduct productCondition) {
    this.productCondition = productCondition;
  }

  public ProductWarranty getWarranty() {
    return warranty;
  }

  public void setWarranty(ProductWarranty warranty) {
    this.warranty = warranty;
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

  public String getBrandName() {
    return brandName;
  }

  public void setBrandName(String brandName) {
    this.brandName = brandName;
  }

  public String getMaxDisplayResolution() {
    return maxDisplayResolution;
  }

  public void setMaxDisplayResolution(String maxDisplayResolution) {
    this.maxDisplayResolution = maxDisplayResolution;
  }

  @Override
  public String toString() {
    return
      new Concat()
        .whenIsNotNull(this)
        .append("refreshRate").when(refreshRate != null).append(refreshRate)
        .append(" responseTime: ").when(responseTime != null).append(responseTime)
        .append(" screenSize: ").when(screenSize != null).append(screenSize)
        .append(" affiliateLink: ").when(affiliateLink != null).append(affiliateLink)
        .append(" warranty: ").when(warranty != null).append(warranty)
        .append(" price: ").when(price != null).append(price)
        .append(" brandName: ").when(brandName != null).append(brandName)
        .append(" maxDisplay: ").when(maxDisplayResolution != null).append(maxDisplayResolution)
        .append(" specialFeatures: ").when(specialFeatures != null).append(specialFeatures)
        .toString();
  }
}
