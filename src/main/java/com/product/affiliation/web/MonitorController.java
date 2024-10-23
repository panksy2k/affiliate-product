package com.product.affiliation.web;

import com.product.affiliation.models.Monitor;
import com.product.affiliation.models.ProductQuery;
import com.product.affiliation.repositories.MonitorRepository;
import com.product.affiliation.validators.ProductValidator;
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

    ProductValidator.validateMonitorID(monitorID)
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

    ProductValidator.validateMonitorID(idParam)
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
      q.setKey(payloadElement.getString("k"));
      q.setValue(payloadElement.getValue("v"));
      q.setOperation(ProductQuery.Operator.valueOf(payloadElement.getString("operator")));

      queryParam.add(q);
    }

    monitorRepository.findMonitors(queryParam)
      .onSuccess(ms -> {
        JsonObject responseBody = JsonObject.mapFrom(ms);
        context.response().setStatusCode(200).end(responseBody.encode());
      })
      .onFailure(context::fail);
  }
}
