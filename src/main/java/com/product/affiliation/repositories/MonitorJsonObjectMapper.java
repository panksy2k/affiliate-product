package com.product.affiliation.repositories;

import com.product.affiliation.models.Monitor;
import io.vertx.core.json.JsonObject;
import java.util.function.Function;

public class MonitorJsonObjectMapper implements Function<Monitor, JsonObject> {

  @Override
  public JsonObject apply(Monitor product) {
    JsonObject params = new JsonObject();

    params.put(MonitorRepository.PRODUCT_TYPE, product.getProductType());
    params.put(MonitorRepository.REFRESH_RATE, product.getRefreshRate().toString());
    params.put(MonitorRepository.RESPONSE_TIME, product.getResponseTime().toString());
    params.put(MonitorRepository.SCREEN_SIZE, product.getScreenSize().toString());
    params.put(MonitorRepository.MODEL_NAME, product.getModelName());

    return params;
  }
}
