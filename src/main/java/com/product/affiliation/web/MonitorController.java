package com.product.affiliation.web;

import com.product.affiliation.models.Monitor;
import com.product.affiliation.models.ProductQuery;
import com.product.affiliation.repositories.MonitorRepository;
import com.product.affiliation.validators.ProductValidator;
import io.vertx.core.CompositeFuture;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import java.util.ArrayList;
import java.util.List;

public class MonitorController {
  private final MonitorRepository monitorRepository;

  public MonitorController(MonitorRepository monitorRepository) {
    this.monitorRepository = monitorRepository;
  }

  public void createMonitor(RoutingContext context) {
    JsonObject bodyAsJson = context.getBodyAsJson();
    try {
      Monitor monitor = bodyAsJson.mapTo(Monitor.class);
      ProductValidator.validateCreateMonitor(monitor)
        .compose(monitorRepository::createMonitor)
        .onSuccess(m -> {
          JsonObject responseBody = JsonObject.mapFrom(m);
          context.response().setStatusCode(201).end(responseBody.encode());
        })
        .onFailure(context::fail);
    } catch (Exception e) {
      context.fail(e);
    }
  }

  public void removeMonitor(RoutingContext context) {
    String monitorID = context.pathParam("id");

    ProductValidator.validateEmptyValue(monitorID)
      .compose(mid -> monitorRepository.removeMonitor(monitorID))
      .onFailure(context::fail)
      .onSuccess(removed -> {
        JsonObject responseBody = new JsonObject();
        responseBody.put("deleted", removed);

        context.response().setStatusCode(200).end(responseBody.encode());
      });
  }

  public void findMonitorById(RoutingContext context) {
    String idParam = context.pathParam("id");

    ProductValidator.validateEmptyValue(idParam)
      .compose(id -> monitorRepository.findMonitorById(id))
      .onFailure(context::fail)
      .onSuccess(resultOpt -> {
        if (resultOpt.isPresent()) {
          JsonObject responseBody = JsonObject.mapFrom(resultOpt.get());
          context.response().setStatusCode(200).end(responseBody.encode());
        } else {
          context.response().setStatusCode(404).end();
        }
      });
  }

  public void findMonitors(RoutingContext context) {
    JsonArray payloadAsArray = context.body().asJsonArray();
    List<ProductQuery> queryParam = new ArrayList<>(payloadAsArray.size());

    for (int i = 0; i < payloadAsArray.size(); i++) {
      JsonObject payloadElement = payloadAsArray.getJsonObject(i);

      ProductQuery q = new ProductQuery();
      q.setKey(payloadElement.getString("key"));
      q.setValue(payloadElement.getValue("value"));
      q.setOperation(ProductQuery.Operator.valueOf(payloadElement.getString("operation")));

      queryParam.add(q);
    }

    monitorRepository.findMonitors(queryParam)
      .onSuccess(ms -> {
        JsonArray responseBody = JsonArray.of(ms.toArray());
        context.response().setStatusCode(200).end(responseBody.encode());
      })
      .onFailure(context::fail);
  }

  public void findMonitorAttribute(RoutingContext context) {
    String attributeName = context.pathParam("name");
    String typeProduct = context.pathParam("type");
    Future<String> attributeValidationFuture = ProductValidator.validateEmptyValue(attributeName);
    Future<String> typeNameValidationFuture = ProductValidator.validateEmptyValue(typeProduct);

    System.out.println("Fetching attribute " + attributeName);

    CompositeFuture compositeValidation = Future.any(attributeValidationFuture, typeNameValidationFuture);
    compositeValidation.compose(r -> monitorRepository.findProductAttributes(r.resultAt(0), r.resultAt(1)))
      .onFailure(context::fail)
      .onSuccess(result -> {
        if (result != null && !result.isEmpty()) {
          JsonArray responseBody = JsonArray.of(result.toArray(String[]::new));
          context.response().setStatusCode(200).end(responseBody.encode());
        } else {
          context.response().setStatusCode(404).end();
        }
      });
  }
}
