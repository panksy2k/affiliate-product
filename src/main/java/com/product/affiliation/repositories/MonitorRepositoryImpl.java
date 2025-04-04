package com.product.affiliation.repositories;

import com.product.affiliation.exceptions.ProductRepositoryException;
import com.product.affiliation.models.Monitor;
import com.product.affiliation.models.ProductQuery;
import com.product.affiliation.util.JsonUtils;
import com.product.affiliation.util.Utils;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.BulkOperation;
import io.vertx.ext.mongo.FindOptions;
import io.vertx.ext.mongo.MongoClient;
import java.util.ArrayList;
import java.util.Collection;
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
    if (product == null) {
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
  public Future<List<Monitor>> findMonitors(List<ProductQuery> queryCriteria) {
    JsonObject query = new JsonObject();

    for (ProductQuery criteria : queryCriteria) {
      switch (criteria.getOperation()) {
        case GT:
        case LT: {
          query.put(criteria.getKey(), new JsonObject().put(criteria.getOperation().getValue(), criteria.getValue()));
          break;
        }
        case IS: {
          if (criteria.getValue() != null) {
            query.put(criteria.getKey(), criteria.getValue());
          }

          break;
        }
        case IN: {
          if (criteria.getValue() != null && criteria.getValue() instanceof List) {
            List payloadList = (List) criteria.getValue();
            JsonArray values = JsonUtils.flattenCollectionToMultiples(payloadList);
            query.put(criteria.getKey(), new JsonObject().put(criteria.getOperation().getValue(), values));
          }

          break;
        }
      }
    }

    return mc.find(COLLECTION_NAME, query)
      .map(l -> l.stream().map(monitorDocumentMapper::apply).collect(Collectors.toList()));
  }

  @Override
  public Future<List<String>> findMonitorsId(List<ProductQuery> queryCriteria) {
    JsonObject query = new JsonObject();

    for (ProductQuery criteria : queryCriteria) {
      switch (criteria.getOperation()) {
        case GT:
        case LT:
        case IS: {
          query.put(criteria.getKey(), new JsonObject().put(criteria.getOperation().getValue(), criteria.getValue()));
          break;
        }
        case IN: {
          Object inClauseBasedValue = criteria.getValue();
          System.out.println(inClauseBasedValue.getClass().getSimpleName());
          if (inClauseBasedValue instanceof Collection<?>) {
            Collection<?> flattenValueList = (Collection<?>) inClauseBasedValue;
            JsonArray mongoArrayObj = new JsonArray();
            flattenValueList.stream().forEach(e -> mongoArrayObj.add(e));
            System.out.println("Putting collection with in clause " + mongoArrayObj.toString());
            query.put(criteria.getKey(), new JsonObject().put(criteria.getOperation().getValue(), mongoArrayObj));
          }

          break;
        }
      }
    }

    return mc.find(COLLECTION_NAME, query)
      .map(l -> l.stream().map(monitorDocumentMapper::apply).map(Monitor::getId).collect(Collectors.toList()));
  }

  @Override
  public Future<Optional<Monitor>> findMonitorById(String id) {
    System.out.println("Fetching the monitors by ID ");

    JsonObject query = new JsonObject();
    query.put("_id", id);

    return mc.findOne(COLLECTION_NAME, query, null)
      .map(Optional::ofNullable)
      .map(result -> {
        if (result.isPresent()) {
          return Optional.of(monitorDocumentMapper.apply(result.get()));
        }

        return Optional.<Monitor>empty();
      });
  }

  @Override
  public Future<List<String>> findProductAttributes(String returnFieldName, String queryParamProductType) {
    JsonObject productTypeQuery = new JsonObject().put("productType", queryParamProductType.toUpperCase());
    JsonObject returnTypeFields = new JsonObject().put(returnFieldName, 1);

    return mc.findWithOptions(COLLECTION_NAME, productTypeQuery, new FindOptions().setFields(returnTypeFields))
      .map(ja -> {
        List<String> attributeValues = new ArrayList<>(ja.size());

        for (int i = 0; i < ja.size(); i++) {
          attributeValues.add(ja.get(i).getString(returnFieldName));
        }

        return attributeValues;
      });
  }
}
