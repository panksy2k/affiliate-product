package com.product.affiliation.repositories;

import com.product.affiliation.models.Monitor;
import io.vertx.core.Future;

public interface MonitorRepository {

  String PRODUCT_TYPE = "productType";
  String REFRESH_RATE = "refreshRate";
  String RESPONSE_TIME = "responseTime";
  String SCREEN_SIZE = "screenSize";
  String MODEL_NAME = "modelName";

  Future<Monitor> createMonitor(Monitor product);
}