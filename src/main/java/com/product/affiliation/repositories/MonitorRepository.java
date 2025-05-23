package com.product.affiliation.repositories;

import com.product.affiliation.models.Monitor;
import com.product.affiliation.models.ProductQuery;
import io.vertx.core.Future;
import java.util.List;
import java.util.Optional;

public interface MonitorRepository {

  String PRODUCT_TYPE = "productType";
  String REFRESH_RATE = "refreshRate";
  String RESPONSE_TIME = "responseTime";
  String SCREEN_SIZE = "screenSize";
  String MODEL_NAME = "modelName";
  String PRICE = "price";
  String CURRENCY = "currency";
  String WARRANTY = "warranty";
  String COLLECTION_NAME = "monitors";
  String AFFILIATE_LINK = "affiliateURL";
  String PRODUCT_CONDITION = "productCondition";
  String DESCRIPTION = "description";
  String BRAND = "brandName";
  String vendor = "amazon.co.uk";
  String DISPLAY_RESOLUTION = "displayResolution";
  String SPECIAL_FEATURES = "specialFeatures";

  Future<Monitor> createMonitor(Monitor product);

  Future<Boolean> createMonitorInBatch(List<Monitor> monitors);

  Future<Boolean> removeMonitor(String id);

  Future<List<Monitor>> findMonitors(List<ProductQuery> queryCriteria);

  Future<List<String>> findMonitorsId(List<ProductQuery> queryCriteria);

  Future<Optional<Monitor>> findMonitorById(String id);

  Future<List<String>> findProductAttributes(String returnFieldName, String queryParamProductType);
}
