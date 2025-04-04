package com.product.affiliation.repositories;

import com.product.affiliation.models.Monitor;
import io.vertx.core.json.JsonObject;
import java.util.function.Function;

/**
 * To map the domain object into mongodb Json object for persistence
 */
public class MonitorJsonObjectMapper implements Function<Monitor, JsonObject> {

  @Override
  public JsonObject apply(Monitor product) {
    JsonObject params = new JsonObject();

    params.put(MonitorRepository.PRODUCT_TYPE, product.getProductType());
    params.put(MonitorRepository.REFRESH_RATE, product.getRefreshRate().toString());
    params.put(MonitorRepository.RESPONSE_TIME, product.getResponseTime().toString());
    params.put(MonitorRepository.SCREEN_SIZE, product.getScreenSize().toString());
    params.put(MonitorRepository.MODEL_NAME, product.getModelName());

    params.put(MonitorRepository.WARRANTY, nullSafeToString(product.getWarranty()));
    params.put(MonitorRepository.AFFILIATE_LINK, product.getAffiliateLink());
    params.put(MonitorRepository.PRODUCT_CONDITION, nullSafeToString(product.getProductCondition()));
    params.put(MonitorRepository.PRICE, nullSafeToString(product.getPrice()));
    params.put(MonitorRepository.DESCRIPTION, nullSafeToString(product.getDescription()));
    params.put(MonitorRepository.BRAND, nullSafeToString(product.getBrandName()));
    params.put(MonitorRepository.DISPLAY_RESOLUTION,
      nullSafeToValue(product.getMaxDisplayResolution(), disp -> disp.toString()));

    return params;
  }

  private <T> String nullSafeToString(T productAttribute) {
    if (productAttribute == null) {
      return "";
    }

    return productAttribute.toString();
  }

  private <T> String nullSafeToValue(T productAttribute, Function<T, String> toValue) {
    if (productAttribute == null) {
      return "";
    }

    return toValue.apply(productAttribute);
  }
}
