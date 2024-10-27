package com.product.affiliation;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.product.affiliation.config.ApplicationConfigurationManager;
import com.product.affiliation.config.EmbeddedApplicationConfigurationManager;
import com.product.affiliation.exceptions.DependencyCreationException;
import com.product.affiliation.modules.ProductModule;
import com.product.affiliation.web.MonitorController;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.CorsHandler;

public class MainVerticle extends AbstractVerticle {
  private final MonitorController monitorController;

  public MainVerticle(MonitorController monitorController) {
    this.monitorController = monitorController;
  }

  public static void main(String args[]) throws Exception {
    Vertx vertx = Vertx.vertx();

    //Retrieve the application configuration - pertaining to database etc
    ApplicationConfigurationManager configManager = new EmbeddedApplicationConfigurationManager(vertx);

    configManager.retrieveApplicationConfiguration()
      .compose(appCfg -> {
        try {
          ProductModule layers = new ProductModule(vertx, appCfg);
          return Future.succeededFuture(layers);
        } catch (DependencyCreationException e) {
          return Future.failedFuture(e);
        }
      })
      .map(prdModule -> {
        Injector injector = Guice.createInjector(prdModule);
        MainVerticle mainVerticle = injector.getInstance(MainVerticle.class);
        return mainVerticle;
      })
      .compose(vertx::deployVerticle)
      .onFailure(err -> {
        System.out.println(err.getMessage());
        vertx.close();
      })
      .onSuccess(handler -> {
        System.out.println(
          "Product Verticle is successfully instantiated and deployed all its dependencies!");
      });
  }

  @Override
  public void start(Promise<Void> promise) throws Exception {
    HttpServer server = vertx.createHttpServer();
    Router router = Router.router(vertx);

    router.route("/api/*").handler(BodyHandler.create());
    router.route("/api/*").handler(CorsHandler.create("*"));

    router.post("/api/monitor").handler(monitorController::createMonitor);
    router.delete("/api/monitor/:id").handler(monitorController::removeMonitor);
    router.get("/api/monitor/:id").handler(monitorController::findMonitorById);
    router.post("/api/monitors").handler(monitorController::findMonitors);

    server.requestHandler(router);
    server.listen(8080)
      .onFailure(promise::fail)
      .onSuccess(result -> promise.complete());
  }
}
