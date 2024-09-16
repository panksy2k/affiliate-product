package com.product.affiliation.repositories;

import com.product.affiliation.exceptions.ProductRepositoryException;
import com.product.affiliation.models.Monitor;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;

public class MonitorRepositoryImpl implements MonitorRepository {

  private final MongoClient mc;

  public MonitorRepositoryImpl(MongoClient mongoClient) {
    this.mc = mongoClient;
  }

  @Override
  public Future<Monitor> createMonitor(Monitor product) {
    if(product == null) {
      throw new ProductRepositoryException("Monitor object is null -- cannot persist");
    }

    JsonObject params = new JsonObject();
    params.put(PRODUCT_TYPE, product.getProductType());
    params.put(REFRESH_RATE, product.getRefreshRate().toString());
    params.put(RESPONSE_TIME, product.getResponseTime().toString());
    params.put(SCREEN_SIZE, product.getScreenSize().toString());
    params.put(MODEL_NAME, product.getModelName());

    return mc.save("products", params)
            .map(id -> Monitor.withId(id, product));
  }
}
