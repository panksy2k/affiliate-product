package com.product.affiliation.web;

import com.product.affiliation.models.Monitor;
import com.product.affiliation.models.ProductQuery;
import com.product.affiliation.models.ProductType;
import com.product.affiliation.repositories.MonitorRepository;
import com.product.affiliation.validators.ProductValidator;
import io.vertx.core.CompositeFuture;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RequestBody;
import io.vertx.ext.web.RoutingContext;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
    RequestBody requestPayload = context.body();
    List<ProductQuery> queryParam = new ArrayList<>(1);

    if (requestPayload != null && requestPayload.asJsonObject() != null) {
      JsonObject payloadJSON = requestPayload.asJsonObject();

      Map<String, Object> payloadJsonAsMap = payloadJSON.getMap();

      final ProductType productType =
        Optional.ofNullable(payloadJsonAsMap.get("productType")).map(String::valueOf).map(ProductType::valueOf)
          .orElse(null);
      if (productType == ProductType.MONITOR) {
        List<?> payloadAsArray = (List<?>) payloadJsonAsMap.getOrDefault("attr", Collections.emptyList());
        if (!payloadAsArray.isEmpty()) {
          for (int i = 0; i < payloadAsArray.size(); i++) {
            Map<String, ?> payloadElement = (Map<String, ?>) payloadAsArray.get(i);

            ProductQuery q = new ProductQuery();
            q.setKey(String.valueOf(payloadElement.get("key")));
            q.setOperation(ProductQuery.Operator.valueOf(String.valueOf(payloadElement.get("operation"))));
            q.setValue(payloadElement.get("value"));

            queryParam.add(q);
          }
        }

        System.out.println("Received query " + queryParam);

        monitorRepository.findMonitors(queryParam)
          .onSuccess(ms -> {
            JsonArray responseBody = JsonArray.of(ms.toArray());
            System.out.println("Sending " + responseBody.encodePrettily());

            context.response().setStatusCode(200).end(responseBody.encode());
          })
          .onFailure(context::fail);
      } else {
        context.response().setStatusCode(400).end("Bad request");
      }
    }
  }

  public void findMonitorAttribute(RoutingContext context) {
    String attributeName = context.pathParam("name");
    String typeProduct = context.queryParams().get("productType");
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
