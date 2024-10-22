package com.product.affiliation.web;

import com.product.affiliation.models.Monitor;
import com.product.affiliation.repositories.MonitorRepository;
import com.product.affiliation.validators.ProductValidator;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;

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
}
