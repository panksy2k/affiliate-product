package com.product.affiliation.repositories;

import com.product.affiliation.exceptions.ProductRepositoryException;
import com.product.affiliation.models.Monitor;
import com.product.affiliation.util.Utils;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.BulkOperation;
import io.vertx.ext.mongo.MongoClient;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class MonitorRepositoryImpl implements MonitorRepository {

  private final MongoClient mc;

  private final MonitorDocumentMapper monitorDocumentMapper;

  public MonitorRepositoryImpl(MongoClient mongoClient) {
    this.mc = mongoClient;
    this.monitorDocumentMapper = new MonitorDocumentMapper();
  }

  @Override
  public Future<Monitor> createMonitor(Monitor product) {
    if(product == null) {
      throw new ProductRepositoryException("Monitor object is null -- cannot persist");
    }

    MonitorJsonObjectMapper f = new MonitorJsonObjectMapper();
    JsonObject params = f.apply(product);

    return mc.save(COLLECTION_NAME, params)
            .map(id -> Monitor.withId(id, product));
  }

  @Override
  public Future<Boolean> createMonitorInBatch(List<Monitor> monitors) {
    if (Utils.isEmpty(monitors)) {
      return Future.failedFuture("No data to persist");
    }

    final MonitorJsonObjectMapper f = new MonitorJsonObjectMapper();
    List<BulkOperation> insertMonitorOperation = monitors.stream()
      .map(f)
      .map(BulkOperation::createInsert)
      .collect(Collectors.toList());

    return mc.bulkWrite(COLLECTION_NAME, insertMonitorOperation)
      .map(result -> monitors.size() == result.getInsertedCount());
  }

  @Override
  public Future<Boolean> removeMonitor(String id) {
    JsonObject query = new JsonObject();
    query.put("_id", id);

    return mc.removeDocument("monitors", query)
      .map(result -> result.getRemovedCount() > 0);
  }

  @Override
  public Future<List<Monitor>> findMonitors(Monitor queryCriteria) {
    JsonObject query = new JsonObject();

    Optional.ofNullable(queryCriteria.getProductType()).ifPresent(ss -> query.put(PRODUCT_TYPE, ss));
    Optional.ofNullable(queryCriteria.getScreenSize()).ifPresent(ss -> query.put(SCREEN_SIZE, ss.toString()));
    Optional.ofNullable(queryCriteria.getRefreshRate()).ifPresent(rr -> query.put(REFRESH_RATE, rr.toString()));
    Optional.ofNullable(queryCriteria.getResponseTime()).ifPresent(rt -> query.put(RESPONSE_TIME, rt.toString()));
    Optional.ofNullable(queryCriteria.getModelName()).ifPresent(mn -> query.put(MODEL_NAME, mn));

    return mc.find(COLLECTION_NAME, query)
      .map(l -> l.stream().map(monitorDocumentMapper::apply).collect(Collectors.toList()));
  }
}
